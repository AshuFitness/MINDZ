
package com.mindzglobal.www.mindz.Model.login;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class LoginPojo {

    @SerializedName("UserLogin")
    private com.mindzglobal.www.mindz.Model.login.UserLogin mUserLogin;

    public com.mindzglobal.www.mindz.Model.login.UserLogin getUserLogin() {
        return mUserLogin;
    }

    public void setUserLogin(com.mindzglobal.www.mindz.Model.login.UserLogin UserLogin) {
        mUserLogin = UserLogin;
    }

}
