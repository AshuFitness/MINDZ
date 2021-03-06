package com.mindzglobal.www.mindz;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.GetFeedx;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.feedscrimepojo;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ImageActivity extends AppCompatActivity {
    ImageView iv;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private RecyclerView.LayoutManager layoutManager;

    String token;
    SessionManager manager;
    RetrofitClient retrofitClient;

    String imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image2);

        recyclerView = findViewById(R.id.recycler_img);
        recyclerView.setHasFixedSize(true);
        myOnClickListener = new MyOnClickListener(ImageActivity.this);
        layoutManager = new LinearLayoutManager(ImageActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


      /*  iv =  findViewById(R.id.img_posted2);
        Bundle b=getIntent().getExtras();
         imageview=b.getString("imageid");

        Glide.with(this).load(imageview).into(iv);*/

//        CrimeFeeds();
    }

  /*  public void CrimeFeeds() {
//        bar.setVisibility(View.VISIBLE);
        String url = "https://www.homiezin.com/api/feed/Crime/";

        RetrofitClient.getWithClient().allFeeds(url,"application/json","Bearer "+token)
                .enqueue(new GlobalCallback<feedscrimepojo>(recyclerView) {
                    @Override
                    public void onResponse(Call<feedscrimepojo> call, Response<feedscrimepojo> response) {

//				Log.e("FeedsResponse", response.body().toString());

//                        bar.setVisibility(View.GONE);
                        List<GetFeedx> FeedCrimeList = response.body().getGetFeedx();

                        if(FeedCrimeList.isEmpty()){
//					emptyElement.setVisibility(View.VISIBLE);
                        }

                        else {
                            adapter = new ImagAdpater((ArrayList<GetFeedx>) FeedCrimeList,ImageActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });*/
//    }
    public  class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "clicked cardview", Toast.LENGTH_SHORT).show();
//			int selectedItemPosition = recyclerView.getChildPosition(v);
//			RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
//			TextView jobtitle = (TextView) viewHolder.itemView.findViewById(R.id.domain);
//			TextView companyName = (TextView) viewHolder.itemView.findViewById(R.id.company_name);
//			TextView experience = (TextView) viewHolder.itemView.findViewById(R.id.experience);
//			TextView location = (TextView) viewHolder.itemView.findViewById(R.id.location);
//			TextView no_position = (TextView) viewHolder.itemView.findViewById(R.id.no_position);


//			Toast.makeText(context, jobtitle.getText().toString(), Toast.LENGTH_SHORT).show();
//			Intent des = new Intent(JobsAllActivity.this,JobDiscription.class);
//			checkJobView(jobId.getText().toString(),emp);
//			des.putExtra("jobTitle",jobtitle.getText().toString());
//			des.putExtra("companyName",companyName.getText().toString());
//			des.putExtra("experience",experience.getText().toString());
//			des.putExtra("location",location.getText().toString());
//			startActivity(des);
        }
    }



}
