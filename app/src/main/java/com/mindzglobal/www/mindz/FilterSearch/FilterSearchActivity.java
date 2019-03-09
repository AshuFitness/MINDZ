package com.mindzglobal.www.mindz.FilterSearch;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.Follow.FollowCustomAdpater;
import com.mindzglobal.www.mindz.Model.FollowListPojo.Followpojo;
import com.mindzglobal.www.mindz.Model.FollowListPojo.GetRecomUser;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.Model.UserNamepojo.UserName;
import com.mindzglobal.www.mindz.Model.UserNamepojo.UserNameListPojo;
import com.mindzglobal.www.mindz.R;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import custom_font.MyTextView;
import retrofit2.Call;
import retrofit2.Response;



public class FilterSearchActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Bind(R.id.auto_userName)
    AutoCompleteTextView mAuto_userName;

    @Bind(R.id.submit)
    Button mSubmit;

    @Bind(R.id.recycler_filter)
    RelativeLayout mRecycler_filter;

    @Bind(R.id.filter_view)
    LinearLayout mFilter_view;

    @Bind(R.id.toolbar)
    RelativeLayout mToolbar;

    private List<UserName> userNameList;

    SessionManager manager;
    String token;
    private  int lastPosition=-1;
    Activity mContext;

    private static RecyclerView.Adapter adapters;
    private static RecyclerView recyclerView;
    public static View.OnClickListener myOnClickListener;
    private RecyclerView.LayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_search_layout);
        ButterKnife.bind(this);

        manager = new SessionManager();
        token = manager.getPreferences(this, Constants.USER_TOKEN);

//        overridePendingTransition(R.anim.from_middle, R.anim.to_middle);

        overridePendingTransition(R.anim.slide_in_top, 0);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        myOnClickListener = new FilterSearchActivity.MyOnClickListener(FilterSearchActivity.this);
        layoutManager = new LinearLayoutManager(FilterSearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mSubmit.setOnClickListener(this);
        mToolbar.setOnClickListener(this);

        city();

        setAnimation(recyclerView,lastPosition);

        mchoose_cat.setOnClickListener(this);

        mAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                mAutocomplete.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(final PlaceDetails details) {
                        //  Log.d("test", "details " + details);
                        maddress_lay.setVisibility(View.GONE);
                        mState.setText("");
                        mCity.setText("");
                        mArea.setText("");
                        mCountry.setText("");

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

    public  class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {

//				Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//				int selectedItemPosition = recyclerView.getChildPosition(v);
//				RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
//				TextView jobtitle = (TextView) viewHolder.itemView.findViewById(R.id.domain);

//				Toast.makeText(context, jobtitle.getText().toString(), Toast.LENGTH_SHORT).show();
//				Intent des = new Intent(FilterSearchActivity.this,JobDiscription.class);
//				checkJobView(jobId.getText().toString(),emp);
//				des.putExtra("jobTitle",jobtitle.getText().toString());
//				startActivity(des);

        }
    }

    public void showCheckBox(){

        final CharSequence[] items = {"Celebrity","Company","Government Offical","Police","Politician","Salaried Employee", "Self Entrepreneur", "Sport Person"};

        AlertDialog.Builder builder = new AlertDialog.Builder(FilterSearchActivity.this);
        builder.setTitle("Choose Category");
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

            case R.id.submit:
                Filtersearch();
                break;

            case R.id.toolbar:
                mState.setText("");
                mAutocomplete.setText("");
                mCity.setText("");
                mArea.setText("");
                mCountry.setText("");
                mchoose_cat.setText("");
                mAuto_userName.setText("");
                mFilter_view.setVisibility(View.VISIBLE);
                mRecycler_filter.setVisibility(View.GONE);
                break;
        }
    }

    public void city(){

        String url = "https://www.homiezin.com/api/get-username/";
        RetrofitClient.getClient().city(url,"application/json","Bearer "+token).enqueue(new GlobalCallback<UserNameListPojo>(mArea) {
            @Override
            public void onResponse(Call<UserNameListPojo> call, Response<UserNameListPojo> response) {

                Log.e("CityResError",response.toString());
                Log.e("CityRes",response.body().toString());

                List<UserName> getuserName = response.body().getUserName();

                userNameList = getuserName;

                //Calling a method to show the list
                showList();
            }
        });
    }

    private void showList(){
        //String array to store all the book names
        String[] items = new String[userNameList.size()];

        //Traversing through the whole list to get all the names
        for(int i=0; i<userNameList.size(); i++){
            //Storing names to string array
            items[i] = userNameList.get(i).getUsrnam();
        }

//        Log.e("Items",items.toString());
        //Creating an array adapter for list view
        ArrayAdapter adapter = new ArrayAdapter<String>(FilterSearchActivity.this,android.R.layout.simple_spinner_dropdown_item,items);
        //Setting adapter to listview
        mAuto_userName.setThreshold(0);//will start working from first character
        mAuto_userName.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
    }

    public void Filtersearch() {

        RetrofitClient.getWithClient().allFollowListFilter(mAuto_userName.getText().toString(),
                                                        mCountry.getText().toString(),
                                                        mState.getText().toString(),
                                                        mCity.getText().toString(),
                                                        mArea.getText().toString(),
                                                        mchoose_cat.getText().toString(),
                                                        "application/json","Bearer "+token)
                .enqueue(new GlobalCallback<Followpojo>(recyclerView) {
                    @Override
                    public void onResponse(Call<Followpojo> call, Response<Followpojo> response) {

                        Log.e("FollowListResponse", response.body().toString());

                        List<GetRecomUser> FollowList = response.body().getGetRecomUser();

//                        adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList);
//                        recyclerView.setAdapter(adapter);
                        if(FollowList.isEmpty()){
//					emptyElement.setVisibility(View.VISIBLE);
                            mFilter_view.setVisibility(View.VISIBLE);
                            mRecycler_filter.setVisibility(View.GONE);
                        }

                        else {
                            mFilter_view.setVisibility(View.GONE);
                            mRecycler_filter.setVisibility(View.VISIBLE);
                            adapters = new FollowCustomAdpater((ArrayList<GetRecomUser>) FollowList,FilterSearchActivity.this);
                            recyclerView.setAdapter(adapters);
                        }
                    }
                });
    }


    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition )
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.android_rotate_animation);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
