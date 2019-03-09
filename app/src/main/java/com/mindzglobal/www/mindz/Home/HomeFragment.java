package com.mindzglobal.www.mindz.Home;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mindzglobal.www.mindz.Configuration.Config;
import com.mindzglobal.www.mindz.ConstantAndSession.Constants;
import com.mindzglobal.www.mindz.ConstantAndSession.SessionManager;
import com.mindzglobal.www.mindz.CropMain.CropMainActivity;
import com.mindzglobal.www.mindz.CropMain.CropingOption;
import com.mindzglobal.www.mindz.CropMain.CropingOptionAdapter;
import com.mindzglobal.www.mindz.Follow.FollowActivity;
import com.mindzglobal.www.mindz.Home.AreaProblem.AreaProblemActivity;
import com.mindzglobal.www.mindz.Home.CrimeFeeds.CrimeActivity;
import com.mindzglobal.www.mindz.Home.CrimeFeeds.CrimeCustomAdpater;
import com.mindzglobal.www.mindz.Home.FunActivity.FunActivities;
import com.mindzglobal.www.mindz.Home.Opportunities.OpportunitiesActivity;
import com.mindzglobal.www.mindz.Home.Others.OthersActivity;
import com.mindzglobal.www.mindz.Home.Political.PoliticalActivity;
import com.mindzglobal.www.mindz.Home.Sports.SportsActivity;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.GetFeedx;
import com.mindzglobal.www.mindz.Model.FeedsCrimePojo.feedscrimepojo;
import com.mindzglobal.www.mindz.Model.GlobalCallback;
import com.mindzglobal.www.mindz.Model.RetrofitClient;
import com.mindzglobal.www.mindz.Profile.ProfileActivity;
import com.mindzglobal.www.mindz.R;
import com.mindzglobal.www.mindz.fragments.CameraFragmentMainActivity;
import com.mindzglobal.www.mindz.fragments.CreatePostActivity_New;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.mindzglobal.www.mindz.R.drawable.profile_img;

public class HomeFragment extends Fragment implements View.OnClickListener,AppBarLayout.OnOffsetChangedListener {

	CircleImageView post_prof_img;
	TextView post_text;
	ImageView post_imgvid;

	private static RecyclerView.Adapter adapter;
	private static RecyclerView recyclerView;
	public static View.OnClickListener myOnClickListener;
	private RecyclerView.LayoutManager layoutManager;
	EditText t;
	ProgressBar bar;
	private File outPutFile = null;
    private ImageView imageView;
	String token,user_pic;
	SessionManager manager;
	RetrofitClient retrofitClient;
	private Uri mImageCaptureUri;

	String FireBaseToken;

	private final static int REQUEST_PERMISSION_REQ_CODE = 34;
	private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;

	private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
	private boolean mIsAvatarShown = true;

	private int mMaxScrollSize ;

	GridView grid;
	String[] web = {
			"Crime",
			"Area Problem",
			"Fun Activity",
			"Opportunities",
			"Politics",
			"Sports",
			"Others",
			"Profile"
	};

	int[] imageId = {
			R.drawable.crime_homepage,
			R.drawable.areaproblem_homepage,
			R.drawable.funactivity_homepage,
			R.drawable.opportunity_homepage,
			R.drawable.political_homepage,
			R.drawable.sports_homepage,
			R.drawable.others_homepage,
			R.drawable.profile_homepage,
	};

	View rootView;
	TextView find_recruiter;

	RelativeLayout emptyElement;
    Bitmap photo;

	SwipeRefreshLayout mSwipeRefreshLayout;
	Context mContext;
	Toolbar toolbar;
    String imagess;
    String cam_pref,profile_pref;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.activity_home, container, false);

		FireBaseToken = FirebaseInstanceId.getInstance().getToken();

		post_prof_img = rootView.findViewById(R.id.post_profile);
		post_text = rootView.findViewById(R.id.post_text);
		post_imgvid = rootView.findViewById(R.id.post_img_vid);

		emptyElement = rootView.findViewById(R.id.empty_element);

		bar = rootView.findViewById(R.id.progressBar);

		AppBarLayout appbarLayout =  rootView.findViewById(R.id.materialup_appbar);
		appbarLayout.addOnOffsetChangedListener(this);
		mMaxScrollSize = appbarLayout.getTotalScrollRange();

		retrofitClient = new RetrofitClient(token);
		manager = new SessionManager();
		token = manager.getPreferences(getActivity(), Constants.USER_TOKEN);
		user_pic = manager.getPreferences(getActivity(), Constants.USER_PROFILE_PIC);

		getActivity().overridePendingTransition(R.anim.from_middle, R.anim.to_middle);

		new Thread(new Runnable() {
			public void run() {

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Glide.with(getActivity()).load(Config.BASE_URL_MEDIA+user_pic)
								.centerCrop()
								.into(post_prof_img);
					}
				});
			}
		}).start();

		recyclerView = rootView.findViewById(R.id.recycler_view);
		recyclerView.setHasFixedSize(true);
		myOnClickListener = new MyOnClickListener(getActivity());
		layoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());

		post_prof_img.setOnClickListener(this);
		post_text.setOnClickListener(this);
		post_imgvid.setOnClickListener(this);

		CustomGrid adapter = new CustomGrid(getActivity(), web, imageId);
		grid=rootView.findViewById(R.id.grid);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				switch (position){
					case 0:
						Intent is = new Intent(getActivity(), CrimeActivity.class);
						startActivity(is);
						break;
					case 1:
						Intent ar = new Intent(getActivity(), AreaProblemActivity.class);
						startActivity(ar);
						break;
					case 2:
						Intent fa = new Intent(getActivity(), FunActivities.class);
						startActivity(fa);
						break;

					case 3:
						Intent opp = new Intent(getActivity(), OpportunitiesActivity.class);
						startActivity(opp);
						break;

					case 4:
						Intent poli = new Intent(getActivity(), PoliticalActivity.class);
						startActivity(poli);
						break;

					case 5:
						Intent spor = new Intent(getActivity(), SportsActivity.class);
						startActivity(spor);
						break;

					case 6:
						Intent oth = new Intent(getActivity(), OthersActivity.class);
						startActivity(oth);
						break;

					case 7:
						Intent pro = new Intent(getActivity(), ProfileActivity.class);
						startActivity(pro);
						break;

				}
			}
		});

		allFeeds();

		mSwipeRefreshLayout = rootView.findViewById(R.id.swipeContainer);
		mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_context_menu_text_red);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				allFeeds();
				mSwipeRefreshLayout.setRefreshing(false);
			}
		});

		final ImageView imgView= rootView.findViewById(R.id.imageView);

		Glide.with(this)
				.load(R.drawable.binoculars)
				.asGif()
				.animate(R.drawable.binoculars)
				.crossFade()
				.fitCenter()
				.into(imgView);

		find_recruiter =  rootView.findViewById(R.id.find_people);
		find_recruiter.setOnClickListener(this);


		cam_pref=manager.getPreferences(getActivity(),Constants.CAMERA_STATUS);
		if (!cam_pref.contains("1")){
			tapTargetCam();
		}



		return rootView;
	}



	public void tapTargetCam(){

		final Display display = getActivity().getWindowManager().getDefaultDisplay();

		final Drawable droid = ContextCompat.getDrawable(getActivity(), R.drawable.cam_photo_primary);
		final Drawable droid_profile = ContextCompat.getDrawable(getActivity(), R.drawable.profile_img);

		final Rect droidTarget = new Rect(0, 0, droid.getIntrinsicWidth() * 2, droid.getIntrinsicHeight() * 2);
		final Rect droidTarget_profile = new Rect(0, 0, droid_profile.getIntrinsicWidth() * 2, droid_profile.getIntrinsicHeight() * 2);

		droidTarget.offset(display.getWidth() / 2, display.getHeight() / 2);
		droidTarget_profile.offset(display.getWidth() / 2, display.getHeight() / 2);

		final SpannableString sassyDesc = new SpannableString("It allows you to go back, sometimes");
		sassyDesc.setSpan(new StyleSpan(Typeface.ITALIC), sassyDesc.length() - "sometimes".length(), sassyDesc.length(), 0);

		final TapTargetSequence sequence = new TapTargetSequence(getActivity())
				.targets(

//						TapTarget.for View(rootView.findViewById(R.id.post_img_vid),"camera","Explore more with Camera").id(1),
						TapTarget.forView(rootView.findViewById(R.id.post_profile), "profile", "Explore more with Profile")
								.id(2)
								.icon(droid_profile)

				).listener(new TapTargetSequence.Listener() {
					@Override
					public void onSequenceFinish() {
//						((TextView) rootView.findViewById(R.id.educated)).setText("Congratulations! You're educated now!");
					}

					@Override
					public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
						Log.d("TapTargetView", "Clicked on " + lastTarget.id());
					}

					@Override
					public void onSequenceCanceled(TapTarget lastTarget) {
						final AlertDialog dialog = new AlertDialog.Builder(getActivity())
								.setTitle("Uh oh")
								.setMessage("You canceled the sequence")
								.setPositiveButton("Oops", null).show();
						TapTargetView.showFor(dialog,
								TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You canceled the sequence at step " + lastTarget.id())
										.cancelable(false)
										.tintTarget(false), new TapTargetView.Listener() {
									@Override
									public void onTargetClick(TapTargetView view) {
										super.onTargetClick(view);
										dialog.dismiss();
									}
								});
					}
				});

		final SpannableString spannedDesc = new SpannableString("Explore more with Camera");
//		spannedDesc.subSequence(spannedDesc.length() - "TapTargetView".length(), spannedDesc.length());
		TapTargetView.showFor(getActivity(), TapTarget.forView(rootView.findViewById(R.id.post_img_vid), "Camera", spannedDesc)
				.cancelable(false)
				.drawShadow(true)
				.icon(droid)
				.titleTextDimen(R.dimen.title_text_size)
				.tintTarget(false), new TapTargetView.Listener() {
			@Override
			public void onTargetClick(TapTargetView view) {
				super.onTargetClick(view);
				sequence.start();
				manager.setPreferences(getActivity(),Constants.CAMERA_STATUS,"1");
			}

			@Override
			public void onOuterCircleClick(TapTargetView view) {
				super.onOuterCircleClick(view);
//				Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
				Log.d("TapTargetViewSample", "You dismissed me :(");
			}
		});
	}







	public void allFeeds() {
		bar.setVisibility(View.VISIBLE);
		String url = "https://www.homiezin.com/api/feed/Crime/";

		RetrofitClient.getWithClient().allFeeds(url,"application/json","Bearer "+token)
				.enqueue(new GlobalCallback<feedscrimepojo>(recyclerView) {
			@Override
			public void onResponse(Call<feedscrimepojo> call, Response<feedscrimepojo> response) {

				bar.setVisibility(View.GONE);
				List<GetFeedx> FeedCrimeList = response.body().getGetFeedx();

//                        adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList);
//                        recyclerView.setAdapter(adapter);
				if(FeedCrimeList.isEmpty()){
					emptyElement.setVisibility(View.VISIBLE);
				}

				else {
					emptyElement.setVisibility(View.GONE);
					recyclerView.setVisibility(View.VISIBLE);
					adapter = new CrimeCustomAdpater((ArrayList<GetFeedx>) FeedCrimeList,getActivity());
					recyclerView.setAdapter(adapter);
				}
			}
		});
	}

	public static void start(Context c) {
		c.startActivity(new Intent(c, HomeFragment.class));
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

		if (mMaxScrollSize == 0)
			mMaxScrollSize = appBarLayout.getTotalScrollRange();

		int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

		if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
			mIsAvatarShown = false;

//			mProfileImage.animate()
//				.scaleY(0).scaleX(0)
//				.setDuration(200)
//				.start();

		}

		if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
			mIsAvatarShown = true;

//			mProfileImage.animate()
//				.scaleY(1).scaleX(1)
//				.start();

		}

	}

	public  class MyOnClickListener implements View.OnClickListener {

		private final Context context;

		private MyOnClickListener(Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View v) {

//			Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//			int selectedItemPosition = recyclerView.getChildPosition(v);
//			RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(selectedItemPosition);
//			TextView jobtitle = (TextView) viewHolder.itemView.findViewById(R.id.domain);
//			TextView companyName = (TextView) viewHolder.itemView.findViewById(R.id.company_name);
//			TextView experience = (TextView) viewHolder.itemView.findViewById(R.id.experience);
//			TextView location = (TextView) viewHolder.itemView.findViewById(R.id.location);
//			TextView skills = (TextView) viewHolder.itemView.findViewById(R.id.skills);
//			TextView date = (TextView) viewHolder.itemView.findViewById(R.id.date);
//			TextView salary = (TextView) viewHolder.itemView.findViewById(R.id.salary);
//			TextView jobdes = (TextView) viewHolder.itemView.findViewById(R.id.job_des);
//			TextView compType = (TextView) viewHolder.itemView.findViewById(R.id.comp_type);
//			TextView funcArea = (TextView) viewHolder.itemView.findViewById(R.id.function_are);
//			TextView jobRole = (TextView) viewHolder.itemView.findViewById(R.id.job_role);
//			TextView recuriterName = (TextView) viewHolder.itemView.findViewById(R.id.recuriter_name);
//			TextView jobVideo = (TextView) viewHolder.itemView.findViewById(R.id.job_video);
//			TextView education = (TextView) viewHolder.itemView.findViewById(R.id.education);
//			TextView jobId = (TextView) viewHolder.itemView.findViewById(R.id.job_id);
//			CheckBox saveJob = (CheckBox) viewHolder.itemView.findViewById(R.id.item_card_check_favorite);
//			TextView industry = (TextView) viewHolder.itemView.findViewById(R.id.industry);
//			TextView city = (TextView) viewHolder.itemView.findViewById(R.id.city);
//			TextView who_shod_apply = (TextView) viewHolder.itemView.findViewById(R.id.who_shod_apply);
//			TextView view_applied = (TextView) viewHolder.itemView.findViewById(R.id.view_applied);
//			TextView no_position = (TextView) viewHolder.itemView.findViewById(R.id.no_position);


//			Toast.makeText(context, jobtitle.getText().toString(), Toast.LENGTH_SHORT).show();
//			Intent des = new Intent(JobsAllActivity.this,JobDiscription.class);
//			checkJobView(jobId.getText().toString(),emp);
//			des.putExtra("jobTitle",jobtitle.getText().toString());
//			des.putExtra("companyName",companyName.getText().toString());
//			des.putExtra("experience",experience.getText().toString());
//			des.putExtra("location",location.getText().toString());
//			des.putExtra("skills",skills.getText().toString());
//			des.putExtra("date",date.getText().toString());
//			des.putExtra("salary",salary.getText().toString());
//			des.putExtra("jobdesc",jobdes.getText().toString());
//			des.putExtra("compType",compType.getText().toString());
//			des.putExtra("funcArea",funcArea.getText().toString());
//			des.putExtra("jobRole",jobRole.getText().toString());
//			des.putExtra("recuriterName",recuriterName.getText().toString());
//			des.putExtra("jobVideo",jobVideo.getText().toString());
//			des.putExtra("education",education.getText().toString());
//			des.putExtra("jobId",jobId.getText().toString());
//			des.putExtra("industry",industry.getText().toString());
//			des.putExtra("who_shod_apply",who_shod_apply.getText().toString());
//			des.putExtra("city",city.getText().toString());
//			des.putExtra("viewapplied",view_applied.getText().toString());
//			des.putExtra("NoPosition",no_position.getText().toString());
//			startActivity(des);
		}
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()){

			case R.id.post_profile:
				Intent i = new Intent(getActivity(),ProfileActivity.class);
				startActivity(i);
				break;

			case R.id.post_text:
				Intent it = new Intent(getActivity(),CreatePostActivity_New.class);
				startActivity(it);
				break;

			case R.id.post_img_vid:
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				String authorities = getActivity().getPackageName() + ".fileprovider";
//				mImageCaptureUri = FileProvider.getUriForFile(getActivity(), authorities, outPutFile);
//				intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
				startActivityForResult(intent, CAMERA_CODE);
				break;

//			TakePhotoActivity.startCameraFromLocation(startingLocation, getActivity());
//			int[] startingLocation = new int[2];
//			post_imgvid.getLocationOnScreen(startingLocation);
//			startingLocation[0] += post_imgvid.getWidth() / 2;
//			CameraFragmentMainActivity.startCameraFromLocation(startingLocation, getActivity());
//				overridePendingTransition(0, 0);
//				Intent iiv = new Intent(getActivity(),TakePhotoActivity.class);
//				startActivity(iiv);
//				break;

			case R.id.find_people:
				Intent iff = new Intent(getActivity(),FollowActivity.class);
				startActivity(iff);
				break;

		}
	}

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }

//		if (!cam_pref.contains("1")){
//			tapTargetCam();
//		}

	}

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

            mImageCaptureUri = data.getData();
            System.out.println("Gallery Image URI : "+mImageCaptureUri);
            CropingIMG();

        } else if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {

            System.out.println("Camera Image URI : "+mImageCaptureUri);
            CropingIMG();
//                 CropingCameraIMG();
        } else if (requestCode == CROPING_CODE) {

            try {
                if(outPutFile.exists()){
                    photo = decodeFile(outPutFile);
                    imagess = getStringImage(photo);
                    imageView.setImageBitmap(photo);
                }
                else {
                    Toast.makeText(getActivity(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void CropingIMG() {
        final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities( intent, 0 );
        int size = list.size();
        if (size == 0) {
            Toast.makeText(getActivity(), "Cann't find image croping app", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);

            //TODO: don't use return-data tag because it's not return large image data and crash not given any message
            //intent.putExtra("return-data", true);

            //Create output file here
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1) {
                Intent i   = new Intent(intent);
                ResolveInfo res =  list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROPING_CODE);
            } else {
                for (ResolveInfo res : list) {
                    final CropingOption co = new CropingOption();

                    co.title  = getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon  = getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);
                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                }

//                CropingOptionAdapter adapter = new CropingOptionAdapter(getActivity(), cropOptions);

//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
//                builder.setTitle("Choose Croping App");
//                builder.setCancelable(false);
//                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
//                    public void onClick( DialogInterface dialog, int item ) {
//                        startActivityForResult( cropOptions.get(item).appIntent, CROPING_CODE);
//                    }
//                });

//                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel( DialogInterface dialog ) {
//
//                        if (mImageCaptureUri != null ) {
//                            getActivity().getContentResolver().delete(mImageCaptureUri, null, null );
//                            mImageCaptureUri = null;
//                        }
//                    }
//                } );

//                android.app.AlertDialog alert = builder.create();
//                alert.show();
            }
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


}




//	private static final String TAG = HomeFragment.class.getSimpleName();
//	private PostsAdapter postsAdapter;
////	private RecyclerView recyclerView;
//	private FloatingActionButton floatingActionButton;
//	private ProfileManager profileManager;
//	private PostManager postManager;
//	private int counter;
//	private TextView newPostsCounterTextView;
//	private PostManager.PostCounterWatcher postCounterWatcher;
//	private boolean counterAnimationInProgress = false;