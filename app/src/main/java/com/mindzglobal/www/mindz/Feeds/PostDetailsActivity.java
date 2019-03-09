package com.mindzglobal.www.mindz.Feeds;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.GetFeedx;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.Image;
import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PostDetailsActivity extends AppCompatActivity {

    public static final String POST_ID_EXTRA_KEY = "PostDetailsActivity.POST_ID_EXTRA_KEY";

//    PageFlipView mPageFlipView;
//    GestureDetector mGestureDetector;

    String post_id;

    int i;
    String token;
    SessionManager manager;

    RecyclerView media_view;
    Activity mContext;
    List<Image> feed_pic;
    private ArrayList<GetFeedx> dataSet;

    TextView nameTextView,authorTextView,view_count,comment_count,dateTextView,descriptionEditText;
    ImageView authorImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        manager = new SessionManager();
        token = manager.getPreferences(PostDetailsActivity.this, Constants.USER_TOKEN);

        Bundle bundle = getIntent().getExtras();
        post_id  = bundle.getString("post_id");
        String post_owner_name = bundle.getString("post_owner_name");
        String post_owner_username = bundle.getString("post_owner_username");
        String post_date = bundle.getString("post_date");
        String post_contents = bundle.getString("post_contents");
        String user_profile_img = bundle.getString("user_profile_img");


        nameTextView = findViewById(R.id.nameTextView);
        authorTextView = findViewById(R.id.authorTextView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateTextView = findViewById(R.id.dateTextView);
        authorImageView = findViewById(R.id.authorImageView);

        media_view = findViewById(R.id.media_view);
//        view_count = findViewById(R.id.view_count);
//        comment_count = findViewById(R.id.comment_count);

        nameTextView.setText(post_owner_username);
        authorTextView.setText(post_owner_name);
        descriptionEditText.setText(post_contents);
        dateTextView.setText(post_date);

        if(user_profile_img.isEmpty()){
            authorImageView.setVisibility(View.GONE);
        }
        else if(!user_profile_img.isEmpty())
        {
            Glide.with(PostDetailsActivity.this).load(Config.BASE_URL_MEDIA+user_profile_img)
                    .centerCrop()
                    .into(authorImageView);
        }

    }

/*    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mPageFlipView.onFingerUp(event.getX(), event.getY());
            return true;
        }

        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mPageFlipView.onFingerDown(e.getX(), e.getY());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        mPageFlipView.onFingerMove(e2.getX(), e2.getY());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }*/



//public void CrimeFeeds() {
//    bar.setVisibility(View.VISIBLE);
//    String url = "https://www.homiezin.com/api/single-feed/"+;

//    RetrofitClient.getWithClient().allFeeds(url,"application/json","Bearer "+token)
//            .enqueue(new GlobalCallback<String>(nameTextView) {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {

//				Log.e("FeedsResponse", response.body().toString());

//                    bar.setVisibility(View.GONE);
//                    List<GetFeedx> FeedCrimeList = response.body().getGetFeedx();

//                        adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList);
//                        recyclerView.setAdapter(adapter);
//                    if(FeedCrimeList.isEmpty()){
//					emptyElement.setVisibility(View.VISIBLE);
//                    }

//                    else {
//                        adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList,CrimeActivity.this);
//                        recyclerView.setAdapter(adapter);
//                    }
//                }
//            });
//    }

}