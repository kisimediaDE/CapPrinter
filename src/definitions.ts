export interface CapPrinterPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
