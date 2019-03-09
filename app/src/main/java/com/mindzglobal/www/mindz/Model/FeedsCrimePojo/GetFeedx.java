
package com.mindzglobal.www.mindz.Model.FeedsCrimePojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class GetFeedx {

    @SerializedName("category")
    private String mCategory;
    @SerializedName("image")
    private List<Image> mImage;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("pdate")
    private String mPdate;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("tagFriends")
    private String mTagFriends;
    @SerializedName("name")
    private String mName;
    @SerializedName("usrNam")
    private String mUsrNam;
    @SerializedName("proPic")
    private String mProPic;
    @SerializedName("postId")
    private String mPostId;


    @SerializedName("viewCount")
    private int mViewCount;

    @SerializedName("LikeCount")
    private int mLikeCount;

    @SerializedName("CommentCount")
    private int mCommentCount;

    @SerializedName("myLike")
    private int mMyLike;





    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public List<Image> getImage() {
        return mImage;
    }

    public void setImage(List<Image> image) {
        mImage = image;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getPdate() {
        return mPdate;
    }

    public void setPdate(String pdate) {
        mPdate = pdate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTagFriends() {
        return mTagFriends;
    }

    public void setTagFriends(String tagFriends) {
        mTagFriends = tagFriends;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUsrNam() {
        return mUsrNam;
    }

    public void setUsrNam(String usrNam) {
        mUsrNam = usrNam;
    }

    public String getProPic() {
        return mProPic;
    }

    public void setProPic(String proPic) {
        mProPic = proPic;
    }




    public String getPostId() {
        return mPostId;
    }

    public void setPostId(String postId) {
        mPostId = postId;
    }



    public int getViewCount() {
        return mViewCount;
    }

    public void setViewCount(int viewCount) {
        mViewCount = viewCount;
    }



    public int getLikeCount() {
        return mLikeCount;
    }

    public void setLikeCount(int LikeCount) {
        mLikeCount = LikeCount;
    }


    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int CommentCount) {
        mCommentCount = CommentCount;
    }


    public int getMyLike() {
        return mMyLike;
    }

    public void setMyLike(int MyLike) {
        mMyLike = MyLike;
    }


}
