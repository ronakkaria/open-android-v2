package com.citruspay.sdkui;

import org.json.JSONObject;

/**
 * Created by salil on 16/2/15.
 */
public class CitrusUser {

    private String firstName = null;
    private String lastName = null;
    private String emailId = null;
    private String mobileNo = null;
    private Address address = null;

    public CitrusUser(String emailId, String mobileNo, String firstName, String lastName, Address address) {
        this.emailId = emailId;
        this.mobileNo = mobileNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public static CitrusUser fromJSONObject(JSONObject response) {
        CitrusUser user = null;

        if (response != null) {
            String email = response.optString("email");
            String mobileNo = response.optString("mobileNo");
            String firstName = response.optString("firstName");
            String lastName = response.optString("lastName");
            Address address = Address.fromJSONObject(response);

            user = new CitrusUser(email, mobileNo, firstName, lastName, address);
        }

        return user;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "CitrusUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", address=" + address +
                '}';
    }

    public static class Address {

        private String street1 = null;
        private String street2 = null;
        private String city = null;
        private String state = null;
        private String country = null;
        private String zip = null;

        public Address(String street1, String street2, String city, String state, String country, String zip) {
            this.street1 = street1;
            this.street2 = street2;
            this.city = city;
            this.state = state;
            this.country = country;
            this.zip = zip;
        }

        public static Address fromJSONObject(JSONObject response) {
            Address address = null;

            if (response != null) {
                String addressCountry = response.optString("addressCountry");
                String addressState = response.optString("addressState");
                String addressCity = response.optString("addressCity");
                String addressStreet1 = response.optString("addressStreet1");
                String addressStreet2 = response.optString("addressStreet2");
                String addressZip = response.optString("addressZip");

                address = new Address(addressStreet1, addressStreet2, addressCity, addressState, addressCountry, addressZip);
            }

            return address;
        }

        public String getStreet1() {
            return street1;
        }

        public String getStreet2() {
            return street2;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getCountry() {
            return country;
        }

        public String getZip() {
            return zip;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "street1='" + street1 + '\'' +
                    ", street2='" + street2 + '\'' +
                    ", city='" + city + '\'' +
                    ", state='" + state + '\'' +
                    ", country='" + country + '\'' +
                    ", zip='" + zip + '\'' +
                    '}';
        }
    }

}