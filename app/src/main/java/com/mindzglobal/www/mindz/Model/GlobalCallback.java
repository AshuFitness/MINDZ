package com.mindzglobal.www.mindz.Model;

//import android.support.design.widget.Snackbar;

import android.util.Log;
import android.view.View;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;


/**
 * This class will handle errors of whole project
 *
 * @author anandsingh
 */

public abstract class GlobalCallback<T> implements Callback<T> {
    private final View view;

    public GlobalCallback(View view) {
        this.view = view;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (view == null)
            return;
        String message = "Sorry! Unable to process request!";
        if (t instanceof HttpException) {
            switch (((HttpException) t).code()) {
                //400 Bad Request
                case 400:
                    message = "Server is unable to process request. Please try again later.";
                    break;
                //401 Unauthorized, 403 Forbidden, 404 Not Found
                case 401:
                case 403:
                case 404:
                    message = "Server is not compatible to client. Please send a mail to support@jobmindz.com";
                    break;
                //408 Request Time-out
                case 408:
                    message = "Request time out, Please try again or check your internet connection.";
                    break;
                default:
                //    message = "Sorry! Unable to process request, Please try again later";
                    break;
            }
        } else if (t instanceof SocketTimeoutException) {
            message = "Server is not reachable. Either server or your internet connection is down";
        } else if (t instanceof UnknownHostException) {
            message = "Server is not compatible to client. Please send a mail to support@jobmindz.com";
        }
//        Snackbar snackbar = Snackbar
//                .make(view, message, Snackbar.LENGTH_LONG);
//        View snackBarView = snackbar.getView();
//        TextView tv = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
//        tv.setTextColor(Color.WHITE);
//        snackbar.show();

        Log.e("onFailure: ", t.getMessage(),t);

    }
}
