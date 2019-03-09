
package com.mindzglobal.www.mindz.Model.AllFollowListPojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Allfollowlistpojo {

    @SerializedName("FollowList")
    private List<com.mindzglobal.www.mindz.Model.AllFollowListPojo.FollowList> mFollowList;

    public List<com.mindzglobal.www.mindz.Model.AllFollowListPojo.FollowList> getFollowList() {
        return mFollowList;
    }

    public void setFollowList(List<com.mindzglobal.www.mindz.Model.AllFollowListPojo.FollowList> FollowList) {
        mFollowList = FollowList;
    }

}
