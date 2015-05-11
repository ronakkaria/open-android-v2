package com.citrus.retrofit;

import com.citrus.pojo.BillGeneratorPOJO;
import com.citrus.pojo.PaymentOptionsPOJO;
import com.citrus.pojo.StructResponsePOJO;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedString;

/**
 * Created by MANGESH KADAM on 5/7/2015.
 */
public interface API {

    //return url from citrus
    @Headers("Content-Type: application/json")
    @POST("/service/moto/authorize/struct/payment")
    void getPaymentResponse(@Body TypedString body, Callback<StructResponsePOJO> structResponseCallback);

    //payment options of merchant
    @FormUrlEncoded
    @POST("/service/v1/merchant/pgsetting")
    void getPaymentOptions(@Field("vanity") String vanity, Callback<PaymentOptionsPOJO> paymentOptionsCallback);


    //bill generator response
    @GET("/{path}")
    void getBillGeneratorResponse(@Path("path") String path, @Query("amount") String amount, Callback<BillGeneratorPOJO> billGeneratorPOJOCallback);


}
