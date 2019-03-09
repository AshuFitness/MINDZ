
package com.mindzglobal.www.mindz.Model.UserNamepojo;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class UserName {

    @SerializedName("usrnam")
    private String mUsrnam;

    public String getUsrnam() {
        return mUsrnam;
    }

    public void setUsrnam(String usrnam) {
        mUsrnam = usrnam;
    }

}
