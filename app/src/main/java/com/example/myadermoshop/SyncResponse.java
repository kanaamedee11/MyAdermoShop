package com.example.myadermoshop;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * SyncResponse
 *
 * Maps to the single JSON object returned by sync_all.php.
 * Replace all the individual getFromServer*() calls in DatabaseHelper
 * with one call to ApiService.syncAll(), then pass the SyncData to
 * DatabaseHelper.processSyncResponse().
 */
public class SyncResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("lastControlDate")
    private String lastControlDate;

    @SerializedName("data")
    private SyncData data;

    public boolean isSuccess()       { return success; }
    public String getMessage()       { return message; }
    public String getLastControlDate() { return lastControlDate; }
    public SyncData getData()        { return data; }

    public static class SyncData {

        @SerializedName("products")
        private List<Product> products;

        @SerializedName("productTypes")
        private List<ProductType> productTypes;

        @SerializedName("productPrices")
        private List<ProductPrice> productPrices;

        @SerializedName("paymentTypes")
        private List<TypePayment> paymentTypes;

        @SerializedName("operationStatuses")
        private List<OperationStatus> operationStatuses;

        @SerializedName("measurementUnits")
        private List<MeasurementUnit> measurementUnits;

        @SerializedName("typeDispenses")
        private List<TypeDispense> typeDispenses;

        @SerializedName("stocks")
        private List<Stock> stocks;

        @SerializedName("instances")
        private List<ProductInstance> instances;

        @SerializedName("carts")
        private List<Cart> carts;

        @SerializedName("cartItemsWithInstance")
        private List<CartItemWithInstance> cartItemsWithInstance;

        @SerializedName("cartItemsWithoutInstance")
        private List<CartItemWithoutInstance> cartItemsWithoutInstance;

        @SerializedName("payments")
        private List<Payment> payments;

        @SerializedName("closures")
        private List<ClosureData> closures;

        @SerializedName("versements")
        private List<Versement> versements;

        @SerializedName("dispenses")
        private List<Dispense> dispenses;

        @SerializedName("deterioratedWithInstance")
        private List<DeterioratedProductWithInstance> deterioratedWithInstance;

        @SerializedName("deterioratedWithoutInstance")
        private List<DeterioratedProductWithoutInstance> deterioratedWithoutInstance;

        @SerializedName("physicalControls")
        private List<PhysicalControle> physicalControls;

        public List<Product> getProducts()                        { return products; }
        public List<ProductType> getProductTypes()                { return productTypes; }
        public List<ProductPrice> getProductPrices()              { return productPrices; }
        public List<TypePayment> getPaymentTypes()                { return paymentTypes; }
        public List<OperationStatus> getOperationStatuses()       { return operationStatuses; }
        public List<MeasurementUnit> getMeasurementUnits()        { return measurementUnits; }
        public List<TypeDispense> getTypeDispenses()              { return typeDispenses; }
        public List<Stock> getStocks()                            { return stocks; }
        public List<ProductInstance> getInstances()               { return instances; }
        public List<Cart> getCarts()                              { return carts; }
        public List<CartItemWithInstance> getCartItemsWithInstance()     { return cartItemsWithInstance; }
        public List<CartItemWithoutInstance> getCartItemsWithoutInstance(){ return cartItemsWithoutInstance; }
        public List<Payment> getPayments()                        { return payments; }
        public List<ClosureData> getClosures()                    { return closures; }
        public List<Versement> getVersements()                    { return versements; }
        public List<Dispense> getDispenses()                      { return dispenses; }
        public List<DeterioratedProductWithInstance> getDeterioratedWithInstance()     { return deterioratedWithInstance; }
        public List<DeterioratedProductWithoutInstance> getDeterioratedWithoutInstance(){ return deterioratedWithoutInstance; }
        public List<PhysicalControle> getPhysicalControls()       { return physicalControls; }
    }
}
