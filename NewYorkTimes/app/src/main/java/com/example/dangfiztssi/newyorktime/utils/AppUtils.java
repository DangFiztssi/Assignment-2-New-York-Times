package com.example.dangfiztssi.newyorktime.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.example.dangfiztssi.newyorktime.BuildConfig;
import com.example.dangfiztssi.newyorktime.R;
import com.example.dangfiztssi.newyorktime.models.ApiResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DangF on 10/18/16.
 */

public class AppUtils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final Gson GSON = new Gson();

    public static Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(AppContants.STATIC_BASE_URL)
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient client(){
        return new OkHttpClient.Builder()
                .addInterceptor(apikeyInterceptor())
                .addInterceptor(responseIntercepter())
                .build();
    }

    private static Interceptor responseIntercepter(){
        return chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);

            ApiResponse apiResponse = GSON.fromJson(response.body().string(),ApiResponse.class);

            return response.newBuilder()
                    .body(ResponseBody.create(JSON,apiResponse.getResponse().toString()))
                    .build();
        };
    }

    private static Interceptor apikeyInterceptor(){
        return chain -> {
            Request request = chain.request();
            HttpUrl url = request.url()
                    .newBuilder()
                    .addQueryParameter("api_key", "227c750bb7714fc39ef1559ef1bd8329")
                    .build();

            request = request.newBuilder()
                    .url(url)
                    .build();
            return chain.proceed(request);
        };
    }

    public static final Dialog getWaitingDialog(Activity activity){
        Dialog dia = new Dialog(activity);
        LayoutInflater inflater = ((LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View v  = inflater.inflate(R.layout.waiting_dialog, null);
        dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dia.setContentView(v);
        dia.setCanceledOnTouchOutside(false);
        dia.getWindow().setDimAmount(0.0f);
        dia.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dia;
    }

    public static float convertDpToPixel( float dp, Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)/2;
    }

    public static boolean checkNetwork(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
            return true;

        return false;
    }
}
