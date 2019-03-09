package com.mindzglobal.www.mindz.FriendRequest.global;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;


import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;

public class FAQActivity extends Activity {

    ArrayList<QuestionAnswerModel> list;
    FAQAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);


        list=new ArrayList<>();
        adapter=new FAQAdapter(list,FAQActivity.this);

        ListView listview=findViewById(R.id.listview);
        list.add(new QuestionAnswerModel(getResources().getString(R.string.Q1),getResources().getString(R.string.Q1_ANS),false));
        list.add(new QuestionAnswerModel(getResources().getString(R.string.Q2),getResources().getString(R.string.Q2_ANS),false));
        list.add(new QuestionAnswerModel(getResources().getString(R.string.Q3),getResources().getString(R.string.Q3_ANS),false));
        list.add(new QuestionAnswerModel(getResources().getString(R.string.Q4),getResources().getString(R.string.Q4_ANS),false));
        list.add(new QuestionAnswerModel(getResources().getString(R.string.Q5),getResources().getString(R.string.Q5_ANS),false));
        list.add(new QuestionAnswerModel(getResources().getString(R.string.Q6),getResources().getString(R.string.Q6_ANS),false));
        list.add(new QuestionAnswerModel(getResources().getString(R.string.Q7),getResources().getString(R.string.Q7_ANS),false));
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}
