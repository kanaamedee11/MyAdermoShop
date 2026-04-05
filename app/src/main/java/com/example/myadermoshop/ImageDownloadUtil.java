package com.example.myadermoshop;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloadUtil {
    private static final String TAG = "ImageDownloadUtil";

    public static void downloadImage(Context context, final String str, String str2) {
        final File file = new File(context.getFilesDir(), str2);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ImageDownloadUtil.lambda$downloadImage$0(str, file);
                } catch (IOException e) {
                    Log.e(TAG, "Error downloading image", e);
                }
            }
        }).start();
    }

    static void lambda$downloadImage$0(String str, File file) throws IOException {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int i = inputStream.read(bArr);
                if (i != -1) {
                    fileOutputStream.write(bArr, 0, i);
                } else {
                    fileOutputStream.close();
                    inputStream.close();
                    Log.d(TAG, "Image downloaded successfully to " + file.getAbsolutePath());
                    return;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error downloading image", e);
        }
    }

    public static void downloadImageWithCustomPath(Context context, String str, String str2) {
        downloadImage(context, str, str2 + "/" + str.substring(str.lastIndexOf("/") + 1));
    }
}