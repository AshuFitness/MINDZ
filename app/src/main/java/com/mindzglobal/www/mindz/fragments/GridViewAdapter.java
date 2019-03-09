package com.mindzglobal.www.mindz.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindzglobal.www.mindz.R;


import java.util.ArrayList;


public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> implements RemoveImage{

    Activity context;
    ViewHolder viewHolder;
    private ArrayList<String> selectedImage=new ArrayList<>();
    private ArrayList<Model_images> al_menu;
    private GalleryClickedPosition delegate=null;
    private int ImageSelected=0;
    private int int_position;



    ArrayList<String> imagePath=new ArrayList<>();


    public GridViewAdapter(Activity context, ArrayList<Model_images> al_menu, int int_position) {
        this.al_menu = al_menu;
        this.context = context;
        this.int_position = int_position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gridview_iteam, null);
        ViewHolder mh = new ViewHolder(v);
       delegate= (GalleryClickedPosition) context;
        return mh;
    }




    @Override
    public void onBindViewHolder(@NonNull ViewHolder itemRowHolder, int position) {

        itemRowHolder.selectedlayout.setVisibility(View.GONE);
        if(position==0){
            itemRowHolder.linearLayout.setVisibility(View.VISIBLE);
        }else{
            itemRowHolder.linearLayout.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load("file://" + al_menu.get(int_position).getAl_imagepath().get(position))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .centerCrop()
                .into(itemRowHolder.iv_image);

    }

    @Override
    public int getItemCount() {
        int count=0;
        try {
            if(al_menu.size()>int_position){
                count=al_menu.get(int_position).getAl_imagepath().size();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void callBack(String imagePath) {
//        selectedlayout.setVisibility(View.GONE);
      int position =  getCategoryPos(imagePath);
        notifyItemChanged(position);
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image;
        LinearLayout linearLayout;
        RelativeLayout selectedlayout;


        public ViewHolder(View convertView) {
            super(convertView);
            this.iv_image =  convertView.findViewById(R.id.iv_image);
            this.linearLayout=convertView.findViewById(R.id.cameraLayout_view);
            this.selectedlayout=convertView.findViewById(R.id.slected_icon);

           iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = getAdapterPosition();
                if(clickedPosition==0){
                    delegate.clicked(selectedImage,1);
                }else{
                    if(selectedImage.size()<10){
                        selectedlayout.setVisibility(View.VISIBLE);

                        String ImageUrl = al_menu.get(int_position).getAl_imagepath().get(clickedPosition);
                        selectedImage.add(ImageUrl);
                        delegate.clicked(selectedImage,0);
                    }else{
                        Toast.makeText((Context) context, "U Can select only 10 Image at a time", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

            selectedlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectedlayout.setVisibility(View.GONE);
                    int clickedPosition = getAdapterPosition();
                    String ImageUrl = al_menu.get(int_position).getAl_imagepath().get(clickedPosition);
                    selectedImage.remove(ImageUrl);
                    delegate.clicked(selectedImage,0);
                }
            });
        }

/*
        @Override
        public void mRemovex() {
            selectedlayout.setVisibility(View.GONE);
        }*/
    }
    private int getCategoryPos(String category) {
        return al_menu.get(int_position).getAl_imagepath().indexOf(category);
    }
}





