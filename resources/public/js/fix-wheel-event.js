const EVENTS_TO_MODIFY = ['wheel'];

const originalAddEventListener = document.addEventListener.bind(document);

document.addEventListener = (type, listener, options, wantsUntrusted) => {
  let modOptions = options;

  if (EVENTS_TO_MODIFY.includes(type)) {
    if (typeof options === 'boolean') {
      modOptions = {
        capture: options,
        passive: false,
      };
    } else if (typeof options === 'object') {
      modOptions = {
        ...options,
        passive: false,
      };
    }
  }

  return originalAddEventListener(type, listener, modOptions, wantsUntrusted);
};

const originalRemoveEventListener = document.removeEventListener.bind(document);

document.removeEventListener = (type, listener, options) => {
  let modOptions = options;

  if (EVENTS_TO_MODIFY.includes(type)) {
    if (typeof options === 'boolean') {
      modOptions = {
        capture: options,
        passive: false,
      };
    } else if (typeof options === 'object') {
      modOptions = {
        ...options,
        passive: false,
      };
    }
  }

  return originalRemoveEventListener(type, listener, modOptions);
};
