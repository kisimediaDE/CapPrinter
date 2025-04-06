package de.kisimedia.cap.printer;

import android.content.Context;
import android.os.AsyncTask;
import android.print.PrintManager;
import android.util.Log;

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

        if (localPath != null && !localPath.isEmpty()) {
            // Use the local file
            File file = new File(localPath);
            if (!file.exists()) {
                call.reject("Local file not found");
                return;
            }
            // Start printing using the local file
            PrintManager printManager = (PrintManager) getContext().getSystemService(Context.PRINT_SERVICE);
            if (printManager == null) {
                call.reject("PrintManager not available");
                return;
            }
            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(getContext(), file.getAbsolutePath());
            printManager.print("PDF Document", printAdapter, null);
            call.resolve();
        } else if (pdfUrl != null && !pdfUrl.isEmpty()) {
            // Download PDF in a background thread
            new DownloadAndPrintTask(getContext(), pdfUrl, call).execute();
        } else {
            call.reject("No URL or local file path provided");
        }
    }

    /**
     * Asynchronous task to download the PDF and then print it.
     */
    private static class DownloadAndPrintTask extends AsyncTask<Void, Void, File> {
        private final Context context;
        private final String url;
        private final PluginCall call;

        public DownloadAndPrintTask(Context context, String url, PluginCall call) {
            this.context = context;
            this.url = url;
            this.call = call;
        }

        @Override
        protected File doInBackground(Void... voids) {
            try {
                // Download the PDF
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

            // Start the printing process
            PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
            if (printManager == null) {
                call.reject("PrintManager not available");
                return;
            }

            // Create a custom adapter for the PDF
            PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(context, file.getAbsolutePath());
            printManager.print("PDF Document", printAdapter, null);

            // Report success
            call.resolve();
        }
    }
}
