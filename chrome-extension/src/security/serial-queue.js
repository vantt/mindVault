export function createSerialQueue() {
    let tail = Promise.resolve();
    return work => {
        const result = tail.then(work, work);
        tail = result.catch(() => {});
        return result;
    };
}
