package com.citrus.pojo;

import com.google.gson.annotations.Expose;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MANGESH KADAM on 5/8/2015.
 */
public class AmountPOJO {

    @Expose
    private String value;
    @Expose
    private String currency;

    /**
     *
     * @return
     * The value
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     * The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     * The currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @param currency
     * The currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public JSONObject getJSONObject() {
        JSONObject amountObject =  new JSONObject();
        try {
            amountObject.put("value", value);
            amountObject.put("currency", currency);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return amountObject;
    }

}
