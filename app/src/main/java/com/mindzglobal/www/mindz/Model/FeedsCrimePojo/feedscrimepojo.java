
package com.mindzglobal.www.mindz.Model.FeedsCrimePojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class feedscrimepojo {

    @SerializedName("GetFeedx")
    private List<GetFeedx> mGetFeedx;

    public List<GetFeedx> getGetFeedx() {
        return mGetFeedx;
    }

    public void setGetFeedx(List<GetFeedx> GetFeedx) {
        mGetFeedx = GetFeedx;
    }

}
