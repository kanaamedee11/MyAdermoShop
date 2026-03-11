package com.example.myadermoshop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* loaded from: classes.dex */
public class RetrofitInstance {
    public static final String BASE_URL = "https://adermoburundi.xyz/api/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gsonCreate = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(new OkHttpClient.Builder().connectTimeout(20L, TimeUnit.SECONDS).writeTimeout(200L, TimeUnit.SECONDS).readTimeout(200L, TimeUnit.SECONDS).build()).addConverterFactory(GsonConverterFactory.create(gsonCreate)).build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }

    public static HttpService getHttpService() {
        return getRetrofitInstance().create(HttpService.class);
    }
}