package com.mindzglobal.www.mindz.LoginRegistration;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;
import com.mindzglobal.www.mindz.Utils;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import custom_font.MyEditText;
import custom_font.MyTextView;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Admin on 22-12-2017.
 */

public class SignupActivity  extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.autocomplete)
    PlacesAutocompleteTextView mAutocomplete;

    @Bind(R.id.state)
    TextView mState;

    @Bind(R.id.city)
    TextView mCity;

    @Bind(R.id.area)
    TextView mArea;

    @Bind(R.id.country)
    TextView mCountry;

    @Bind(R.id.choose_cat)
    MyTextView mchoose_cat;

    @Bind(R.id.address_lay)
    LinearLayout maddress_lay;

    @Bind(R.id.user_name)
    MyEditText mUser_name;

    @Bind(R.id.name)
    MyEditText mName;

    @Bind(R.id.phone)
    MyEditText mPhone;

    @Bind(R.id.email)
    MyEditText mEmail;

    @Bind(R.id.signUp)
    MyTextView mSignUp;


    @Bind(R.id.password)
    MyEditText mPassword;

    @Bind(R.id.show_password)
    CheckBox mShow_password;

    @Bind(R.id.gender_rgroup)
    RadioGroup gender_radiogroup;

    @Bind(R.id.male_radiobtn)
    RadioButton male;

    @Bind(R.id.female_radiobtn)
    RadioButton female;

    RadioButton gendersm;

    @Bind(R.id.check_private)
    CheckBox mCheck_private;

    String FireBaseToken,accountType;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_layout);
        ButterKnife.bind(this);
        FireBaseToken = FirebaseInstanceId.getInstance().getToken();

        mchoose_cat.setOnClickListener(this);
        mSignUp.setOnClickListener(this);


        mShow_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked){

                            mShow_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            mPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                            mPassword.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        }
                        else {
                            mShow_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text
                            mPassword.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            mPassword.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }
                    }
                });

        mAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                mAutocomplete.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(final PlaceDetails details) {
                      //  Log.d("test", "details " + details);
                        maddress_lay.setVisibility(View.VISIBLE);
                        mState.setText("");
                        mCity.setText("");
                        mArea.setText("");
                        mCountry.setText("");

//                        double lat = details.geometry.location.lat;
//                        double lon = details.geometry.location.lng;
                        
                        for (AddressComponent component : details.address_components) {
                            for (AddressComponentType type : component.types) {
                                switch (type) {

                                    case STREET_NUMBER:
                                        break;

                                    case ROUTE:
                                        break;

                                    case NEIGHBORHOOD:
                                        break;

                                    case SUBLOCALITY_LEVEL_1:
                                        break;

                                    case SUBLOCALITY:
                                        mArea.setText(component.long_name);
                                        break;

                                    case LOCALITY:
                                        mCity.setText(component.long_name);
                                        break;

                                    case ADMINISTRATIVE_AREA_LEVEL_1:
                                        mState.setText(component.long_name);
                                        break;

                                    case ADMINISTRATIVE_AREA_LEVEL_2:
                                        break;

                                    case COUNTRY:
                                        mCountry.setText(component.long_name);
                                        break;

                                    case POSTAL_CODE:
                                        break;

                                    case POLITICAL:
                                        break;

                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(final Throwable failure) {
                        Log.d("test", "failure " + failure);
                    }
                });
            }
        });
    }


    public void showCheckBox(){
        final CharSequence[] items = {"Celebrity","Company","Government Offical","Police","Politician","Salaried Employee", "Self Entrepreneur", "Sport Person"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Choose Your Category");
        builder.setIcon(R.drawable.test);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
               // Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                mchoose_cat.setText(items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.choose_cat:
                showCheckBox();
                break;

            case R.id.signUp:
                checkValidation();
                break;
//
        }
    }


    // Check Validation Method
    private void checkValidation() {
        // Get all edittext texts

        String getFullName = mName.getText().toString();
        String getEmailId = mEmail.getText().toString();
        String getMobileNumber = mPhone.getText().toString();
        String getArea = mArea.getText().toString();
        String getCity= mCity.getText().toString();
		String getState = mState.getText().toString();
        String getCountry = mCountry.getText().toString();
        String getPassword= mPassword.getText().toString();
        String getCategoty= mchoose_cat.getText().toString();
        String getUserName= mUser_name.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0) {
            Toast.makeText(this,  R.string.all_fields_req, Toast.LENGTH_SHORT).show();
        }

        // Check if email id valid or not
        else if (!m.find())
            Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show();

        else if (!getMobileNumber.matches("^[789]\\d{9}$")) {
            Toast.makeText(this, R.string.invalid_mobile, Toast.LENGTH_SHORT).show();
        }

        else if(getUserName.equals("") || getUserName.length() == 0){
            Toast.makeText(this, "UserName is must !!", Toast.LENGTH_SHORT).show();
        }
        else if (getPassword.equals("") || getPassword.length() == 0) {
            Toast.makeText(this,  R.string.incorrect_pass, Toast.LENGTH_SHORT).show();
        }

        else if (getCategoty.equals("") || getCategoty.length() == 0) {
            Toast.makeText(this, "Choose Category", Toast.LENGTH_SHORT).show();
        }

        else if(getArea.equals("") || getArea.length() == 0
                || getCity.equals("") || getCity.length() == 0
                || getState.equals("") || getState.length() == 0
                || getCountry.equals("") || getCountry.length() == 0){
            Toast.makeText(this, R.string.choose_loc, Toast.LENGTH_SHORT).show();

        }

        else
        {
            getOTP();
        }
    }

    public void getOTP(){

        int  selectgender = gender_radiogroup.getCheckedRadioButtonId();
        gendersm = (RadioButton)findViewById(selectgender);

        if(mCheck_private.isChecked()){
            accountType = "1";
        }
        else  if(!mCheck_private.isChecked())
        {
            accountType = "0";
        }



        RetrofitClient.getWithClient().getOTP(gendersm.getText().toString(),mUser_name.getText().toString(),mName.getText().toString(),mEmail.getText().toString(),mPhone.getText().toString())
                .enqueue(new GlobalCallback<String>(mEmail) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
             String takeRes = response.body().toString();

             if(takeRes.contains("1")){  //Email already exists
              //   Log.e("takeRes","1");
                 Toast.makeText(SignupActivity.this, R.string.email_exists, Toast.LENGTH_SHORT).show();
             }
             else if(takeRes.contains("2")){  //Mobile Number ALready Exists
              //   Log.e("takeRes","2");
                 Toast.makeText(SignupActivity.this, R.string.mobile_num__exists, Toast.LENGTH_SHORT).show();
             }
             else if(takeRes.contains("3")){  //username ALready Exists
                 //   Log.e("takeRes","2");
                 Toast.makeText(SignupActivity.this, "Choose different username", Toast.LENGTH_SHORT).show();
             }
             else if(takeRes.contains("4")){ // Valid User otp
                 Intent i = new Intent(SignupActivity.this,OtpActivity.class);
                 i.putExtra("name",mName.getText().toString());
                 i.putExtra("email",mEmail.getText().toString());
                 i.putExtra("mobile",mPhone.getText().toString());
                 i.putExtra("area",mArea.getText().toString());
                 i.putExtra("city",mCity.getText().toString());
                 i.putExtra("state",mState.getText().toString());
                 i.putExtra("country",mCountry.getText().toString());
                 i.putExtra("password",mPassword.getText().toString());
                 i.putExtra("am",mchoose_cat.getText().toString());
                 i.putExtra("username",mUser_name.getText().toString());
                 i.putExtra("gender",gendersm.getText().toString());
                 i.putExtra("AccountType",accountType);
                 i.putExtra("FirebaseTKN",FireBaseToken);
                 startActivity(i);
                 finish();
             }
             }
        });
    }

}
