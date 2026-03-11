package com.example.myadermoshop;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.AreaBreakType;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
import java.util.List;

/* loaded from: classes.dex */
public class PdfGenerator {
    private final DatabaseHelper dbHelper;

    public PdfGenerator(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
    }

    public Bitmap generateBarcodeWithText(String str) {
        try {
            EnumMap enumMap = new EnumMap(EncodeHintType.class);
            enumMap.put(EncodeHintType.MARGIN, 0);
            Bitmap bitmapCreateBitmap = new BarcodeEncoder().createBitmap(new MultiFormatWriter().encode(str, BarcodeFormat.CODE_128, 1100, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, enumMap));
            int height = bitmapCreateBitmap.getHeight();
            int i = height + 70;
            Bitmap bitmapCreateBitmap2 = Bitmap.createBitmap(bitmapCreateBitmap.getWidth(), height + 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapCreateBitmap2);
            canvas.drawBitmap(bitmapCreateBitmap, 0.0f, 0.0f, null);
            Paint paint = new Paint();
            paint.setTextSize(70);
            paint.setTextAlign(Paint.Align.CENTER);
            Rect bounds = new Rect();
            paint.getTextBounds(str, 0, str.length(), bounds);
            canvas.drawText(str, bitmapCreateBitmap.getWidth() / 2, (i - ((70 - bounds.height()) / 2)) + 10, paint);
            return bitmapCreateBitmap2;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createPdfWithBarcodes(List<String> list, String str) {
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(str));
            pdfDocument.setDefaultPageSize(PageSize.A4.rotate());
            Document document = new Document(pdfDocument);
            int i = this.dbHelper.getContext().getSharedPreferences("BarcodePrefs", 0).getInt("labelCount", 2);
            int i2 = 0;
            for (String str2 : list) {
                if (i2 == i) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                    i2 = 0;
                }
                Bitmap bitmapGenerateBarcodeWithText = generateBarcodeWithText(str2);
                if (bitmapGenerateBarcodeWithText != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmapGenerateBarcodeWithText.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    Image image = new Image(ImageDataFactory.create(byteArrayOutputStream.toByteArray()));
                    float width = pdfDocument.getDefaultPageSize().getWidth();
                    float f = 0.85f * width;
                    image.setWidth(f);
                    image.setFixedPosition((width - f) / 2.0f, i == 1 ? 150.0f : i2 == 0 ? 330.0f : 50.0f);
                    document.add(image);
                    i2++;
                }
            }
            document.close();
            this.dbHelper.insertPdfFile(str.substring(str.lastIndexOf('/') + 1), str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
