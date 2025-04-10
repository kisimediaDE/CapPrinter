import { registerPlugin } from '@capacitor/core';
import type { CapPrinterPlugin } from './definitions';

const CapPrinter = registerPlugin<CapPrinterPlugin>('CapPrinter', {
  web: () => import('./web').then((m) => new m.CapPrinterWeb()),
});

export * from './definitions';
export { CapPrinter };
