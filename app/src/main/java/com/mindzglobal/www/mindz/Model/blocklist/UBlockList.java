
package com.mindzglobal.www.mindz.Model.blocklist;

import com.google.gson.annotations.SerializedName;


public class UBlockList {

    @SerializedName("name")
    private String mName;
    @SerializedName("profilePic")
    private String mProfilePic;
    @SerializedName("userId")
    private String mUserId;
    @SerializedName("userName")
    private String mUserName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getProfilePic() {
        return mProfilePic;
    }

    public void setProfilePic(String profilePic) {
        mProfilePic = profilePic;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

}
