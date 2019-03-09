package com.mindzglobal.www.mindz.FriendRequest.global;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Follow.FollowActivity;
import com.mindzglobal.www.mindz.MainActivity;
import com.mindzglobal.www.mindz.Profile.AboutActivity;
import com.mindzglobal.www.mindz.Profile.OtherProfileActivity;
import com.mindzglobal.www.mindz.R;
import com.mindzglobal.www.mindz.fragments.GuidePageActivity2;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener  {
//    DialogClickInterface

    RelativeLayout relative_findfrnd, block_list, help_support, settings_global,
            privacy_global, about_global, faq_global, logout_global, poke_list;

    Dialog dialog;
    SessionManager manager;
    int identifier = 0;

    public SettingsFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        manager = new SessionManager();
        relative_findfrnd = view.findViewById(R.id.relative_findfrnd);
        block_list = view.findViewById(R.id.block_list);
        poke_list = view.findViewById(R.id.poke_list);
        help_support = view.findViewById(R.id.help_support);
        settings_global = view.findViewById(R.id.settings_global);
        privacy_global = view.findViewById(R.id.privacy_global);
        about_global = view.findViewById(R.id.about_global);
        faq_global = view.findViewById(R.id.faq_global);
        logout_global = view.findViewById(R.id.logout_global);

        relative_findfrnd.setOnClickListener(this);
        block_list.setOnClickListener(this);
        poke_list.setOnClickListener(this);
        help_support.setOnClickListener(this);
        settings_global.setOnClickListener(this);
        privacy_global.setOnClickListener(this);
        about_global.setOnClickListener(this);
        faq_global.setOnClickListener(this);
        logout_global.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.relative_findfrnd:
                Intent intent_firnds = new Intent(getActivity(), FollowActivity.class);
                startActivity(intent_firnds);
                break;


            case R.id.poke_list:
                Intent intent_poke = new Intent(getActivity(), PokeListActivity.class);
                startActivity(intent_poke);
                break;


            case R.id.block_list:
                Intent intent_block = new Intent(getActivity(), BlockListActivity.class);
                startActivity(intent_block);
                break;

            case R.id.help_support:
                Intent intent_help = new Intent(getActivity(), HelpSuupportActivity.class);
                startActivity(intent_help);
                break;


            case R.id.settings_global:
                Intent intent_settings = new Intent(getActivity(), SettingsGlobalActivity.class);
                startActivity(intent_settings);
                break;

            case R.id.privacy_global:
                Intent intent_privacy = new Intent(getActivity(), PrivacyActivity.class);
                startActivity(intent_privacy);
                break;

            case R.id.about_global:
                Intent intent_about = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent_about);
                break;

            case R.id.faq_global:
                Intent intent_faq = new Intent(getActivity(), FAQActivity.class);
                startActivity(intent_faq);
                break;

            case R.id.logout_global:
                logoutdialog();
//                CustomAlertDialog.getInstance().showConfirmDialog("Logout", "Are you sure, you want to logout?", "Yes", "No", getContext(), identifier);
                break;


        }


    }


//    @Override
//    public void onClickPositiveButton(DialogInterface pDialog, int pDialogIntefier) {
//        if (pDialogIntefier == 0)
//
//            manager.setPreferences(getActivity(), Constants.LOGIN_STATUS, "0");
//        manager.setPreferences(getActivity(), Constants.CAMERA_STATUS, "0");
//
//        Intent intent = new Intent(getActivity(), GuidePageActivity2.class);
//        startActivity(intent);
//        getActivity().finish();
//
//        Toast.makeText(getActivity(), "Clicked on positive button..!", Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void onClickNegativeButton(DialogInterface pDialog, int pDialogIntefier) {
//        if (pDialogIntefier == 0)
//            pDialog.cancel();
//        Toast.makeText(getActivity(), "Clicked on negative button..!", Toast.LENGTH_SHORT).show();
//    }



    public void logoutdialog(){

        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.logout_dialog_layout);

        Button cancel_logout=dialog.findViewById(R.id.cancel_logout);
        Button logout_dialog =dialog.findViewById(R.id.logout_dialog);

        logout_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.setPreferences(getActivity(), Constants.LOGIN_STATUS, "0");
                manager.setPreferences(getActivity(), Constants.CAMERA_STATUS, "0");

                Intent intent = new Intent(getActivity(), GuidePageActivity2.class);
                startActivity(intent);
                getActivity().finish();
                dialog.dismiss();

            }
        });

        cancel_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }




//    public void logout() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setIcon(R.drawable.test)
//                .setTitle("Log out")
//                .setMessage("Are you sure you want to exit?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        manager.setPreferences(getActivity(), Constants.LOGIN_STATUS, "0");
//                        manager.setPreferences(getActivity(), Constants.CAMERA_STATUS, "0");
//
//                        Intent intent = new Intent(getActivity(), GuidePageActivity2.class);
//                        startActivity(intent);
//                        getActivity().finish();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
//
//    }
}


