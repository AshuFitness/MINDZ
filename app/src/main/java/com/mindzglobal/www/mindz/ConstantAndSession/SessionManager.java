package com.mindzglobal.www.mindz.ConstantAndSession;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    public void setPreferences(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences("Androidwarriors", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();

    }

    public String getPreferences(Context context, String key) {

        SharedPreferences prefs = context.getSharedPreferences("Androidwarriors", Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }

//    public String getConsumerId(Context context) {
//        return getPreferences(context, Constants.EmpID);
//    }

//    public String getAccessKey(Context context) {
//        return getPreferences(context, Constants.AccessKey);
//    }
//
//    public String getFinPercentage(Context context) {
//        return getPreferences(context, Constants.FinanPercentage);
//    }


}

