export interface CapPrinterPlugin {
  print(options: { url?: string; localPath?: string }): Promise<void>;
}
