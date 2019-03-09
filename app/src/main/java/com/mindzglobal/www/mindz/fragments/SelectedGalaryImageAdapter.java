package com.mindzglobal.www.mindz.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindzglobal.www.mindz.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class SelectedGalaryImageAdapter extends RecyclerView.Adapter<SelectedGalaryImageAdapter.ViewHolder> {
/*

   CrossRemove mRemoveCross;

    public interface CrossRemove {
        void mRemovex();

    }

*/

    Context context;
    GridViewAdapter.ViewHolder viewHolder;
//    ArrayList<String> imagePath;
    public GalleryClickedPosition delegate=null;
    int selectedCount=0;
    List<String> selectedImages;
    public RemoveImage removeImage = null;
    private OnItemClickListener mOnItemClickListener;
    private DisplayImageOptions options;



    public SelectedGalaryImageAdapter(Context context,  List<String> selectedImages) {
        this.selectedImages = selectedImages;
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
    public interface OnItemClickListener {
        void onItemClick(View view, List<String> selectedImages, int clickedPosition);

    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gridview_iteam, null);
        ViewHolder mh = new ViewHolder(v);
        delegate =(GalleryClickedPosition)context;
//        mRemoveCross=(CrossRemove)context;
        return mh;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder itemRowHolder, int position) {

        itemRowHolder.selectedlayout.setVisibility(View.GONE);
        itemRowHolder.crossImage.setVisibility(View.VISIBLE);
        if (position == 0) {
            itemRowHolder.linearLayout.setVisibility(View.VISIBLE);
        } else {
            itemRowHolder.linearLayout.setVisibility(View.GONE);
        }

//        ImageLoader.getInstance().displayImage("file://" + imagePath.get(position), itemRowHolder.iv_image, options);//Load Images over ImageView
        Glide.with(context)
                .load(selectedImages.get(position))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .centerCrop()
                .into(itemRowHolder.iv_image);

    }

    @Override
    public int getItemCount() {
        return selectedImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image,crossImage;
        LinearLayout linearLayout;
        RelativeLayout selectedlayout;

        public ViewHolder(View convertView) {
            super(convertView);
            this.iv_image =  convertView.findViewById(R.id.iv_image);
            this.linearLayout =  convertView.findViewById(R.id.cameraLayout_view);
            this.selectedlayout =  convertView.findViewById(R.id.slected_icon);
            this.crossImage =  convertView.findViewById(R.id.remove_image);

            crossImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    String ImageUrl = selectedImages.get(clickedPosition);
                    selectedImages.remove(ImageUrl);
                    delegate.clicked(selectedImages,0);
//                    mRemoveCross.mRemovex();
                }
            });

            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        int clickedPosition = getAdapterPosition();
                       // mOnItemClickListener.onItemClick(v,imagePath.get(clickedPosition));
                        mOnItemClickListener.onItemClick(v, selectedImages,clickedPosition);
                    }
                }
            });
        }

    }

}