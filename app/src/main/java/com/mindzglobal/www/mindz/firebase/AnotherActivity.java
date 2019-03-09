package com.mindzglobal.www.mindz.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mindzglobal.www.mindz.R;


/**
 * Created by AndroidBash on 20-Aug-16.
 */
public class AnotherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);

                assert value != null;
                if (key.equals("AnotherActivity")) {
//                    Intent intent = new Intent(this, RecommendedJobsActivity.class);
//                    Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
//                    intent.putExtra("value", value);
//                    startActivity(intent);
//                    finish();
                }

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);

    }
}

