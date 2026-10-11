import { describe, it, expect } from 'vitest';
import { readFileSync } from 'node:fs';
import { JSDOM } from 'jsdom';
import { localizeDemo } from '../../src/demo/demo.js';

const html = readFileSync('src/demo/demo.html', 'utf8');
const css = readFileSync('src/demo/demo.css', 'utf8');
const messages = language => JSON.parse(readFileSync(`src/_locales/${language}/messages.json`, 'utf8'));
const dom = () => new JSDOM(html);

describe('bundled static Recipe guide', () => {
  it('has complete EN/VI text and accessible image translations for every rendered key', () => {
    const doc = dom().window.document;
    const keys = new Set([...doc.querySelectorAll('[data-i18n], [data-i18n-alt]')]
      .map(element => element.dataset.i18n || element.dataset.i18nAlt));
    expect(keys.size).toBeGreaterThan(60);
    for (const language of ['en', 'vi']) {
      const locale = messages(language);
      for (const key of keys) expect(locale[key]?.message, `${language}: ${key}`).toBeTruthy();
    }
    expect(messages('vi').demoStepCopyBody.message).toContain('30');
    expect(messages('en').demoIdeaStorage.message).toContain('encrypted');
    expect(messages('en').demoModifiersBody.message).toContain('not reversible');
  });

  it('uses literal text substitutions while preserving synthetic Recipe markup', () => {
    const doc = dom().window.document;
    const examples = [...doc.querySelectorAll('code')].map(element => element.textContent);
    const unsafe = '<img src=x onerror="throw 1">';
    localizeDemo(doc, () => unsafe);
    expect(doc.querySelector('[data-i18n="demoIdeaBody"]').textContent).toBe(unsafe);
    expect(doc.querySelectorAll('img')).toHaveLength(1);
    expect(doc.querySelector('img').getAttribute('alt')).toBe(unsafe);
    expect([...doc.querySelectorAll('code')].map(element => element.textContent)).toEqual(examples);
  });

  it('contains no unmarked prose and stays self-contained without a remote font import', () => {
    const doc = dom().window.document;
    const walker = doc.createTreeWalker(doc.body, 4);
    const unmarked = [];
    while (walker.nextNode()) {
      const node = walker.currentNode, value = node.textContent.trim();
      if (!value || node.parentElement.closest('[data-i18n], code, script')) continue;
      if (/^(Pass|Chef|\d+|fb|#|1|_|!|\?|~)$/.test(value)) continue;
      unmarked.push(value);
    }
    expect(unmarked).toEqual([]);
    expect(css).not.toMatch(/@import|https?:\/\//);
    expect([...doc.querySelectorAll('[src],link[href]')].map(el => el.getAttribute('src') || el.getAttribute('href')))
      .toEqual(['demo.css', 'sheet-recipes.png', 'demo.js']);
    expect(doc.querySelector('#btn-open-builder')).toBeNull();
  });
});
