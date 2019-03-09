package com.mindzglobal.www.mindz.fragments;



import android.app.Activity;
import android.content.Intent;
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


public class AlbumGridViewAdapter extends RecyclerView.Adapter<AlbumGridViewAdapter.ViewHolder> implements RemoveImage {

    Activity context;
    ArrayList<String> selectedImage=new ArrayList<>();
    ArrayList<Model_images> al_menu;
    int int_position;
    LinearLayout ok;

    public AlbumGridViewAdapter(Activity context, ArrayList<Model_images> al_menu, int int_position, LinearLayout ok) {
        this.al_menu = al_menu;
        this.context = context;
        this.int_position = int_position;
        this.ok=ok;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gridview_iteam, null);
        ViewHolder mh = new ViewHolder(v);
        return mh;
    }




    @Override
    public void onBindViewHolder(ViewHolder itemRowHolder, int position) {

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

      /*  itemRowHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ChatTreadActivity.class);
                mContext.startActivity(it);
            }
        });*/
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
        int position =  getCategoryPos(imagePath);
        notifyItemChanged(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image;
        LinearLayout linearLayout;
        RelativeLayout selectedlayout;

        public ViewHolder(View convertView) {
            super(convertView);
            this.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            this.linearLayout=(LinearLayout)convertView.findViewById(R.id.cameraLayout_view);
            this.selectedlayout=(RelativeLayout)convertView.findViewById(R.id.slected_icon);


            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    ok.setVisibility(View.VISIBLE);
                        if(selectedImage.size()<10){
                            selectedlayout.setVisibility(View.VISIBLE);
                            String ImageUrl = al_menu.get(int_position).getAl_imagepath().get(clickedPosition);
                            selectedImage.add(ImageUrl);

                        }else{
                            Toast.makeText(context, "U Can select only 10 Image at a time", Toast.LENGTH_LONG).show();
                        }
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent();
                    it.putStringArrayListExtra("selectedImage",selectedImage);
                    context.setResult(5,it);
                    context.finish();
                }
            });

            selectedlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectedlayout.setVisibility(View.GONE);
                    int clickedPosition = getAdapterPosition();
                    String ImageUrl = al_menu.get(int_position).getAl_imagepath().get(clickedPosition);
                    selectedImage.remove(ImageUrl);
                    Intent it =new Intent(context,CreatePostActivity_New.class );
                    it.putStringArrayListExtra("selectedImage",selectedImage);
                    context.startActivityForResult(it, 5);
                }
            });
        }

    }
    private int getCategoryPos(String category) {
        return al_menu.get(int_position).getAl_imagepath().indexOf(category);
    }
}




/*
public class GridViewAdapter extends ArrayAdapter<Model_images>  {

    Context context;
    ViewHolder viewHolder;
    ArrayList<Model_images> al_menu = new ArrayList<>();
    int int_position;


    public GridViewAdapter(Context context, ArrayList<Model_images> al_menu, int int_position) {
        super(context, R.layout.adapter_photosfolder, al_menu);
        this.al_menu = al_menu;
        this.context = context;
        this.int_position = int_position;


    }

    @Override
    public int getCount() {

        Log.e("ADAPTER LIST SIZE", al_menu.get(int_position).getAl_imagepath().size() + "");
        return al_menu.get(int_position).getAl_imagepath().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_menu.get(int_position).getAl_imagepath().size() > 0) {
            return al_menu.get(int_position).getAl_imagepath().size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gridview_iteam, parent, false);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.linearLayout=(LinearLayout)convertView.findViewById(R.id.cameraLayout_view);
            viewHolder.selectedlayout=(RelativeLayout)convertView.findViewById(R.id.slected_icon);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.selectedlayout.setVisibility(View.GONE);
if(position==0){
    viewHolder.linearLayout.setVisibility(View.VISIBLE);
}else{
    viewHolder.linearLayout.setVisibility(View.GONE);
}

        Glide.with(context)
                .load("file://" + al_menu.get(int_position).getAl_imagepath().get(position))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .centerCrop()
                .into(viewHolder.iv_image);

        return convertView;

    }



    private static class ViewHolder  {
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image;
        LinearLayout linearLayout;
        RelativeLayout selectedlayout;

       */
/* @Override implements GalleryClickedPosition
        public void clicked(int Position) {
            selectedlayout.setVisibility(View.VISIBLE);
        }*//*

    }


}
*/