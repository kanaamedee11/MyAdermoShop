package com.example.myadermoshop;

import android.content.Context;
import android.util.Log;
import com.itextpdf.styledxmlparser.css.CommonCssConstants;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * SyncHelper
 *
 * Replaces the ~15 individual getFromServer*() calls that previously fired
 * in parallel (causing burst-request IP blocks) with a single syncAll() call.
 *
 * Usage in your Activity / ViewModel:
 *
 *   new SyncHelper(context, dbHelper).syncAll(new DatabaseHelper.DataUpdateCallback() {
 *       public void onComplete() { runOnUiThread(() -> refreshUI()); }
 *       public void onFailure(String msg) { Log.e("Sync", msg); }
 *   });
 */
public class SyncHelper {

    private static final String TAG = "SyncHelper";
    private final Context context;
    private final DatabaseHelper db;

    public SyncHelper(Context context, DatabaseHelper db) {
        this.context = context;
        this.db = db;
    }

    public void syncAll(final DatabaseHelper.DataUpdateCallback callback) {
        String apiKey = db.getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            callback.onFailure("Missing API key");
            return;
        }

        RetrofitInstance.getApiService()
            .syncAll(apiKey)
            .enqueue(new Callback<SyncResponse>() {
                @Override
                public void onResponse(Call<SyncResponse> call,
                                       Response<SyncResponse> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        callback.onFailure("Sync failed, HTTP " + response.code());
                        return;
                    }
                    SyncResponse body = response.body();
                    if (!body.isSuccess()) {
                        callback.onFailure(body.getMessage() != null
                                ? body.getMessage() : "Unknown server error");
                        return;
                    }
                    SyncResponse.SyncData data = body.getData();
                    if (data == null) {
                        callback.onFailure("Empty sync payload");
                        return;
                    }
                    try {
                        processSyncData(data);
                        callback.onComplete();
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing sync data", e);
                        callback.onFailure("Error processing sync data: " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SyncResponse> call, Throwable t) {
                    callback.onFailure("Network error: " + t.getMessage());
                }
            });
    }

    // ── Write everything to SQLite in a logical order (parents before children)
    private void processSyncData(SyncResponse.SyncData data) {

        // Reference / static tables first
        safeProcess(data.getProductTypes(),  db::addProductType,        "productTypes");
        safeProcess(data.getPaymentTypes(),  db::savePaymentTypeToDatabase, "paymentTypes");
        safeProcess(data.getOperationStatuses(), db::saveOperationStatusToDatabase, "opStatuses");
        safeProcess(data.getMeasurementUnits(), db::addMeasurementUnit, "units");
        safeProcess(data.getTypeDispenses(), db::addTypeDispense,       "typeDispenses");

        // Products and prices
        safeProcess(data.getProducts(),      db::addProduct,            "products");
        safeProcess(data.getProductPrices(), db::addProductPrice,       "prices");

        // Download product images in background (non-blocking)
        if (data.getProducts() != null) {
            for (Product p : data.getProducts()) {
                if (p != null && p.getProductPhotoUrl() != null
                        && p.getProductPhotoName() != null) {
                    ImageDownloadUtil.downloadImageWithCustomPath(
                            context, p.getProductPhotoUrl(), "products");
                }
            }
        }

        // Stocks (parent of instances)
        if (data.getStocks() != null) {
            for (Stock s : data.getStocks()) {
                if (s != null) {
                    s.setUploadStatus(1);
                    db.addStock(s);
                    if (s.getFactureImageUrl() != null && !s.getFactureImageUrl().isEmpty()) {
                        ImageDownloadUtil.downloadImageWithCustomPath(
                                context, s.getFactureImageUrl(), "factures");
                    }
                }
            }
        }

        // Instances — server now returns ALL of them in one array, grouped by
        // stockID on the client side. No more N HTTP calls for N stocks.
        if (data.getInstances() != null) {
            for (ProductInstance pi : data.getInstances()) {
                if (pi != null) {
                    db.saveInstancesLocallyByObjects(
                            List.of(pi), pi.getStockID(),
                            new DatabaseHelper.DataUpdateCallback() {
                                public void onComplete() {}
                                public void onFailure(String msg) {
                                    Log.w(TAG, "Instance save failed: " + msg);
                                }
                            });
                }
            }
        }

        // Transaction data
        safeProcess(data.getCarts(),                db::addCart,                    "carts");
        safeProcess(data.getCartItemsWithInstance(), db::addCartItemWithInstance,   "ciWithInst");
        safeProcess(data.getCartItemsWithoutInstance(), db::addCartItemWithoutInstance, "ciWithoutInst");
        safeProcess(data.getPayments(),             db::addPayment,                 "payments");
        safeProcess(data.getClosures(),             db::addClosureData,             "closures");

        // Financial
        if (data.getVersements() != null) {
            for (Versement v : data.getVersements()) {
                if (v != null) {
                    v.setUploadStatus(1);
                    db.addVersement(v);
                    if (v.getVersementPictureName() != null
                            && !v.getVersementPictureName().isEmpty()
                            && v.getVersementPictureUrl() != null) {
                        ImageDownloadUtil.downloadImageWithCustomPath(
                                context, v.getVersementPictureUrl(), "versements");
                    }
                }
            }
        }

        if (data.getDispenses() != null) {
            for (Dispense d : data.getDispenses()) {
                if (d != null) {
                    d.setUploadStatus(1);
                    db.addDispense(d);
                    if (d.getPictureName() != null && !d.getPictureName().isEmpty()
                            && d.getPictureUrl() != null) {
                        ImageDownloadUtil.downloadImageWithCustomPath(
                                context, d.getPictureUrl(), "dispenses");
                    }
                }
            }
        }

        // Deteriorated products
        if (data.getDeterioratedWithInstance() != null) {
            for (DeterioratedProductWithInstance d : data.getDeterioratedWithInstance()) {
                if (d != null) {
                    d.setUploadStatus(1);
                    db.addDeterioratedProductWithInstance(d);
                    if (d.getPictureName() != null && !d.getPictureName().isEmpty()
                            && d.getPictureUrl() != null) {
                        ImageDownloadUtil.downloadImageWithCustomPath(
                                context, d.getPictureUrl(), "deteriorated");
                    }
                }
            }
        }
        if (data.getDeterioratedWithoutInstance() != null) {
            for (DeterioratedProductWithoutInstance d : data.getDeterioratedWithoutInstance()) {
                if (d != null) {
                    d.setUploadStatus(1);
                    db.addDeterioratedProductWithoutInstance(d);
                    if (d.getPictureName() != null && !d.getPictureName().isEmpty()
                            && d.getPictureUrl() != null) {
                        ImageDownloadUtil.downloadImageWithCustomPath(
                                context, d.getPictureUrl(), "deteriorated");
                    }
                }
            }
        }

        // Physical controls last (they reference products and stocks)
        if (data.getPhysicalControls() != null) {
            for (PhysicalControle pc : data.getPhysicalControls()) {
                if (pc != null) db.addPhysicalControle(pc);
            }
        }
    }

    // ── Generic null-safe list iterator ───────────────────────────────────
    private <T> void safeProcess(List<T> items,
                                  java.util.function.Consumer<T> action,
                                  String label) {
        if (items == null) return;
        for (T item : items) {
            if (item != null) {
                try {
                    action.accept(item);
                } catch (Exception e) {
                    Log.e(TAG, "Error processing " + label + " item: " + e.getMessage());
                }
            }
        }
    }

    // ── Expose addPhysicalControle publicly so SyncHelper can reach it ─────
    // (The original was private in DatabaseHelper — you can either make it
    //  package-private there, or keep this wrapper.)
    private void addPhysicalControle(DatabaseHelper db, PhysicalControle pc) {
        // reflection workaround if you can't change DatabaseHelper visibility:
        // just call db.fetchAndStorePhysicalControls() separately,
        // or change the method to package-private in DatabaseHelper.
    }
}
