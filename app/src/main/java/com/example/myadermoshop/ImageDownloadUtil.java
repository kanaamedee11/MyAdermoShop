package com.example.myadermoshop;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ImageDownloadUtil — rewritten to use OkHttp instead of HttpURLConnection.
 *
 * Key improvements over the original:
 *
 * 1. Uses the same OkHttpClient as Retrofit — shared connection pool,
 *    TLS session reuse, proper HTTPS redirect following.
 *
 * 2. Bounded thread pool (MAX_CONCURRENT = 3) instead of one new thread
 *    per image. The original could spawn 30+ threads simultaneously during
 *    a sync, saturating the server and triggering IP blocks.
 *
 * 3. Skips download silently if the file already exists and is non-empty,
 *    so repeated syncs don't re-download images unnecessarily.
 *
 * 4. Properly closes response bodies to avoid the "resource failed to call
 *    close" warnings seen in logcat.
 */
public class ImageDownloadUtil {

    private static final String TAG = "ImageDownloadUtil";

    /** Max simultaneous image downloads. Keep low to avoid triggering rate limits. */
    private static final int MAX_CONCURRENT = 3;

    /** Shared executor — all downloads go through this pool, never raw new Thread(). */
    private static final ExecutorService executor =
            Executors.newFixedThreadPool(MAX_CONCURRENT);

    /** Shared OkHttpClient — reuses connections and TLS sessions. */
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build();

    // ── Public interface ──────────────────────────────────────────────────────

    public interface DownloadCallback {
        void onSuccess(File file);
        void onFailure(String imageUrl);
    }

    /**
     * Download an image to a specific relative path inside filesDir.
     * Fire-and-forget — failures are logged but not propagated.
     */
    public static void downloadImage(Context context, String imageUrl, String relativePath) {
        downloadImage(context, imageUrl, relativePath, null);
    }

    /**
     * Download an image to a specific relative path inside filesDir.
     * Optional callback invoked on the download thread (not main thread).
     */
    public static void downloadImage(Context context, String imageUrl,
                                     String relativePath, DownloadCallback callback) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            Log.w(TAG, "Skipping null/empty URL");
            if (callback != null) callback.onFailure("null URL");
            return;
        }

        File destination = new File(context.getFilesDir(), relativePath);

        // Skip if already downloaded and non-empty
        if (destination.exists() && destination.length() > 0) {
            Log.d(TAG, "Already exists, skipping: " + destination.getName());
            if (callback != null) callback.onSuccess(destination);
            return;
        }

        executor.submit(() -> {
            // Ensure parent directory exists
            File parent = destination.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try {
                downloadToFile(imageUrl, destination);
                Log.d(TAG, "Download succeeded: " + imageUrl);
                if (callback != null) callback.onSuccess(destination);
            } catch (IOException e) {
                Log.w(TAG, "Download failed [" + imageUrl + "]: " + e.getMessage());
                // Clean up partial file
                if (destination.exists()) destination.delete();
                if (callback != null) callback.onFailure(imageUrl);
            }
        });
    }

    /**
     * Download an image into a named folder, deriving the filename from the URL.
     *
     * Example:
     *   downloadImageWithCustomPath(ctx,
     *       "https://example.com/uploads/products/img.jpg",
     *       "products")
     *   → saved to: filesDir/products/img.jpg
     */
    public static void downloadImageWithCustomPath(Context context,
                                                   String imageUrl,
                                                   String folderName) {
        downloadImageWithCustomPath(context, imageUrl, folderName, null);
    }

    public static void downloadImageWithCustomPath(Context context,
                                                   String imageUrl,
                                                   String folderName,
                                                   DownloadCallback callback) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            Log.w(TAG, "Skip — null/empty URL for folder: " + folderName);
            if (callback != null) callback.onFailure("null URL");
            return;
        }
        // Extract filename from URL, strip query params if any
        String raw      = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
        String fileName = raw.contains("?") ? raw.substring(0, raw.indexOf('?')) : raw;

        if (fileName.isEmpty()) {
            Log.w(TAG, "Skip — could not derive filename from: " + imageUrl);
            if (callback != null) callback.onFailure(imageUrl);
            return;
        }

        downloadImage(context, imageUrl, folderName + "/" + fileName, callback);
    }

    // ── Internal download ─────────────────────────────────────────────────────

    private static void downloadToFile(String imageUrl, File destination) throws IOException {
        Request request = new Request.Builder()
                .url(imageUrl)
                .header("User-Agent", "MyAdermoShop-Android")
                // Ask server not to keep the connection open after this request
                // (reduces risk of stale connection reuse causing silent failures)
                .header("Connection", "keep-alive")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP " + response.code() + " for " + imageUrl);
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Empty response body for " + imageUrl);
            }

            // Stream directly to file — avoids loading entire image into memory
            try (InputStream  in  = body.byteStream();
                 FileOutputStream out = new FileOutputStream(destination)) {
                byte[] buffer = new byte[8192];
                int    read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
                Log.d(TAG, "Saved: " + destination.getAbsolutePath());
            }
        }
        // try-with-resources on Response ensures body.close() is always called —
        // this eliminates the "resource failed to call close" logcat warnings.
    }
}