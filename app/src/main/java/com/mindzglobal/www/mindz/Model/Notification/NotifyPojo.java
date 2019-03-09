
package com.mindzglobal.www.mindz.Model.Notification;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class NotifyPojo {

    @SerializedName("NotificationList")
    private List<NotificationList> mNotificationList;

    public List<NotificationList> getNotificationList() {
        return mNotificationList;
    }

    public void setNotificationList(List<NotificationList> notificationList) {
        mNotificationList = notificationList;
    }

}
