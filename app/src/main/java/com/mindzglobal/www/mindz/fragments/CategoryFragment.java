package com.mindzglobal.www.mindz.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.Bind;
import custom_font.MyTextVieww;
import tyrantgit.explosionfield.ExplosionField;

public class CategoryFragment extends Fragment implements View.OnClickListener {

    ImageView pic_11;

    @Bind(R.id.pic_2)
    ImageView pic_2;

    @Bind(R.id.pic_3)
    ImageView pic_3;

    @Bind(R.id.pic_4)
    ImageView pic_4;

    @Bind(R.id.pic_5)
    ImageView pic_5;

    @Bind(R.id.pic_6)
    ImageView pic_6;

    @Bind(R.id.pic_7)
    ImageView pic_7;

//    @Bind(R.id.choose_cat_rel)
    RelativeLayout mChoose_cat_rel;

    @Bind(R.id.category)
    MyTextVieww mCategory;

    private ExplosionField mExplosionField;
    ArrayList<String> getMediaBase;

    SessionManager manager;
    String token;

    RetrofitClient retrofitClient;

    OnCallbackReceived mCallback;

    public interface OnCallbackReceived {
        void cUpdate();
        void aUpdate();
        void fUpdate();
        void oppUpdate();
        void pUpdate();
        void sUpdate();
        void othUpdate();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnCallbackReceived) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }


    public CategoryFragment() {

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_category, container, false);
//        ButterKnife.bind(this,view);
        manager = new SessionManager();
        token = manager.getPreferences(getActivity(), Constants.USER_TOKEN);
        retrofitClient = new RetrofitClient(token);
        mExplosionField = ExplosionField.attach2Window(Objects.requireNonNull(getActivity()));

        mChoose_cat_rel = view.findViewById(R.id.choose_cat_rel);

//        mCallback.Update();
        pic_11 = view.findViewById(R.id.pic_1);
        pic_2 = view.findViewById(R.id.pic_2);
        pic_3 = view.findViewById(R.id.pic_3);
        pic_4 = view.findViewById(R.id.pic_4);
        pic_5 = view.findViewById(R.id.pic_5);
        pic_6 = view.findViewById(R.id.pic_6);
        pic_7 = view.findViewById(R.id.pic_7);

        pic_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExplosionField.explode(v);
                mCallback.cUpdate();
                Toast.makeText(getActivity(), "Crime", Toast.LENGTH_SHORT).show();
                mChoose_cat_rel.setVisibility(View.GONE);

            }

        });



        pic_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExplosionField.explode(v);
                mCallback.aUpdate();
                Toast.makeText(getActivity(), "Area Problem", Toast.LENGTH_SHORT).show();
                mChoose_cat_rel.setVisibility(View.GONE);

            }

        });


        pic_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExplosionField.explode(v);
                mCallback.fUpdate();
                Toast.makeText(getActivity(), "Fun Activity", Toast.LENGTH_SHORT).show();
                mChoose_cat_rel.setVisibility(View.GONE);
            }

        });


        pic_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExplosionField.explode(v);
                mCallback.oppUpdate();
                Toast.makeText(getActivity(), "Opportunity", Toast.LENGTH_SHORT).show();
                mChoose_cat_rel.setVisibility(View.GONE);

//                mMain_screen.setVisibility(View.VISIBLE);

            }

        });




        pic_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExplosionField.explode(v);
                mCallback.pUpdate();
                Toast.makeText(getActivity(), "Political", Toast.LENGTH_SHORT).show();
                mChoose_cat_rel.setVisibility(View.GONE);

            }

        });


        pic_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExplosionField.explode(v);
                mCallback.sUpdate();
                Toast.makeText(getActivity(), "Sports", Toast.LENGTH_SHORT).show();
                mChoose_cat_rel.setVisibility(View.GONE);
            }

        });




        pic_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mExplosionField.explode(v);
                mCallback.othUpdate();
                Toast.makeText(getActivity(), "Other", Toast.LENGTH_SHORT).show();
                mChoose_cat_rel.setVisibility(View.GONE);
            }

        });


        return view;
    }


    @Override
    public void onClick(View view) {

    }



}

