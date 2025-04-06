import { WebPlugin } from '@capacitor/core';

import type { CapPrinterPlugin } from './definitions';

export class CapPrinterWeb extends WebPlugin implements CapPrinterPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
