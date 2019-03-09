
package com.mindzglobal.www.mindz.Model.FollowListPojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Followpojo {

    @SerializedName("GetRecomUser")
    private List<com.mindzglobal.www.mindz.Model.FollowListPojo.GetRecomUser> mGetRecomUser;

    public List<com.mindzglobal.www.mindz.Model.FollowListPojo.GetRecomUser> getGetRecomUser() {
        return mGetRecomUser;
    }

    public void setGetRecomUser(List<com.mindzglobal.www.mindz.Model.FollowListPojo.GetRecomUser> GetRecomUser) {
        mGetRecomUser = GetRecomUser;
    }

}
