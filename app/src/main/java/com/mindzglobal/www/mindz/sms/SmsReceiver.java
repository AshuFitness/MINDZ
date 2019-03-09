package com.mindzglobal.www.mindz.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.

            String messageBody = smsMessage.getMessageBody();


//            if (sender.contains("JMINDZ") && messageBody.contains("JobMindz") && messageBody.contains("registration")) {
//
//                Pattern regEx = Pattern.compile("(?:is)+[\\s]*[0-9+[\\\\,]*+[0-9]*]+[\\\\.]*[0-9]+");
//
//                // Find instance of pattern matches
//                Matcher m = regEx.matcher(messageBody);
//
//                if(m.find()){
//                    String otpValue = (m.group(0).replaceAll("is",""));
//                    String k = otpValue.toString().replace(" ", "");
////                    Log.e("OTP",k);
//                    mListener.messageReceived(k);
//                }
//            }
//
//
//           else
               if (sender.contains("HOMIEZ") && messageBody.contains("OTP") && messageBody.contains("account")) {

                Pattern regEx = Pattern.compile("(?:is)+[\\s]*[0-9+[\\\\,]*+[0-9]*]+[\\\\.]*[0-9]+");

                // Find instance of pattern matches
                Matcher m = regEx.matcher(messageBody);

                if(m.find()){
                    String otpValue = (m.group(0).replaceAll("is",""));
                    String k = otpValue.toString().replace(" ", "");
//                    Log.e("OTP",k);
                    mListener.differentMob(k);
                }
            }

        }
    }
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
