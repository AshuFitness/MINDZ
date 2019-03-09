package com.mindzglobal.www.mindz.Model;

import com.mindzglobal.www.mindz.Configuration.Config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author ashu
 */
public class RetrofitClient {

    private static Retrofit retrofit;


    private static MyRetrofitAPI myRetrofitAPI;

    public static volatile  String token ;


    public RetrofitClient(){

    }

    // constructor
    public RetrofitClient(String token){
        this.token = token;

    }

    public static MyRetrofitAPI getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .client(check())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            myRetrofitAPI = retrofit.create(MyRetrofitAPI.class);
        }
        return myRetrofitAPI;
    }

    public static MyRetrofitAPI getWithClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .client(getRequestHeader())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            myRetrofitAPI = retrofit.create(MyRetrofitAPI.class);
        }

        return myRetrofitAPI;

    }

    private static OkHttpClient getRequestHeader() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    public static OkHttpClient check(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept","application/json")
                        .addHeader("Authorization", "Bearer "+token);
                // <-- this is the important line

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
//    OkHttpClient client = new OkHttpClient.Builder()
//            .readTimeout(30, TimeUnit.SECONDS)
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .build();
        return client;
    }

}

