package com.citrus.asynch;

import com.citrus.pojo.BillGeneratorPOJO;
import com.citrus.retrofit.RetroFitClient;

import java.net.MalformedURLException;
import java.net.URL;

import retrofit.Callback;

/**
 * Created by MANGESH KADAM on 5/8/2015.
 */
public class GetJSONBill {
    String billURL;
    String amount;
    Callback<BillGeneratorPOJO> billGeneratorPOJOCallback;
    public GetJSONBill(String billURL, String amount, Callback<BillGeneratorPOJO> billGeneratorPOJOCallback){
        this.billURL = billURL;
        this.amount = amount;
        this.billGeneratorPOJOCallback = billGeneratorPOJOCallback;
    }

    public void getJSONBill() {
        URL url = null;
        try {
            url = new URL(billURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String baseURL = url.getProtocol() + "://" + url.getAuthority();
        String path = url.getPath();
        path = path.substring(1);
        RetroFitClient.getBillGeneratorClient(baseURL).getBillGeneratorResponse(path,amount,billGeneratorPOJOCallback);
    }
}
