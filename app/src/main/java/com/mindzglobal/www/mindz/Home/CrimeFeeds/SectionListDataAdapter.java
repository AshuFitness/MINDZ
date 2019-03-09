package com.mindzglobal.www.mindz.Home.CrimeFeeds;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ImageActivity;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.Image;
import com.mindzglobal.www.mindz.R;

import java.util.List;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private List<Image> itemsList;
    private Activity mContext;

    public SectionListDataAdapter(Activity context, List<Image> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }



    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.media_item_box, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        ImageView itemImage = holder.itemImage;

        String singleItem = itemsList.get(i).getImg();


        if(singleItem.isEmpty()){

        }

        else {
            Glide.with(mContext)
                    .load(Config.BASE_URL_MEDIA+singleItem)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(itemImage);

        }


    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
//        return itemsList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

//        protected TextView tvTitle;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

//            this.tvTitle =  view.findViewById(R.id.tvTitle);
            this.itemImage =  view.findViewById(R.id.itemImage);


            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if (pos!=RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(mContext, ImageActivity.class);
                        intent.putExtra("imageid", itemsList.get(pos).getImg());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You Touched", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

    }

}