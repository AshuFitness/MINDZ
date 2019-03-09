
package com.mindzglobal.www.mindz.Model.login;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class UserLogin {

    @SerializedName("dob")
    private String mDob;
    @SerializedName("panCard")
    private String mPanCard;
    @SerializedName("adhaarCard")
    private String mAdhaarCard;
    @SerializedName("area")
    private String mArea;
    @SerializedName("city")
    private String mCity;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("name")
    private String mName;
    @SerializedName("number")
    private String mNumber;
    @SerializedName("profileCategory")
    private String mProfileCategory;
    @SerializedName("response")
    private String mResponse;
    @SerializedName("state")
    private String mState;
    @SerializedName("token")
    private String mToken;
    @SerializedName("gender")
    private String mGender;
    @SerializedName("profilePic")
    private String mProfilePic;
    @SerializedName("coverPic")
    private String mCoverPic;
    @SerializedName("userName")
    private String mUserName;
    @SerializedName("account_type")
    private String mAccountType;

    public String getDob() {
        return mDob;
    }

    public void setDob(String dob) {
        mDob = dob;
    }

    public String getPanCard() {
        return mPanCard;
    }

    public void setPanCard(String pancard) {
        mPanCard = pancard;
    }

    public String getAdhaardCard() {
        return mAdhaarCard;
    }

    public void setgetAdhaardCard(String dob) {
        mAdhaarCard = dob;
    }

    public String getArea() {
        return mArea;
    }

    public void setArea(String area) {
        mArea = area;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public String getProfileCategory() {
        return mProfileCategory;
    }

    public void setProfileCategory(String profileCategory) {
        mProfileCategory = profileCategory;
    }

    public String getResponse() {
        return mResponse;
    }

    public void setResponse(String response) {
        mResponse = response;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getProfilePic() {
        return mProfilePic;
    }

    public void setProfilePic(String proPic) {
        mProfilePic = proPic;
    }

    public String getCoverPic() {
        return mCoverPic;
    }

    public void setcoverPic(String coverPic) {
        mCoverPic = coverPic;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getAccountType() {
        return mAccountType;
    }

    public void setAccountType(String accType) {
        mAccountType = accType;
    }

}
