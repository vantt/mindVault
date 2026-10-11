/** Inspect Chrome's actual action popup target; it is not a Playwright tab. */
import { expect, type BrowserContext } from '@playwright/test';

export async function openActualToolbar(context: BrowserContext, extensionId: string) {
  const browser = context.browser();
  if (!browser) throw new Error('A headed Chrome browser is required');
  const cdp = await browser.newBrowserCDPSession();
  try {
  const worker = context.serviceWorkers().find(worker => worker.url().includes(extensionId));
  if (!worker) throw new Error('Extension service worker is unavailable');
  // Chrome creates its real action popover. No virtual popup or injected app UI.
  await worker.evaluate(() => chrome.action.openPopup());
  const url = `chrome-extension://${extensionId}/popup/popup.html`;
  let targetId = '';
  await expect.poll(async () => {
    const { targetInfos } = await cdp.send('Target.getTargets');
    targetId = targetInfos.find(target => target.url === url && target.type === 'page')?.targetId || '';
    return Boolean(targetId);
  }).toBe(true);
  const { sessionId } = await cdp.send('Target.attachToTarget', {targetId, flatten: false});
  let nextId = 0;

  async function evaluate<T = unknown>(expression: string): Promise<T> {
    const id = ++nextId;
    return new Promise<T>((resolve, reject) => {
      const finish = () => {
        clearTimeout(timer);
        cdp.off('Target.receivedMessageFromTarget', received);
      };
      const received = (event: {sessionId: string; message: string}) => {
        if (event.sessionId !== sessionId) return;
        const message = JSON.parse(event.message);
        if (message.id !== id) return;
        finish();
        if (message.error || message.result?.exceptionDetails) {
          reject(new Error('Actual popup evaluation failed'));
        } else resolve(message.result.result.value as T);
      };
      const timer = setTimeout(() => { finish(); reject(new Error('Actual popup evaluation timed out')); }, 10000);
      cdp.on('Target.receivedMessageFromTarget', received);
      cdp.send('Target.sendMessageToTarget', {sessionId, message: JSON.stringify({
        id, method: 'Runtime.evaluate', params: {expression, returnByValue: true, awaitPromise: true},
      })}).catch(error => { finish(); reject(error); });
    });
  }

  return {
    evaluate,
    async close() {
      try { await cdp.send('Target.closeTarget', {targetId}); }
      finally { await cdp.detach(); }
    },
  };
  } catch (error) {
    await cdp.detach();
    throw error;
  }
}
