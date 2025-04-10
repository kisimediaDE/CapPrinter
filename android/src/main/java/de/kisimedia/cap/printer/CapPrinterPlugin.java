package de.kisimedia.cap.printer;

import android.content.Context;
import android.os.AsyncTask;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@CapacitorPlugin(name = "CapPrinter")
public class CapPrinterPlugin extends com.getcapacitor.Plugin {

    @PluginMethod
    public void print(PluginCall call) {
        String localPath = call.getString("localPath");
        String pdfUrl = call.getString("url");
        JSObject options = call.getObject("options", new JSObject());

        String orientation = options.optString("orientation", "portrait");
        boolean duplex = options.optBoolean("duplex", false);
        String outputType = options.optString("outputType", "general");

        PrintAttributes.Builder attributesBuilder = new PrintAttributes.Builder();
        attributesBuilder.setMediaSize(
            orientation.equals("landscape") ? PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE : PrintAttributes.MediaSize.UNKNOWN_PORTRAIT
        );
        attributesBuilder.setDuplexMode(
            duplex ? PrintAttributes.DUPLEX_MODE_LONG_EDGE : PrintAttributes.DUPLEX_MODE_NONE
        );
        attributesBuilder.setColorMode(
            outputType.equals("grayscale") ? PrintAttributes.COLOR_MODE_MONOCHROME : PrintAttributes.COLOR_MODE_COLOR
        );

        if (localPath != null && !localPath.isEmpty()) {
            File file = new File(localPath);
            if (!file.exists()) {
                call.reject("Local file not found");
                return;
            }
            printPDF(file, attributesBuilder, call);
        } else if (pdfUrl != null && !pdfUrl.isEmpty()) {
            new DownloadAndPrintTask(getContext(), pdfUrl, attributesBuilder, call).execute();
        } else {
            call.reject("No URL or local file path provided");
        }
    }

    private void printPDF(File file, PrintAttributes.Builder builder, PluginCall call) {
        PrintManager printManager = (PrintManager) getContext().getSystemService(Context.PRINT_SERVICE);
        if (printManager == null) {
            call.reject("PrintManager not available");
            return;
        }

        PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(getContext(), file.getAbsolutePath());
        printManager.print("PDF Document", printAdapter, builder.build());
        call.resolve();
    }

    private static class DownloadAndPrintTask extends AsyncTask<Void, Void, File> {
        private final Context context;
        private final String url;
        private final PluginCall call;
        private final PrintAttributes.Builder builder;

        public DownloadAndPrintTask(Context context, String url, PrintAttributes.Builder builder, PluginCall call) {
            this.context = context;
            this.url = url;
            this.builder = builder;
            this.call = call;
        }

        @Override
        protected File doInBackground(Void... voids) {
            try {
                URL pdfUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) pdfUrl.openConnection();
                connection.connect();

                InputStream input = connection.getInputStream();
                File outputFile = new File(context.getCacheDir(), "temp.pdf");

                FileOutputStream output = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }

                output.close();
                input.close();

                return outputFile;
            } catch (Exception e) {
                Log.e("CapPrinterPlugin", "Error downloading PDF: ", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(File file) {
            if (file == null) {
                call.reject("Error downloading the PDF");
                return;
            }

            PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
            if (printManager == null) {
                call.reject("PrintManager not available");
                return;
            }

            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(context, file.getAbsolutePath());
            printManager.print("PDF Document", printAdapter, builder.build());

            call.resolve();
        }
    }
}
