package com.citrus.retrofit;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by MANGESH KADAM on 5/7/2015.
 */
public class RetroFitClient {
    private static API RETROFIT_CLIENT;

    private static String CITRUS_ROOT =
            "https://sandboxadmin.citruspay.com";


    static {
        setupCitrusRestClient();
    }

    private RetroFitClient() {}


    public static API getCitrusRestClient() {
        return RETROFIT_CLIENT;
    }


    private static void setupCitrusRestClient() {
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
