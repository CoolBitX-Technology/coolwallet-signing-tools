import { createContext } from 'react';

interface Context {
  connected: boolean;
  setConnected(value: boolean): void;
  isLocked: boolean;
  setIsLocked(value: boolean): void;
}

const defaultValue: Context = {
  connected: false,
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  setConnected() {},
  isLocked: false,
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  setIsLocked() {},
};

const context = createContext<Context>(defaultValue);

export default context;
