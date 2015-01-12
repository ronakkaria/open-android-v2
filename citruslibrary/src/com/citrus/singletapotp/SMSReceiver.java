package com.citrus.singletapotp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import static com.citrus.singletapotp.SMSManagerConstants.OTPBankOptions;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String messageSender = null;
        String message = null;
        String javascript = null;
        String otp = null;
        OTPBankOptions bankOptions = null;
        if (bundle != null) {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                if (i == 0) {
                    //---get the sender address/phone number---
                    messageSender = msgs[i].getOriginatingAddress();
                    message = msgs[i].getMessageBody();
                    Log.d("Citrus", "OTP Sender " + messageSender + "\n Message Body : " + message);

                    if (messageSender.toLowerCase().contains(OTPBankOptions.SBI.getSMSNumber().toLowerCase())) {
                        bankOptions = OTPBankOptions.SBI;
                    } else if (messageSender.toLowerCase().contains(OTPBankOptions.ICICI.getSMSNumber().toLowerCase())) {
                        bankOptions = OTPBankOptions.ICICI;
                    } else if (messageSender.toLowerCase().contains(OTPBankOptions.HDFC.getSMSNumber().toLowerCase())) {
                        bankOptions = OTPBankOptions.HDFC;
                    } else if (messageSender.toLowerCase().contains(OTPBankOptions.AXIS.getSMSNumber().toLowerCase())) {
                        bankOptions = OTPBankOptions.AXIS;
                    } else if (messageSender.toLowerCase().contains(OTPBankOptions.CITI.getSMSNumber().toLowerCase())) {
                        bankOptions = OTPBankOptions.CITI;
                    } else if (messageSender.toLowerCase().contains(OTPBankOptions.KOTAK.getSMSNumber().toLowerCase())) {
                        bankOptions = OTPBankOptions.KOTAK;
                    }

                    Log.d("Citrus SMSReceiver ", "bankOptions " + bankOptions.name());

                    // Extract the OTP and get the respected javascript
                    otp = bankOptions.parseOTP(message);

                    Log.d("Citrus SMSReceiver ", "otp is " + otp);
                    if (!TextUtils.isEmpty(otp)) {
                        javascript = bankOptions.getJavascript(otp);

                        // Since that we have got the OTP send event
                        Intent in = new Intent("action_otp_received");
                        in.putExtra("javascript", javascript);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(in);
                    }
                    break;
                }

            }
        }
    }
}
