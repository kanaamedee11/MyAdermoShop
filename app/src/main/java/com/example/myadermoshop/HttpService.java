package com.example.myadermoshop;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/* loaded from: classes.dex */
public interface HttpService {
    @POST("add_physical_control.php")
    Call<ServerResponse<Void>> addPhysicalControl(@Query(DatabaseHelper.COLUMN_API_KEY) String str, @Body RequestBody requestBody);

    @POST("upload_file/RestApi/multi_upload.php")
    @Multipart
    Call<FileModel> callMultipleUploadApi(@Part List<MultipartBody.Part> list);

    @POST("add_stock.php")
    @Multipart
    Call<FileModel> callUploadApi(@Part MultipartBody.Part part, @Part(DatabaseHelper.COLUMN_STOCK_ID) RequestBody requestBody, @Part(DatabaseHelper.COLUMN_STOCK_QUANTITY) RequestBody requestBody2, @Part(DatabaseHelper.COLUMN_TOTAL_AMOUNT_USED) RequestBody requestBody3, @Part("productID") RequestBody requestBody4, @Part(DatabaseHelper.COLUMN_STOCK_MAN_DATE) RequestBody requestBody5, @Part(DatabaseHelper.COLUMN_STOCK_EXP_DATE) RequestBody requestBody6, @Part(DatabaseHelper.COLUMN_SUPPLIER_NAME) RequestBody requestBody7, @Part(DatabaseHelper.COLUMN_SUPPLIER_CONTACT) RequestBody requestBody8, @Part(DatabaseHelper.COLUMN_FACTURE_NUMBER) RequestBody requestBody9, @Part(DatabaseHelper.COLUMN_PAYMENT_TYPE_ID) RequestBody requestBody10, @Part(DatabaseHelper.COLUMN_API_KEY) RequestBody requestBody11, @Part(DatabaseHelper.COLUMN_STATUS_ID) RequestBody requestBody12);

    @POST("addDeterioratedProductWithInstance.php")
    @Multipart
    Call<FileModel> callUploadDeterioratedProductWithInstanceApi(@Part MultipartBody.Part part, @Part(DatabaseHelper.COLUMN_DETERIORATED_PRODUCT_WITH_INSTANCE_ID) RequestBody requestBody, @Part(DatabaseHelper.COLUMN_DETERIORATION_DATE) RequestBody requestBody2, @Part(DatabaseHelper.COLUMN_INSTANCE_ID) RequestBody requestBody3, @Part(DatabaseHelper.COLUMN_QUANTITY) RequestBody requestBody4, @Part(DatabaseHelper.COLUMN_REASON) RequestBody requestBody5, @Part(DatabaseHelper.COLUMN_DETECTED_BY_EMPLOYEE_ID) RequestBody requestBody6, @Part(DatabaseHelper.COLUMN_SUBMISSION_DATE) RequestBody requestBody7, @Part(DatabaseHelper.COLUMN_API_KEY) RequestBody requestBody8);

    @POST("addDeterioratedProductWithoutInstance.php")
    @Multipart
    Call<FileModel> callUploadDeterioratedProductWithoutInstanceApi(@Part MultipartBody.Part part, @Part(DatabaseHelper.COLUMN_DETERIORATED_PRODUCT_WITHOUT_INSTANCE_ID) RequestBody requestBody, @Part(DatabaseHelper.COLUMN_DETERIORATION_DATE) RequestBody requestBody2, @Part("productID") RequestBody requestBody3, @Part(DatabaseHelper.COLUMN_QUANTITY) RequestBody requestBody4, @Part(DatabaseHelper.COLUMN_REASON) RequestBody requestBody5, @Part(DatabaseHelper.COLUMN_DETECTED_BY_EMPLOYEE_ID) RequestBody requestBody6, @Part(DatabaseHelper.COLUMN_SUBMISSION_DATE) RequestBody requestBody7, @Part(DatabaseHelper.COLUMN_API_KEY) RequestBody requestBody8);

    @POST("add_dispense.php")
    @Multipart
    Call<FileModel> callUploadDispenseApi(@Part MultipartBody.Part part, @Part(DatabaseHelper.COLUMN_DISPENSE_ID) RequestBody requestBody, @Part(DatabaseHelper.COLUMN_DISPENSE_DATE) RequestBody requestBody2, @Part("typeDispenseID") RequestBody requestBody3, @Part(DatabaseHelper.COLUMN_AMOUNT) RequestBody requestBody4, @Part("employeeID") RequestBody requestBody5, @Part(DatabaseHelper.COLUMN_API_KEY) RequestBody requestBody6, @Part(DatabaseHelper.COLUMN_STATUS_ID) RequestBody requestBody7, @Part(DatabaseHelper.COLUMN_PAYMENT_TYPE_ID) RequestBody requestBody8);

    @POST("add_versement.php")
    @Multipart
    Call<FileModel> callUploadVersementApi(@Part MultipartBody.Part part, @Part("versementID") RequestBody requestBody, @Part("versementDateTime") RequestBody requestBody2, @Part("employeeID") RequestBody requestBody3, @Part("adminID") RequestBody requestBody4, @Part(DatabaseHelper.COLUMN_STATUS_ID) RequestBody requestBody5, @Part("expectedAmount") RequestBody requestBody6, @Part("versedAmount") RequestBody requestBody7, @Part(DatabaseHelper.COLUMN_PAYMENT_TYPE_ID) RequestBody requestBody8, @Part(DatabaseHelper.COLUMN_API_KEY) RequestBody requestBody9);

    @POST("upload_closure_data.php")
    Call<ServerResponse<Void>> uploadClosureData(@Query(DatabaseHelper.COLUMN_API_KEY) String str, @Body ClosureData closureData);

    @POST("upload_daily_carts.php")
    Call<ServerResponse<Void>> uploadDailyCarts(@Query(DatabaseHelper.COLUMN_API_KEY) String str, @Body List<Cart> list);

    @POST("upload_daily_payments.php")
    Call<ServerResponse<Void>> uploadDailyPayments(@Query(DatabaseHelper.COLUMN_API_KEY) String str, @Body List<Payment> list);
}