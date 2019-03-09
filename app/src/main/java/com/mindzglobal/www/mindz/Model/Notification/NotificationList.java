
package com.mindzglobal.www.mindz.Model.Notification;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class NotificationList {

    @SerializedName("image")
    private String mImage;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("title")
    private String mTitle;

    @SerializedName("date")
    private String mDate;

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

}
