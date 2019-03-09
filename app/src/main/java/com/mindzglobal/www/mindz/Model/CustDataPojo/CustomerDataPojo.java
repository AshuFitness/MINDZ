
package com.mindzglobal.www.mindz.Model.CustDataPojo;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CustomerDataPojo {

    @SerializedName("UserRegister")
    private com.mindzglobal.www.mindz.Model.CustDataPojo.UserRegister mUserRegister;

    public com.mindzglobal.www.mindz.Model.CustDataPojo.UserRegister getUserRegister() {
        return mUserRegister;
    }

    public void setUserRegister(com.mindzglobal.www.mindz.Model.CustDataPojo.UserRegister UserRegister) {
        mUserRegister = UserRegister;
    }

}
