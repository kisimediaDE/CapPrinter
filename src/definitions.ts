/**
 * Plugin interface for CapPrinter.
 */
export interface CapPrinterPlugin {
  /**
   * Print a PDF document from a remote URL or local file path.
   *
   * Either `url` or `localPath` must be provided.
   */
  print(options: PrintRequest): Promise<void>;
}

/**
 * Parameters for a print request.
 */
export interface PrintRequest {
  /**
   * Remote PDF file URL.
   * Optional if `localPath` is provided.
   */
  url?: string;

  /**
   * Local file path to a PDF document.
   * Optional if `url` is provided.
   */
  localPath?: string;

  /**
   * Optional native print configuration.
   */
  options?: PrintOptions;
}

/**
 * Optional print configuration for native print dialogs.
 */
export interface PrintOptions {
  /**
   * Defines the output quality or color mode.
   *
   * - `general`: Default system quality
   * - `photo`: High-quality color/photo print
   * - `grayscale`: Black & white / grayscale
   */
  outputType?: 'general' | 'photo' | 'grayscale';

  /**
   * Sets the page orientation.
   *
   * - `portrait`: Standard vertical layout
   * - `landscape`: Horizontal layout
   */
  orientation?: 'portrait' | 'landscape';

  /**
   * Enables duplex printing if the printer supports it.
   */
  duplex?: boolean;
}
