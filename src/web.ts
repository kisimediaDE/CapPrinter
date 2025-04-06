import { WebPlugin } from '@capacitor/core';

import type { CapPrinterPlugin } from './definitions';

export class CapPrinterWeb extends WebPlugin implements CapPrinterPlugin {
  async print(_options: { url?: string; localPath?: string }): Promise<void> {
    console.warn('PDF printing is only supported on native platforms.');
  }
}
