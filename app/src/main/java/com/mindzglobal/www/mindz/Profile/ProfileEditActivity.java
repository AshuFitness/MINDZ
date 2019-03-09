package com.mindzglobal.www.mindz.Profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.R;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import butterknife.Bind;
import butterknife.ButterKnife;
import custom_font.MyEditText;
import custom_font.MyTextView;
import retrofit2.Call;
import retrofit2.Response;


public class ProfileEditActivity extends AppCompatActivity implements  View.OnClickListener {

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

    @Bind(R.id.edit_name)
    MyEditText mName;

    @Bind(R.id.edit_dob)
    MyEditText mDOB;

    @Bind(R.id.edit_pan)
    MyEditText mPan;

    @Bind(R.id.edit_adhar)
    MyEditText mAdhar;

    @Bind(R.id.choose_cat)
    MyTextView mchoose_cat;

    @Bind(R.id.submit)
    TextView mSubmit;

    @Bind(R.id.check_private)
    CheckBox mCheck_private;

    @Bind(R.id.gender_rgroup)
    RadioGroup gender_radiogroup;

    @Bind(R.id.male_radiobtn)
    RadioButton male;

    @Bind(R.id.female_radiobtn)
    RadioButton female;

    RadioButton gendersm;

    SessionManager manager;
    String token,accountType,name,area,city,state,country,iam,accType,gender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_edit_profile);
        ButterKnife.bind(this);
        manager = new SessionManager();

        token = manager.getPreferences(ProfileEditActivity.this, Constants.USER_TOKEN);
        name = manager.getPreferences(ProfileEditActivity.this, Constants.USER_NAME);
        area = manager.getPreferences(ProfileEditActivity.this, Constants.USER_AREA);
        city = manager.getPreferences(ProfileEditActivity.this, Constants.USER_CITY);
        state = manager.getPreferences(ProfileEditActivity.this, Constants.USER_STATE);
        country = manager.getPreferences(ProfileEditActivity.this, Constants.USER_COUNTRY);
        iam = manager.getPreferences(ProfileEditActivity.this, Constants.USER_PRO_CAT);
        accType = manager.getPreferences(ProfileEditActivity.this, Constants.USER_ACC_TYPE);
        gender = manager.getPreferences(ProfileEditActivity.this, Constants.USER_GENDER);

        Bundle bundle = getIntent().getExtras();
        String dob = bundle.getString("dob");
        String panCard = bundle.getString("panCard");
        String adharCard = bundle.getString("adharCard");


        if(accType.contains("0")){
            mCheck_private.setChecked(false);
        }
        if(accType.contains("1")){
            //private
            mCheck_private.setChecked(true);
        }

        if(gender.contains("Male")){
            male.setChecked(true);
        }
        if(gender.contains("Female")){
            female.setChecked(true);
        }

        mName.setText(name);
        mDOB.setText(dob);
        mPan.setText(panCard);
        mchoose_cat.setText(iam);
        mArea.setText(area);
        mCity.setText(city);
        mState.setText(state);
        mCountry.setText(country);
        mAdhar.setText(adharCard);
        mSubmit.setOnClickListener(this);
        mchoose_cat.setOnClickListener(this);


        mAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                mAutocomplete.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(final PlaceDetails details) {
                        //  Log.d("test", "details " + details);
//                        maddress_lay.setVisibility(View.VISIBLE);
                        mState.setText("");
                        mCity.setText("");
                        mArea.setText("");
                        mCountry.setText("");
//
                        for (AddressComponent component : details.address_components) {
                            for (AddressComponentType type : component.types) {
                                switch (type) {
                                    case STREET_NUMBER:

                                        break;
                                    case ROUTE:

                                        break;
                                    case NEIGHBORHOOD:
                                        Log.e("NEIGHBORHOOD",component.long_name);
                                        break;
                                    case SUBLOCALITY_LEVEL_1:
                                        Log.e("SUBLOCALITY_LEVEL_1",component.long_name);
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

                                    case ADMINISTRATIVE_AREA_LEVEL_3:
                                        Log.e("ADMIN_AREA_LEVEL_3",component.long_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_4:
                                        Log.e("ADMIN_AREA_LEVEL_4",component.long_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_5:
                                        Log.e("ADMIN_AREA_LEVEL_5",component.long_name);
                                        break;
                                    case COLLOQUIAL_AREA:
                                        Log.e("COLLOQUIAL_AREA",component.long_name);
                                        break;
                                    case GEOCODE:
                                        Log.e("GEOCODE",component.long_name);
                                        break;
                                    case INTERSECTION:
                                        Log.e("INTERSECTION",component.long_name);
                                        break;
                                    case NATURAL_FEATURE:
                                        Log.e("NATURAL_FEATURE",component.long_name);
                                        break;
                                    case POINT_OF_INTEREST:
                                        Log.e("POINT_OF_INTEREST",component.long_name);
                                        break;
                                    case SUBLOCALITY_LEVEL_2:
                                        Log.e("SUBLOCALITY_LEVEL_2",component.long_name);
                                        break;

                                    case SUBLOCALITY_LEVEL_3:
                                        Log.e("SUBLOCALITY_LEVEL_3",component.long_name);
                                        break;

                                    case SUBLOCALITY_LEVEL_4:
                                        Log.e("SUBLOCALITY_LEVEL_4",component.long_name);
                                        break;

                                    case SUBLOCALITY_LEVEL_5:
                                        Log.e("SUBLOCALITY_LEVEL_5",component.long_name);
                                        break;

                                    case TRANSIT_STATION:
                                        Log.e("TRANSIT_STATION",component.long_name);
                                        break;

                                    case SUBPREMISE:

                                        Log.e("SUBPREMISE",component.long_name);
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

    private void checkValidation() {
        // Get all edittext texts
        String getFullName = mName.getText().toString();
        String getArea = mArea.getText().toString();
        String getCity= mCity.getText().toString();
        String getState = mState.getText().toString();
        String getCountry = mCountry.getText().toString();
        String getDOB= mDOB.getText().toString();
        String getPan= mPan.getText().toString();
        String getAdhar= mAdhar.getText().toString();
        String getCategoty= mchoose_cat.getText().toString();

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0) {
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
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
            postEditData();
        }

    }

    public void postEditData(){
        String getFullName = mName.getText().toString();
        String getArea = mArea.getText().toString();
        String getCity= mCity.getText().toString();
        String getState = mState.getText().toString();
        String getCountry = mCountry.getText().toString();
        String getDOB= mDOB.getText().toString();
        String getPan= mPan.getText().toString();
        String getAdhar= mAdhar.getText().toString();
        String getCategoty= mchoose_cat.getText().toString();
        Log.e("token",token);
        if(mCheck_private.isChecked()){
            accountType = "1";
        }
        else  if(!mCheck_private.isChecked())
        {
            accountType = "0";
        }
        int  selectgender = gender_radiogroup.getCheckedRadioButtonId();
        gendersm = findViewById(selectgender);

        RetrofitClient.getWithClient().Editprofile(accountType,gendersm.getText().toString(),getFullName,getCategoty,getCountry,getState,getCity,
                getArea,getDOB,getPan,getAdhar,"application/json","Bearer "+token).enqueue(new GlobalCallback<String>(mName) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//            Log.e("Editres",response.toString());

                String Editres = response.body().toString();
                if(Editres.contains("1")){
                    Toast.makeText(ProfileEditActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                }
                else if(Editres.contains("2")){
                    Toast.makeText(ProfileEditActivity.this, "UnSuccessfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.submit:
                checkValidation();
                break;

            case R.id.choose_cat:
                showCheckBox();
                break;
        }
    }

    public void showCheckBox(){
        final CharSequence[] items = {"Celebrity","Company","Government Offical","Police","Politician","Salaried Employee","Self Enterpreuer", "Sport Person"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEditActivity.this);
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
}
