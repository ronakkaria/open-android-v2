package com.citrus.retrofit;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by MANGESH KADAM on 5/7/2015.
 */
public class RetroFitClient {
    private static API RETROFIT_CLIENT;

    private static String CITRUS_ROOT = null;
           // "https://sandboxadmin.citruspay.com";


    /*static {
        setupCitrusRetroFitClient();
    }*/

    private RetroFitClient() {}


    public static API getCitrusRetroFitClient() {
        return RETROFIT_CLIENT;
    }

    public static void initRetroFitClient(String environment) {
        if("sandbox".equalsIgnoreCase(environment))
            CITRUS_ROOT = "https://sandboxadmin.citruspay.com";
        else
            CITRUS_ROOT = "https://admin.citruspay.com";
        setupCitrusRetroFitClient();
    }

    private static void setupCitrusRetroFitClient() {
        RestAdapter builder = new RestAdapter.Builder()
                .setEndpoint(CITRUS_ROOT)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        RETROFIT_CLIENT = builder.create(API.class);
    }

    public static API getBillGeneratorClient(String baseHost){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(baseHost)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        API billGeneratorClient = restAdapter.create(API.class);
        return billGeneratorClient;
    }

}
