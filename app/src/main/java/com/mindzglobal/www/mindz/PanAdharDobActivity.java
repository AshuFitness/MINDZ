package com.mindzglobal.www.mindz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mindzglobal.www.mindz.Home.HomeFragment;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class PanAdharDobActivity extends AppCompatActivity implements View.OnClickListener {

    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijg3ZjhiNWYwYzkxYTIyZGExNjBlOGM0OGFjYWI3NTZhNjUyOGE1YjNjMDBmMGIxZGMyMjc3OTI2ZDM5MTNiM2FjNTRiYzMxNmVjYmRmNmJhIn0.eyJhdWQiOiIxIiwianRpIjoiODdmOGI1ZjBjOTFhMjJkYTE2MGU4YzQ4YWNhYjc1NmE2NTI4YTViM2MwMGYwYjFkYzIyNzc5MjZkMzkxM2IzYWM1NGJjMzE2ZWNiZGY2YmEiLCJpYXQiOjE1MTQ5NzkwMzMsIm5iZiI6MTUxNDk3OTAzMywiZXhwIjoxNTQ2NTE1MDMzLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.fyvxm3_p4aHDqcZ_NVPM5LPdjBQ9ziVFfabC0pQ1mOdpew4GC-c8N18ILB5Q7j4ZEjf4UAPIUWN7I-8ljAWLd2fooa-Kzk5GQP46lo6U8JjwDwSa-JtwnA5epDZgi_UY5zUeRWnWFtbLSOGXktgMXett_9fqRpIia2H8ag9lsQR9m96MeskTYOggWVNEQ553DULtWIrKrKApf1Sj4xVO7rGSba5OuxIe0ffGLWN2eqO30CQtToiHx_VEBKmPI_ipslhJzTf2looBItTH0kACw_VYHgEq80LGhUUhRpbkfj55ZRvbLfPbwys3QB5IO4Olh66hoxtnAhdHVfJYmmSADksKhnTWvv675D4SC2-0-sGx-f1LE3OHo0GJX9geGPVCBPf9kuR337NTAmzAmuuYnH-jTCJ-VGI42Jlo0tvm4EQKWAgWOYLOIVDPJ82EHw50fUx0w6oppA6dDgpTAqCCFdvaJf6OfdlGzW8Lk71VhBE5SdDH_JdysuF4QxL_GzJlyUvT0aN3ScAxFr-db08bE1g_jLmu6ND5XVdhK4HdPeGHliHKjXZ1P-yqTf9qhny_M_opx_EfKscV1YgHIGHq1IYCBzXnMAWND2BlnxVotCSTl0p4LyLwIAPH646GWG7--KIqe02P8uW7AJn5hWvivQy0raH3x1mprRyqJUcp9tM";

    @Bind(R.id.dob_edit) TextView datofbirth;
    @Bind(R.id.pan_edit) EditText pan;
    @Bind(R.id.adhar_edit) EditText adhar;

    @Bind(R.id.submit) Button submit;
    @Bind(R.id.skip) Button skip;


    static final int DATE_DIALOG_ID = 0;
    private int mYear,mMonth,mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pan_adhar_layout);
        ButterKnife.bind(this);



        Calendar c=Calendar.getInstance();
        mYear=c.get(Calendar.YEAR);
        mMonth=c.get(Calendar.MONTH);
        mDay=c.get(Calendar.DAY_OF_MONTH);
        //String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        datofbirth.setText( sdf.format(c.getTime()));

        datofbirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG_ID);

            }
        });


        submit.setOnClickListener(this);


    }


    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            datofbirth.setText(new StringBuilder().append(mDay).append("/").append(mMonth+1).append("/").append(mYear));
        }
    };




    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.submit:
                if (checkValidate()){

                    dobPanAdhar();
                }
                break;

            case R.id.skip:
                Intent s = new Intent(PanAdharDobActivity.this,HomeFragment.class);
                startActivity(s);
                finish();
                break;
        }

    }

    public boolean checkValidate(){

        boolean valid = true;

        String dob = datofbirth.getText().toString();

        if (dob.isEmpty()){
            datofbirth.setError("Select your date of birth");
            valid = false;
        } else {
            datofbirth.setError(null);
        }
        return valid;
    }

    public void dobPanAdhar(){

        RetrofitClient.getWithClient().dobpanadhar(datofbirth.getText().toString(),pan.getText().toString(),adhar.getText().toString(), "application/json","Bearer "+token)
                .enqueue(new GlobalCallback<String>(skip) {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.e("PanAdhar",response.body().toString());

                String res = response.body().toString();

                if (res.contains("1")){
                    Intent i = new Intent(PanAdharDobActivity.this, HomeFragment.class);
                    startActivity(i);
                    finish();
                }


            }
        });
    }

}
