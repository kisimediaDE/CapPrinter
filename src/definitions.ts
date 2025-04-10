export interface CapPrinterPlugin {
  print(options: PrintRequest): Promise<void>;
}

export interface PrintRequest {
  url?: string;
  localPath?: string;
  options?: PrintOptions;
}

export interface PrintOptions {
  /**
   * Output type: general, photo, or grayscale.
   */
  outputType?: 'general' | 'photo' | 'grayscale';

  /**
   * Page orientation: portrait or landscape.
   */
  orientation?: 'portrait' | 'landscape';

  /**
   * Duplex printing if supported by the printer.
   */
  duplex?: boolean;
}
