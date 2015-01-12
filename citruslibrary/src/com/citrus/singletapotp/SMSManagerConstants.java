package com.citrus.singletapotp;

import android.text.TextUtils;
import android.util.Log;

public interface SMSManagerConstants {

   enum OTPBankOptions {
        SBI {
            @Override
            public String getSMSNumber() {
                return "SBI";
            }

            @Override
            public String parseOTP(String message) {
                return "";
            }

            @Override
            public String getJavascript(String otp) {
                return "";
            }
        },
        ICICI {
            @Override
            public String getSMSNumber() {
                return "ICICIB";
            }

            @Override
            public String parseOTP(String message) {
                String otp = null;
                int start = -1;
                if (!TextUtils.isEmpty(message) &&(start = message.indexOf("is")) != -1) {
                    otp = message.substring(start + 3, start + 9);

                    Log.d("Citrus ICICI", "OTP is " + otp + " : start = " + start);
                }

                return otp;
            }

            @Override
            public String getJavascript(String otp) {
                return "javascript:" + "document.passwdForm.otpPassword.value=" + otp + "; submitPassword();";
            }
        },
        AXIS {
            @Override
            public String getSMSNumber() {
                return "SBI";
            }

            @Override
            public String parseOTP(String message) {
                return "";
            }

            @Override
            public String getJavascript(String otp) {
                return "";
            }
        },
        HDFC {
            @Override
            public String getSMSNumber() {
                return "HDFCBK";
            }

            @Override
            public String parseOTP(String message) {
                String otp = null;
                int start = -1;
                if (!TextUtils.isEmpty(message) &&(start = message.indexOf("is")) != -1) {
                    otp = message.substring(start + 3, start + 9);

                    Log.d("Citrus HDFC", "OTP is " + otp + " : start = " + start);
                }

                return otp;
            }

            @Override
            public String getJavascript(String otp) {
                return "javascript:document.frmDynamicAuth.txtOtpPassword.value=" + otp + "; ValidateForm();";
            }
        }, CITI {
            @Override
            public String getSMSNumber() {
                return "Citibk";
            }

            @Override
            public String parseOTP(String message) {
                String otp = null;
                int start = -1;
                if (!TextUtils.isEmpty(message) && (start = message.indexOf("is")) != -1) {
                    otp = message.substring(0, start - 1);

                    Log.d("Citrus CITI", "OTP is " + otp);
                }

                return otp;
            }

            @Override
            public String getJavascript(String otp) {
                return "javascript:document.optInForm.otp.value="+otp+ "; document.optInForm.submit();";
            }
        }, KOTAK {
            @Override
            public String getSMSNumber() {
                return "KOTAKB";
            }

            @Override
            public String parseOTP(String message) {
                String otp = null;
                int start = -1;
                if (!TextUtils.isEmpty(message) && (start = message.indexOf("is")) != -1) {
                    otp = message.substring(0, start - 1);

                    Log.d("Citrus KOTAK", "OTP is " + otp);
                }

                return otp;
            }

            @Override
            public String getJavascript(String otp) {
                return "javascript:document.frmPayerAuth.txtOtp.value=" + otp + "; document.frmPayerAuth.submit();";
            }
        };

        /**
         * Returns the number/sender name from which the SMS is received for OTP.
         *
         * @return
         */
        public abstract String getSMSNumber();

        /**
         * Parse the message and return OTP
         *
         * @param message
         * @return parsed OTP
         */
        public abstract String parseOTP(String message);

        /**
         * Get the javascript with respect to the bank.
         * This javascript will be loaded inside the webview to complete the transaction.
         *
         * @param otp
         * @return javascript w.r.t. bank
         */
        public abstract String getJavascript(String otp);
    }
}
