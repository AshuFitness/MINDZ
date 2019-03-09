package com.mindzglobal.www.mindz.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Home.CrimeFeeds.CrimeActivity;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;

public class PokeActivity extends AppCompatActivity implements View.OnClickListener{



    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private RecyclerView.LayoutManager layoutManager;

    ProgressBar bar;

    String token;
    SessionManager manager;
    RetrofitClient retrofitClient;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poke_layout);

        recyclerView = findViewById(R.id.recycler_poke);
        recyclerView.setHasFixedSize(true);
        myOnClickListener = new MyOnClickListener(PokeActivity.this);
        layoutManager = new LinearLayoutManager(PokeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        bar = findViewById(R.id.progressBar);

//        CrimeFeeds();

        mSwipeRefreshLayout = findViewById(R.id.swipeContainercrime);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_context_menu_text_red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                CrimeFeeds();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onClick(View v) {

    }


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
