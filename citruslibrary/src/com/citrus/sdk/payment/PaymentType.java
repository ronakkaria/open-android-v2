/*
   Copyright 2014 Citrus Payment Solutions Pvt. Ltd.
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
     http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.citrus.sdk.payment;

import android.os.Parcel;
import android.os.Parcelable;

import com.citrus.sdk.CitrusUser;
import com.citrus.sdk.classes.Amount;

/**
 * Created by salil on 24/4/15.
 */
public abstract class PaymentType implements Parcelable {

    protected Amount amount;
    protected String url;

    public PaymentType() {
    }

    /**
     *
     * @param amount
     * @param url
     */
    public PaymentType(Amount amount, String url)  {
        this.amount = amount;
        this.url = url;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getUrl() {
        return url;
    }

    public static class LoadMoney extends PaymentType implements Parcelable {

        public LoadMoney() {
        }

        /**
         *
         * @param amount
         * @param returnUrl - For response of the LoadMoney transaction
         * @throws IllegalArgumentException - if Amount or returnUrl is null.
         */
        public LoadMoney(Amount amount, String returnUrl) throws IllegalArgumentException {
            super(amount, returnUrl);

            if (amount == null) {
                throw new IllegalArgumentException("Amount should be not null.");
            } else if (url == null) {
                throw new IllegalArgumentException("returnUrl should be not null.");
            }
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.amount, flags);
            dest.writeString(this.url);
        }

        private LoadMoney(Parcel in) {
            this.amount = in.readParcelable(Amount.class.getClassLoader());
            this.url = in.readString();
        }

        public static final Creator<LoadMoney> CREATOR = new Creator<LoadMoney>() {
            public LoadMoney createFromParcel(Parcel source) {
                return new LoadMoney(source);
            }

            public LoadMoney[] newArray(int size) {
                return new LoadMoney[size];
            }
        };
    }

    public static class CitrusCash extends PaymentType implements Parcelable {

        public CitrusCash() {
        }

        /**
         *
         * @param amount - amount to be Payed
         * @param billUrl - Url of the billGenerator
         * @throws IllegalArgumentException - If amount or billUrl is null.
         */
        public CitrusCash(Amount amount, String billUrl) throws IllegalArgumentException {
            super(amount, billUrl);

            if (amount == null) {
                throw new IllegalArgumentException("Amount should be not null.");
            } else if (url == null) {
                throw new IllegalArgumentException("billUrl should be not null.");
            }
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.amount, flags);
            dest.writeString(this.url);
        }

        private CitrusCash(Parcel in) {
            this.amount = in.readParcelable(Amount.class.getClassLoader());
            this.url = in.readString();
        }

        public static final Creator<CitrusCash> CREATOR = new Creator<CitrusCash>() {
            public CitrusCash createFromParcel(Parcel source) {
                return new CitrusCash(source);
            }

            public CitrusCash[] newArray(int size) {
                return new CitrusCash[size];
            }
        };
    }

    public static class PGPayment extends PaymentType implements Parcelable {

        public PGPayment() {
        }

        /**
         *
         * @param amount - Amount to be payed
         * @param billUrl - url of the billGenerator
         * @throws IllegalArgumentException - if amount or billUrl is null.
         */
        public PGPayment(Amount amount, String billUrl) throws IllegalArgumentException {
            super(amount, billUrl);

            if (amount == null) {
                throw new IllegalArgumentException("Amount should be not null.");
            } else if (url == null) {
                throw new IllegalArgumentException("Url should be not null.");
            }
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.amount, flags);
            dest.writeString(this.url);
        }

        private PGPayment(Parcel in) {
            this.amount = in.readParcelable(Amount.class.getClassLoader());
            this.url = in.readString();
        }

        public static final Creator<PGPayment> CREATOR = new Creator<PGPayment>() {
            public PGPayment createFromParcel(Parcel source) {
                return new PGPayment(source);
            }

            public PGPayment[] newArray(int size) {
                return new PGPayment[size];
            }
        };
    }

    public static class SendMoney extends PaymentType implements Parcelable {

        private CitrusUser user = null;

        public SendMoney() {
        }

        /**
         *
         * @param amount - Amount to be sent
         * @param user - User details
         * @throws IllegalArgumentException - If amount or user is null.
         */
        public SendMoney(Amount amount, CitrusUser user) throws IllegalArgumentException {
            super(amount, null);
            this.user = user;

            if (amount == null) {
                throw new IllegalArgumentException("Amount should be not null.");
            } else if (user == null) {
                throw new IllegalArgumentException("Url should be not null.");
            }
        }

        public CitrusUser getUser() {
            return user;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.user, 0);
            dest.writeParcelable(this.amount, 0);
            dest.writeString(this.url);
        }

        private SendMoney(Parcel in) {
            this.user = in.readParcelable(CitrusUser.class.getClassLoader());
            this.amount = in.readParcelable(Amount.class.getClassLoader());
            this.url = in.readString();
        }

        public static final Creator<SendMoney> CREATOR = new Creator<SendMoney>() {
            public SendMoney createFromParcel(Parcel source) {
                return new SendMoney(source);
            }

            public SendMoney[] newArray(int size) {
                return new SendMoney[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.amount, flags);
        dest.writeString(this.url);
    }

    private PaymentType(Parcel in) {
        this.amount = in.readParcelable(Amount.class.getClassLoader());
        this.url = in.readString();
    }

}
