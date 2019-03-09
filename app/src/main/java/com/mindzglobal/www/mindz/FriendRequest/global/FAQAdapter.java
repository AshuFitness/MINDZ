package com.mindzglobal.www.mindz.FriendRequest.global;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;



public class FAQAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<QuestionAnswerModel> list;
    public FAQAdapter(ArrayList<QuestionAnswerModel> list, Activity activity){
        this.list=list;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view=activity.getLayoutInflater().inflate(R.layout.question_answer_layout,null);
        TextView textViewQuestion=(TextView)view.findViewById(R.id.textviewQuestion);
        TextView textviewAnswer=(TextView)view.findViewById(R.id.textviewAnswer);
        ImageView imageview=(ImageView)view.findViewById(R.id.imageview);
        LinearLayout linlayque=(LinearLayout)view.findViewById(R.id.linlayque);

        linlayque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean flag=list.get(i).isOpen();
                if(flag==false){
                    for(int j=0;j<list.size() ;j++){
                        list.get(j).setOpen(false);
                        list.get(i).setOpen(true);
                    }
                }else{
                    list.get(i).setOpen(false);
                }
                notifyDataSetChanged();
            }
        });
        if(list.get(i).isOpen()){
            imageview.setImageResource(R.drawable.arrow_up);
            textviewAnswer.setVisibility(View.VISIBLE);
        }else{
            imageview.setImageResource(R.drawable.arrow_down);
            textviewAnswer.setVisibility(View.GONE);
        }
        textViewQuestion.setText(list.get(i).getQuestion());
        textviewAnswer.setText(list.get(i).getAnswer());
        return view;
    }
}
