package com.mindzglobal.www.mindz.fragments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.mindzglobal.www.mindz.R;


public class AlbumPhotosActivity extends AppCompatActivity {
    int int_position;
    private GridLayoutManager lLayout;
    AlbumGridViewAdapter adapter;
    RecyclerView image_List;
    LinearLayout ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallary_main_activity);
        int_position = getIntent().getIntExtra("value", 0);

        image_List = (RecyclerView) findViewById(R.id.gv_folder);
        lLayout = new GridLayoutManager(AlbumPhotosActivity.this, 3);
        image_List.setHasFixedSize(true);
        image_List.setLayoutManager(lLayout);
        ok=(LinearLayout) findViewById(R.id.ok);

        adapter = new AlbumGridViewAdapter(AlbumPhotosActivity.this, BrowsePicture2.al_images, int_position,ok);
        image_List.setAdapter(adapter);
    }
}
