package com.mindzglobal.www.mindz.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindzglobal.www.mindz.R;

import java.util.ArrayList;
import java.util.List;




public class ExFilterAdapter extends RecyclerView.Adapter<ExFilterAdapter.ViewHolder> {

    int dra[]={R.drawable.pro2,R.drawable.pro2,
            R.drawable.pro2,R.drawable.pro2,
            R.drawable.pro2,R.drawable.pro2,
            R.drawable.pro2,R.drawable.pro2,
            R.drawable.pro2,R.drawable.pro2};

    private List<ModelClass> mFilters = new ArrayList<>();
    private ExColorDrawable listener;
    private Context context;
    private Bitmap mBitmap;
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView filterImages;
        TextView mTextView;
        FrameLayout mViewFrame;

        ViewHolder(View view) {
            super(view);
            this.mTextView = (TextView) view.findViewById(R.id.filter_name);
            this.filterImages = (ImageView) view.findViewById(R.id.list_item_gpuimage);
            this.mViewFrame = (FrameLayout) view.findViewById(R.id.cont_filter);

            mViewFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();
                    listener.drawableList(i);
                    }
            });
        }
    }


    public ExFilterAdapter(Context context, ExColorDrawable listener ) {

        this.context = context;
        this.listener = listener;
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pro2);

    }

    @Override
    public ExFilterAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.filter_list_iteam, null);
        return new ExFilterAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExFilterAdapter.ViewHolder holder, int position) {


        holder.mTextView.setText(position);
        Bitmap  bitmap = BitmapFactory.decodeResource(context.getResources(),dra[position]);
        holder.filterImages.setImageBitmap(bitmap);
    }


    @Override
    public int getItemCount() {
        return dra.length;
    }
}