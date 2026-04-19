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

    public interface DownloadCallback {
        void onSuccess(File file);
        void onFailure(String imageUrl);
    }

    // Fire and forget - skips silently on failure
    public static void downloadImage(Context context, String imageUrl, String relativePath) {
        downloadImage(context, imageUrl, relativePath, null);
    }

    // With optional callback
    public static void downloadImage(Context context, String imageUrl,
                                     String relativePath, DownloadCallback callback) {
        File file = new File(context.getFilesDir(), relativePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        new Thread(() -> {
            try {
                downloadToFile(imageUrl, file);
                Log.d(TAG, "Download succeeded: " + imageUrl);
                if (callback != null) callback.onSuccess(file);
            } catch (IOException e) {
                Log.w(TAG, "Skipping image, download failed: " + imageUrl);
                if (callback != null) callback.onFailure(imageUrl);
            }
        }).start();
    }

    private static void downloadToFile(String imageUrl, File destination) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            URL url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Connection", "close");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setDoInput(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("Server returned HTTP " + responseCode);
            }

            inputStream = connection.getInputStream();
            outputStream = new FileOutputStream(destination);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            Log.d(TAG, "Image saved to: " + destination.getAbsolutePath());

        } catch (IOException e) {
            if (destination.exists()) destination.delete();
            throw e;
        } finally {
            try { if (outputStream != null) outputStream.close(); } catch (IOException ignored) {}
            try { if (inputStream != null) inputStream.close(); } catch (IOException ignored) {}
            if (connection != null) connection.disconnect();
        }
    }

    public static void downloadImageWithCustomPath(Context context,
                                                   String imageUrl,
                                                   String folderPath) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        downloadImage(context, imageUrl, folderPath + "/" + fileName);
    }

    public static void downloadImageWithCustomPath(Context context,
                                                   String imageUrl,
                                                   String folderPath,
                                                   DownloadCallback callback) {
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        downloadImage(context, imageUrl, folderPath + "/" + fileName, callback);
    }
}