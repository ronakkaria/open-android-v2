package com.citrus.pojo;

/**
 * Created by MANGESH KADAM on 5/7/2015.
 */

/*import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")*/
public class NetBankingPOJO {

   // @Expose
    private String bankName;
    //@Expose
    private String issuerCode;

    /**
     *
     * @return
     * The bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     *
     * @param bankName
     * The bankName
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     *
     * @return
     * The issuerCode
     */
    public String getIssuerCode() {
        return issuerCode;
    }

    /**
     *
     * @param issuerCode
     * The issuerCode
     */
    public void setIssuerCode(String issuerCode) {
        this.issuerCode = issuerCode;
    }

}
