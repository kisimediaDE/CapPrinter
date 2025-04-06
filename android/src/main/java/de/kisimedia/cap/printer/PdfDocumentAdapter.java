package de.kisimedia.cap.printer;

import android.content.Context;
import android.os.CancellationSignal;
import android.print.PrintDocumentAdapter;
import android.print.PrintAttributes;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfDocumentAdapter extends PrintDocumentAdapter {
    private final Context context;
    private final String pdfPath;

    public PdfDocumentAdapter(Context context, String pdfPath) {
        this.context = context;
        this.pdfPath = pdfPath;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback,
                         android.os.Bundle extras) {

        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        PrintDocumentInfo info = new PrintDocumentInfo
                .Builder("temp.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .build();

        callback.onLayoutFinished(info, true);
    }

    @Override
    public void onWrite(android.print.PageRange[] pages, ParcelFileDescriptor destination,
                        CancellationSignal cancellationSignal,
                        WriteResultCallback callback) {

        FileInputStream input = null;
        FileOutputStream output = null;
        PrintedPdfDocument pdfDocument = null;

        try {
            File file = new File(pdfPath);
            input = new FileInputStream(file);
            output = new FileOutputStream(destination.getFileDescriptor());

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }

            callback.onWriteFinished(new android.print.PageRange[]{android.print.PageRange.ALL_PAGES});
        } catch (Exception e) {
            Log.e("PdfDocumentAdapter", "Error writing PDF: ", e);
            callback.onWriteFailed(e.toString());
        } finally {
            try {
                if (pdfDocument != null) {
                    pdfDocument.close();
                }
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                Log.e("PdfDocumentAdapter", "Error closing streams: ", e);
            }
        }
    }
}
