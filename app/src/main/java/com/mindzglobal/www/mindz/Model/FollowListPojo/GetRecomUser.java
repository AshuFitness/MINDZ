
package com.mindzglobal.www.mindz.Model.FollowListPojo;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class GetRecomUser {

    @SerializedName("area")
    private String mArea;
    @SerializedName("name")
    private String mName;
    @SerializedName("profileCategory")
    private String mProfileCategory;
    @SerializedName("profilePic")
    private String mProfilePic;
    @SerializedName("userId")
    private String mUserId;
    @SerializedName("userName")
    private String mUserName;

    @SerializedName("followStatus")
    private String mFollowStatus;

    public String getArea() {
        return mArea;
    }

    public void setArea(String area) {
        mArea = area;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getProfileCategory() {
        return mProfileCategory;
    }

    public void setProfileCategory(String profileCategory) {
        mProfileCategory = profileCategory;
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

    public String getFollowStatus() {
        return mFollowStatus;
    }

    public void setFollowStatus(String followStatus) {
        mFollowStatus = followStatus;
    }

}
