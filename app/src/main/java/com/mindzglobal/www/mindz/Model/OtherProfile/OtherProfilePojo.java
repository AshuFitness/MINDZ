
package com.mindzglobal.www.mindz.Model.OtherProfile;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class OtherProfilePojo {

    @SerializedName("getSingleProfile")
    private GetSingleProfile mGetSingleProfile;

    public GetSingleProfile getGetSingleProfile() {
        return mGetSingleProfile;
    }

    public void setGetSingleProfile(GetSingleProfile getSingleProfile) {
        mGetSingleProfile = getSingleProfile;
    }

}
