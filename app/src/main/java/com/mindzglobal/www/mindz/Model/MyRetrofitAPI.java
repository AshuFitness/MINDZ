package com.mindzglobal.www.mindz.Model;

import com.mindzglobal.www.mindz.Model.AllFollowListPojo.Allfollowlistpojo;
import com.mindzglobal.www.mindz.Model.CustDataPojo.CustomerDataPojo;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.Image;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.feedscrimepojo;
import com.mindzglobal.www.mindz.Model.FollowListPojo.Followpojo;
import com.mindzglobal.www.mindz.Model.Notification.NotifyPojo;
import com.mindzglobal.www.mindz.Model.OtherProfile.OtherProfilePojo;
import com.mindzglobal.www.mindz.Model.UserNamepojo.UserNameListPojo;
import com.mindzglobal.www.mindz.Model.blocklist.BlocklistPojo;
import com.mindzglobal.www.mindz.Model.login.LoginPojo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;


public interface MyRetrofitAPI {
//    @POST("logins")
//    @FormUrlEncoded
//    Call<String> details(@Field("Mobile") String finDes, @Field("Name") String own,
//                         @Field("Email") String custId, @Field("Login") String check);

//    @GET
//    Call<RecentTransactionPojo> recentTransaction(@Url String url);


    @POST("otp/")
    @FormUrlEncoded
    Call<String> getOTP(@Field("gender") String gen, @Field("userName") String uname, @Field("name") String name, @Field("email") String email,
                        @Field("number") String num);

    @POST("otpvalidation/")
    @FormUrlEncoded
    Call<String> vaildOTP(@Field("number") String mob, @Field("otpcode") String otpNum);

    @POST("resend/")
    @FormUrlEncoded
    Call<String> resndOtp(@Field("name") String mob, @Field("number") String otpNum,@Field("email") String email);

    @POST("register/")
    @FormUrlEncoded
    Call<CustomerDataPojo> custData(@Field("ftoken") String ftkn,@Field("account_type") String acc_type,
                                    @Field("gender") String gen,@Field("userName") String uname,
                                    @Field("name") String name, @Field("email") String email,
                                    @Field("number") String mob, @Field("password") String pass,
                                    @Field("profileCategory") String proCat, @Field("country") String country,
                                    @Field("state") String state, @Field("city") String city,
                                    @Field("area") String area);

    @POST("login/")
    @FormUrlEncoded
    Call<LoginPojo> login(@Field("email") String email, @Field("password") String pass);


    @GET
    Call<String> fpassword(@Url String url);



    @POST("changepass/")
    @FormUrlEncoded
    Call<String> resetpass(@Field("oldpass") String email, @Field("newpass") String pass,
                           @Header("Accept") String accept, @Header("Authorization") String AuthToken);


    @Multipart
    @POST("profilepic/")
    Call<String> PicPost( @Part MultipartBody.Part file);

//    postProfilepic


    @POST("profilepic/")
    @FormUrlEncoded
    Call<String> postProfilepic(@Field("profile_pic") String image,
                                @Header("Accept") String accept,
                                @Header("Authorization") String AuthToken);


    @POST("coverpic/")
    @FormUrlEncoded
    Call<String> postCoverpic(@Field("cover_pic") String image,
                                @Header("Accept") String accept,
                                @Header("Authorization") String AuthToken);


    @POST("updateprofile/")
    @FormUrlEncoded
    Call<String> Editprofile(@Field("account_type") String acctype,@Field("gender") String gender,@Field("name") String name,
                                     @Field("profileCategory") String proCat, @Field("country") String country,
                                     @Field("state") String state, @Field("city") String city,
                                     @Field("area") String area, @Field("dob") String date,
                                     @Field("panCard") String pancard,@Field("adhaarCard") String aCard ,
                                     @Header("Accept") String accept,@Header("Authorization") String AuthToken);


    @POST("numberchangeotp/")
    @FormUrlEncoded
    Call<String> mobChange(@Field("number") String number,@Field("email") String email,@Field("name") String name);

    @POST("numberchange/")
    @FormUrlEncoded
    Call<String>   postmobChange(@Field("number") String number,
                               @Header("Accept") String accept,
                               @Header("Authorization") String AuthToken);


    @POST("emailchange/")
    @FormUrlEncoded
    Call<String> changeEID(@Field("email") String email,
                           @Header("Accept") String accept,
                           @Header("Authorization") String AuthToken);


    //pan-adhar-dob
    @POST("updateprofile/")
    @FormUrlEncoded
    Call<String> dobpanadhar(@Field("dob") String dob,
                             @Field("panCard") String pc,
                             @Field("adhaarCard") String ac,
                             @Header("Accept") String accept,
                             @Header("Authorization") String AuthToken);


    @GET
    Call<LoginPojo> getuserData(@Url String url, @Header("Accept") String accept,
                                @Header("Authorization") String AuthToken);

    @POST("poststatus/")
    Call<String> uploadMultiFile(@Body RequestBody file, @Header("Accept") String accept,
                                 @Header("Authorization") String AuthToken);



    @GET
    Call<feedscrimepojo> allFeeds(@Url String url, @Header("Accept") String accept,
                                  @Header("Authorization") String AuthToken);

    @GET
    Call<Image> allFeeds1(@Url String url, @Header("Accept") String accept,
                          @Header("Authorization") String AuthToken);



//    @GET
//    Call<Followpojo> allFollowList(@Url String url, @Header("Accept") String accept,
//                                   @Header("Authorization") String AuthToken);

    @GET
    Call<Allfollowlistpojo> AllMemberFollowList(@Url String url, @Header("Accept") String accept,
                                                @Header("Authorization") String AuthToken);


    @POST("follow/")
    @FormUrlEncoded
    Call<String> folloStatus(@Field("followId") String follow,
                             @Header("Accept") String accept,
                             @Header("Authorization") String AuthToken);


    @GET
    Call<UserNameListPojo> city(@Url String url, @Header("Accept") String accept,
                                @Header("Authorization") String AuthToken);

    @POST("recommended-user/")
    @FormUrlEncoded
    Call<Followpojo> allFollowList(@Field("usrnam") String uname,
                                   @Field("country") String country,
                                   @Field("state") String state,
                                   @Field("city") String city,
                                   @Field("area") String area,
                                   @Field("profileCategory") String categoty,
                                   @Header("Accept") String accept,
                                   @Header("Authorization") String AuthToken);

    @POST("recommended-user/")
    @FormUrlEncoded
    Call<Followpojo> allFollowListFilter(@Field("usrnam") String uname,
                                   @Field("country") String country,
                                   @Field("state") String state,
                                   @Field("city") String city,
                                   @Field("area") String area,
                                   @Field("profileCategory") String categoty,
                                   @Header("Accept") String accept,
                                   @Header("Authorization") String AuthToken);




    @POST("view-like-comment-post/")
    @FormUrlEncoded
    Call<String> viewLikeComment(@Field("PostId") String postId,
                                 @Field("category") String catgeory,
                                 @Field("comment") String comment,@Header("Accept") String accept, @Header("Authorization") String AuthToken);


    @GET
    Call<NotifyPojo> getnotification(@Url String url, @Header("Accept") String accept,
                                     @Header("Authorization") String AuthToken);


    @GET
    Call<OtherProfilePojo> getotherprofile(@Url String url, @Header("Accept") String accept,
                                           @Header("Authorization") String AuthToken);



    @POST("block-user/")
    @FormUrlEncoded
    Call<String> blockother(@Field("blockedUser") String userId,
                                @Header("Accept") String accept, @Header("Authorization") String AuthToken);



    @GET
    Call<BlocklistPojo> allblockList(@Url String url, @Header("Accept") String accept,
                                     @Header("Authorization") String AuthToken);



    @POST("update-token/")
    @FormUrlEncoded
    Call<String> updatetoken(@Field("ftoken") String ftkn, @Header("Accept") String accept,
                             @Header("Authorization") String AuthToken);


//    @POST("gallery/")
//    @FormUrlEncoded
//    Call<StaffPojo> InfraImageGallery(@Field("courseId") String courseid,
//                                      @Field("belongsto") String belongsTo,
//                                      @Field("type") String imagorvideo,
//                                      @Header("Accept") String accept, @Header("Authorization") String AuthToken);




}

