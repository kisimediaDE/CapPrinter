import { WebPlugin } from '@capacitor/core';
import type { CapPrinterPlugin, PrintRequest } from './definitions';

export class CapPrinterWeb extends WebPlugin implements CapPrinterPlugin {
  async print(_options: PrintRequest): Promise<void> {
    console.warn('[CapPrinter] PDF printing is only supported on native platforms.');
  }
}
