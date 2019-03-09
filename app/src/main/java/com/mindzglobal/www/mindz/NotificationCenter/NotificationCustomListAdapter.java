package com.mindzglobal.www.mindz.NotificationCenter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindzglobal.www.mindz.Model.Notification.NotificationList;
import com.mindzglobal.www.mindz.R;

import java.util.List;

public class NotificationCustomListAdapter extends ArrayAdapter<NotificationList> {

    private Activity context;
    private List<NotificationList> allnotifyList;

    public NotificationCustomListAdapter(Activity context,List<NotificationList> allnotifyList ) {
        super(context, R.layout.notification_box_layout, allnotifyList);
        this.context = context;
        this.allnotifyList = allnotifyList;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.notification_box_layout, null, true);

        TextView head = listViewItem.findViewById(R.id.head);
        TextView Content = listViewItem.findViewById(R.id.Content);
        TextView nDate = listViewItem.findViewById(R.id.date);
        RelativeLayout rel_img = listViewItem.findViewById(R.id.rel_img);
        ImageView img = listViewItem.findViewById(R.id.image_notify);

        nDate.setText(allnotifyList.get(position).getDate());
        head.setText(allnotifyList.get(position).getTitle());
        Content.setText(allnotifyList.get(position).getMessage());
        String checkImage = allnotifyList.get(position).getImage();
        Glide.with(context).load(checkImage)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);

        if(checkImage.isEmpty()){
            rel_img.setVisibility(View.GONE);
        }
        else
        {
            rel_img.setVisibility(View.VISIBLE);
        }

        return listViewItem;
    }



}
