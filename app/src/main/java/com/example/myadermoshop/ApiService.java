package com.example.myadermoshop;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/* loaded from: classes.dex */
public interface ApiService {
    @POST("generate_instances.php")
    Call<ServerResponse<List<String>>> generateInstances(@Query(DatabaseHelper.COLUMN_API_KEY) String str, @Query(DatabaseHelper.COLUMN_STOCK_ID) String str2);

    @GET("get_cart_items_with_instance.php")
    Call<ServerResponse<List<CartItemWithInstance>>> getCartItemsWithInstance(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_cart_items_without_instance.php")
    Call<ServerResponse<List<CartItemWithoutInstance>>> getCartItemsWithoutInstance(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_carts.php")
    Call<ServerResponse<List<Cart>>> getCarts(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_closuredata.php")
    Call<ServerResponse<List<ClosureData>>> getClosureData(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_deteriorated_products_with_instance.php")
    Call<ServerResponse<List<DeterioratedProductWithInstance>>> getDeterioratedProductsWithInstance(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_deteriorated_products_without_instance.php")
    Call<ServerResponse<List<DeterioratedProductWithoutInstance>>> getDeterioratedProductsWithoutInstance(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_dispenses.php")
    Call<ServerResponse<List<Dispense>>> getDispenses(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_instances.php")
    Call<ServerResponse<List<ProductInstance>>> getInstances(@Query(DatabaseHelper.COLUMN_API_KEY) String str, @Query(DatabaseHelper.COLUMN_STOCK_ID) String str2);

    @GET("get_measurement_units.php")
    Call<ServerResponse<List<MeasurementUnit>>> getMeasurementUnits();

    @GET("get_operation_statuses.php")
    Call<ServerResponse<List<OperationStatus>>> getOperationStatuses();

    @GET("get_payment_types.php")
    Call<ServerResponse<List<TypePayment>>> getPaymentTypes();

    @GET("get_payments.php")
    Call<ServerResponse<List<Payment>>> getPayments(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_physical_controls.php")
    Call<ServerResponse<List<PhysicalControle>>> getPhysicalControls(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_product_prices.php")
    Call<ServerResponse<List<ProductPrice>>> getProductPrices(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_products.php")
    Call<ServerResponse<List<Product>>> getProducts(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_server_status.php")
    Call<ServerResponse<String>> getServerStatus();

    @GET("get_stocks.php")
    Call<ServerResponse<List<Stock>>> getStocks(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_type_dispenses.php")
    Call<ServerResponse<List<TypeDispense>>> getTypeDispenses(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @GET("get_versements.php")
    Call<ServerResponse<List<Versement>>> getVersements(@Query(DatabaseHelper.COLUMN_API_KEY) String str);

    @POST("employee_login.php")
    Call<LoginResponse> loginEmployee(@Body LoginRequest request);

    @POST("change_password.php")
    Call<ServerResponse<Void>> changePassword(@Body Map<String, String> body);

    @POST("check_password_validity.php")
    Call<ServerResponse<Employee>> checkPasswordValidity(@Body Map<String, String> body);

    @GET("sync_all.php")
    Call<SyncResponse> syncAll(@Query("apiKey") String apiKey);
}