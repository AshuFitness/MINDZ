
package com.mindzglobal.www.mindz.Model.UserNamepojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class UserNameListPojo {

    @SerializedName("UserName")
    private List<com.mindzglobal.www.mindz.Model.UserNamepojo.UserName> mUserName;

    public List<com.mindzglobal.www.mindz.Model.UserNamepojo.UserName> getUserName() {
        return mUserName;
    }

    public void setUserName(List<com.mindzglobal.www.mindz.Model.UserNamepojo.UserName> UserName) {
        mUserName = UserName;
    }

}
