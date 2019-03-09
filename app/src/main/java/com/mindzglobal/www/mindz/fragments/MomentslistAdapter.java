//package com.mindzglobal.www.mindz.fragments;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.animation.AnticipateInterpolator;
//import android.view.animation.OvershootInterpolator;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.makeramen.roundedimageview.RoundedImageView;
//import com.ogaclejapan.arclayout.ArcLayout;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import in.tagteen.tagteen.BuzzPreviewActivity;
//import in.tagteen.tagteen.CommentLikeActivity;
//import in.tagteen.tagteen.Fragments.BuzzPreviewActivity_new;
//import in.tagteen.tagteen.Fragments.MomentsFeed;
//import in.tagteen.tagteen.Fragments.share.ShareDialog;
//import in.tagteen.tagteen.Model.GetReactionModel;
//import in.tagteen.tagteen.Model.InsertCoolModel;
//import in.tagteen.tagteen.Model.LikeJsonInputModel;
//import in.tagteen.tagteen.Model.ReactionInputJson;
//import in.tagteen.tagteen.Model.SectionDataModel;
//import in.tagteen.tagteen.MomentFeedVideoPlay;
//import in.tagteen.tagteen.R;
//import in.tagteen.tagteen.VideoPartDetail;
//import in.tagteen.tagteen.apimodule_retrofit.API_Call_Retrofit;
//import in.tagteen.tagteen.apimodule_retrofit.Apimethods;
//import in.tagteen.tagteen.apimodule_retrofit.CommonApicallModule;
//import in.tagteen.tagteen.configurations.RegistrationConstants;
//import in.tagteen.tagteen.configurations.RequestConstants;
//import in.tagteen.tagteen.networkEngine.AsyncResponse;
//import in.tagteen.tagteen.networkEngine.AsyncWorker;
//import in.tagteen.tagteen.profile.AboutFragment;
//import in.tagteen.tagteen.util.Utils;
//import in.tagteen.tagteen.utils.AnimatorUtils;
//import in.tagteen.tagteen.utils.DateTimeCalculation;
//import in.tagteen.tagteen.utils.FontStyles;
//import in.tagteen.tagteen.utils.MyBounceInterpolator;
//import in.tagteen.tagteen.workers.SharedPreferenceSingleton;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import tyrantgit.explosionfield.ExplosionField;
//
//import static in.tagteen.tagteen.configurations.ServerConnector.REQUEST_DELETE_POST;
//import static in.tagteen.tagteen.configurations.ServerConnector.REQUEST_REPORT_POST;
//
//
//public class MomentslistAdapter extends RecyclerView.Adapter<MomentslistAdapter.ItemRowHolder>{
//    final Animation myAnim, Anim2;
//    private ArrayList<SectionDataModel> dataList;
//    private Context mContext;
//    LikeJsonInputModel likeJsonInputModel = new LikeJsonInputModel();
//    Bundle bundle = new Bundle();
//    MomentsFeed momentsFeed;
//    AboutFragment aboutFragment;
//    int flag=0;
//    final String logedInUserid = SharedPreferenceSingleton.getInstance().getStringPreference(RegistrationConstants.USER_ID);
//    String Accesstoken = SharedPreferenceSingleton.getInstance().getStringPreference(RegistrationConstants.TOKEN);
//
//    public MomentslistAdapter(Context context, ArrayList<SectionDataModel> dataList, MomentsFeed momentsFeed,int flag) {
//        this.dataList = dataList;
//        this.mContext = context;
//        myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
//        Anim2 = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
//        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
//        myAnim.setInterpolator(interpolator);
//        Anim2.setInterpolator(interpolator);
//        this.momentsFeed = momentsFeed;
//        this.flag=flag;
//    }
//
//    public MomentslistAdapter(Context context, ArrayList<SectionDataModel> dataList, AboutFragment aboutFragment,int flag) {
//        this.dataList = dataList;
//        this.mContext = context;
//        myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
//        Anim2 = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
//        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
//        myAnim.setInterpolator(interpolator);
//        Anim2.setInterpolator(interpolator);
//        this.aboutFragment = aboutFragment;
//        this.flag=flag;
//    }
//
//    @Override
//    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.momentslist_items, null);
//        ItemRowHolder mh = new ItemRowHolder(v);
//        return mh;
//    }
//
//    @Override
//    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int i) {
//        final String postUserid = dataList.get(i).getPost_userid();
//        itemRowHolder.moreoption.setTag(true);
//        itemRowHolder.more_view.setVisibility(View.GONE);
//        itemRowHolder.layoutMain.setVisibility(View.VISIBLE);
//        if(dataList.get(i).isShare_to()){
//            itemRowHolder.relFeedThumb.setVisibility(View.GONE);
//            itemRowHolder.relFeedThumb_share.setVisibility(View.VISIBLE);
//           /* if(dataList.get(i).getOwner_post_type_id().equalsIgnoreCase("3")) {
//                itemRowHolder.itemTitle.setText(dataList.get(i).getPost_creator_name() + " shared a showroom video");
//            }else if(dataList.get(i).getOwner_post_type_id().equalsIgnoreCase("2")) {
//                itemRowHolder.itemTitle.setText(dataList.get(i).getPost_creator_name() + " shared a teenfeed article");
//            }else {
//                itemRowHolder.itemTitle.setText(dataList.get(i).getPost_creator_name() + " shared a moments post");
//            }*/
//            itemRowHolder.owner_name.setText(dataList.get(i).getOwner_post_creator_name());
//            itemRowHolder.tagged_number_owner_creator.setText(dataList.get(i).getOwner_tagged_number());
//            Utils.loadProfilePic(mContext, itemRowHolder.owner_pic, dataList.get(i).getOwner_post_creator_profilepic());
//            if (dataList.get(i).getPost_image_createdby_creator_url().isEmpty() || dataList.get(i).getPost_image_createdby_creator_url() == null) {
//                if (dataList.get(i).getPost_video_thumb_createdby_creator().isEmpty()) {
//                    itemRowHolder.owner_post.setVisibility(View.GONE);
//                } else {
//                    itemRowHolder.owner_post.setVisibility(View.VISIBLE);
//                    itemRowHolder.owner_post.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    Glide.with(mContext).load(dataList.get(i).getPost_video_thumb_createdby_creator().get(0)).fitCenter().into(itemRowHolder.owner_post);
//                }
//            } else {
//                itemRowHolder.owner_post.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                Glide.with(mContext).load(dataList.get(i).getPost_image_createdby_creator_url().get(0)).fitCenter().into(itemRowHolder.owner_post);
//            }
//        }else {
//            itemRowHolder.relFeedThumb.setVisibility(View.VISIBLE);
//            itemRowHolder.relFeedThumb_share.setVisibility(View.GONE);
//            itemRowHolder.itemTitle.setText(dataList.get(i).getPost_creator_name());
//            if (dataList.get(i).getPost_image_createdby_creator_url().isEmpty() || dataList.get(i).getPost_image_createdby_creator_url() == null) {
//
//                if (dataList.get(i).getPost_video_thumb_createdby_creator().isEmpty() ||dataList.get(i).getPost_video_thumb_createdby_creator()==null) {
//                    //itemRowHolder.postImage.setVisibility(View.GONE);
//                    itemRowHolder.relFeedThumb.setVisibility(View.GONE);
//                } else {
//                    itemRowHolder.postImage.setVisibility(View.VISIBLE);
//                    itemRowHolder.postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    Glide.with(mContext).load(dataList.get(i).getPost_video_thumb_createdby_creator().get(0)).fitCenter().into(itemRowHolder.postImage);
//                }
//                itemRowHolder.total_image.setVisibility(View.GONE);
//            } else {
//                itemRowHolder.postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                try {
//                    Glide.with(mContext).load(dataList.get(i).getPost_image_createdby_creator_url().get(0)).fitCenter().into(itemRowHolder.postImage);
//                    if (dataList.get(i).getPost_image_createdby_creator_url().size() == 1) {
//                        itemRowHolder.total_image.setVisibility(View.GONE);
//                    } else {
//                        itemRowHolder.total_image.setText("1/" + dataList.get(i).getPost_image_createdby_creator_url().size());
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        itemRowHolder.taggednumber.setText(dataList.get(i).getPosted_creator_tagged_number());
//        Utils.loadProfilePic(mContext, itemRowHolder.im, dataList.get(i).getPost_creator_profilepic());
//        if (dataList.get(i).getText_description().equalsIgnoreCase("") || dataList.get(i).getText_description().equalsIgnoreCase(" ") || dataList.get(i).getText_description() == null) {
//            itemRowHolder.txt_descriptn.setVisibility(View.GONE);
//            itemRowHolder.owner_post_title.setVisibility(View.GONE);
//        } else {
//            if(dataList.get(i).isShare_to()){
//                itemRowHolder.owner_post_title.setVisibility(View.VISIBLE);
//                itemRowHolder.owner_post_title.setText(dataList.get(i).getText_description());
//            }else {
//                itemRowHolder.txt_descriptn.setVisibility(View.VISIBLE);
//                itemRowHolder.txt_descriptn.setText(dataList.get(i).getText_description());
//            }
//        }
//
//        itemRowHolder.im.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.gotoProfile(mContext,dataList.get(i).getPost_userid());
//            }
//        });
//        itemRowHolder.owner_pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.gotoProfile(mContext,dataList.get(i).getOwner_post_creator_id());
//            }
//        });
//
//        if (dataList.get(i).getPost_image_createdby_creator_url().size() > 1) {
//            itemRowHolder.total_image.postDelayed(new Runnable() {
//                public void run() {
//                    itemRowHolder.total_image.setVisibility(View.GONE);
//                }
//            }, 1000);
//        }
//
//        itemRowHolder.timeset.setText(Utils.getRelativeTime(dataList.get(i).getPost_created_date_time()));
//
//        if (dataList.get(i).getCommentcount() >= 1) {
//            itemRowHolder.textCommentcount.setVisibility(View.VISIBLE);
//            itemRowHolder.textCommentcount.setText("View" + dataList.get(i).getCommentcount() + " Comment");
//        } else {
//            itemRowHolder.textCommentcount.setVisibility(View.GONE);
//        }
//
//
//        int likeCount = dataList.get(i).getLikecount();
//        int coolcount = dataList.get(i).getCoolcount();
//        int swegcount = dataList.get(i).getSwegcount();
//        int nerdcount = dataList.get(i).getNerdcount();
//        int dabcount = dataList.get(i).getDabcount();
//        if(coolcount ==0 &&swegcount==0&&nerdcount==0&&dabcount==0&&likeCount==0){
//            itemRowHolder.headrCount.setVisibility(View.GONE);
//        }else if (coolcount ==1 ||swegcount==1||nerdcount==1||dabcount==1||likeCount==1) {
//            itemRowHolder.headrCount.setVisibility(View.VISIBLE);
//            itemRowHolder.headrCount.setText("1" + " react");
//        } else if (coolcount > 1 ||swegcount>1||nerdcount>1||dabcount>1||likeCount>1) {
//            itemRowHolder.headrCount.setVisibility(View.VISIBLE);
//            itemRowHolder.headrCount.setText((coolcount + likeCount+swegcount+nerdcount+dabcount) + " reacts");
//        }
//
//        if(dataList.get(i).getAction_flag()==1){
//            itemRowHolder.textReact.setText("Cool");
//            itemRowHolder.coolbtn.setImageResource(R.drawable.svg_cool_emoji);
//        }else if(dataList.get(i).getAction_flag()==2){
//            itemRowHolder.textReact.setText("Swag");
//            itemRowHolder.coolbtn.setImageResource(R.drawable.svg_swag_emoji);
//        }else if(dataList.get(i).getAction_flag()==4){
//            itemRowHolder.textReact.setText("Dab");
//            itemRowHolder.coolbtn.setImageResource(R.drawable.svg_dab_emoji);
//        }else if(dataList.get(i).getAction_flag()==3){
//            itemRowHolder.textReact.setText("Nerd");
//            itemRowHolder.coolbtn.setImageResource(R.drawable.new_nerd_icon);
//        }else if(dataList.get(i).getAction_flag()==5){
//            itemRowHolder.textReact.setText("Heart");
//            itemRowHolder.coolbtn.setImageResource(R.drawable.ic_svg_heart);
//        }else{
//            itemRowHolder.textReact.setText("React");
//            itemRowHolder.coolbtn.setImageResource(R.drawable.ic_svg_cool_select);
//        }
//
//        itemRowHolder.textShareCount.setText(Utils.getRelativeTime(dataList.get(i).getPost_created_date_time()));
//        itemRowHolder.headrCount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, CommentLikeActivity.class);
//                bundle.putInt("comment_select_flag", 1);
//                bundle.putInt("keypadshow", 0);
//                bundle.putString("postid", dataList.get(i).getPostid());
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
//            }
//        });
//        itemRowHolder.textCommentcount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, CommentLikeActivity.class);
//                bundle.putInt("comment_select_flag", 0);
//                bundle.putInt("keypadshow", 0);
//                bundle.putString("postid", dataList.get(i).getPostid());
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
//            }
//        });
//
//
//        itemRowHolder.linearComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(mContext, CommentLikeActivity.class);
//                bundle.putInt("comment_select_flag", 0);
//                bundle.putInt("keypadshow", 1);
//                bundle.putString("postid", dataList.get(i).getPostid());
//                in.putExtras(bundle);
//                mContext.startActivity(in);
//            }
//        });
//        itemRowHolder.sharebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShareDialog(mContext, dataList.get(i));
//            }
//        });
//        if (dataList.get(i).getPost_video_url() != null) {
//            itemRowHolder.imageAnim.setVisibility(View.VISIBLE);
//            itemRowHolder.imageAnim.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
//        } else {
//            itemRowHolder.imageAnim.setVisibility(View.GONE);
//        }
//
//        itemRowHolder.postImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (itemRowHolder.total_image.getVisibility() == View.GONE && dataList.get(i).getPost_image_createdby_creator_url().size() > 1) {
//                    itemRowHolder.total_image.setVisibility(View.VISIBLE);
//                }
//                if (dataList.get(i).getPost_video_url() != null) {
//                    Intent it = new Intent(mContext, MomentFeedVideoPlay.class);
//                    it.putExtra("postid", dataList.get(i).getPostid());
//                    it.putExtra("post_creater_id", dataList.get(i).getPost_userid());
//                    mContext.startActivity(it);
//                } else {
//                    /*Intent it = new Intent(mContext, BuzzPreviewActivity.class);
//                    it.putExtra("postid", dataList.get(i).getPostid());
//                    it.putExtra("post_creater_id", dataList.get(i).getPost_userid());
//                    mContext.startActivity(it);*/
//                    Fragment fragment = new BuzzPreviewActivity_new();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("postlistdata",dataList.get(i));
//                    fragment.setArguments(bundle);
//                    FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    //fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
//                    fragmentTransaction.replace(R.id.main_content, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                }
//            }
//        });
//        itemRowHolder.moreoption.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (logedInUserid.equalsIgnoreCase(postUserid)) {
//                    itemRowHolder.textReport.setText("Delete");
//                } else {
//                    itemRowHolder.textReport.setText("Report");
//                }
//                try {
//                    boolean flag = (Boolean) itemRowHolder.moreoption.getTag();
//                    if (flag == true) {
//                        itemRowHolder.moreoption.setTag(false);
//                        itemRowHolder.more_view.setVisibility(View.VISIBLE);
//                    } else {
//                        itemRowHolder.moreoption.setTag(true);
//                        itemRowHolder.more_view.setVisibility(View.GONE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        itemRowHolder.more_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                itemRowHolder.moreoption.setTag(true);
//                itemRowHolder.more_view.setVisibility(View.GONE);
//                if (flag==1) {
//                    momentsFeed.delete_report_post(i);
//                }
//                if (flag==2) {
//                    aboutFragment.delete_report_post(i);
//                }
//
//            }
//        });
//        itemRowHolder.linReact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemRowHolder.textReact.setText("React");
//                itemRowHolder.coolbtn.setImageResource(R.drawable.ic_svg_cool_select);
//                SectionDataModel smodel = dataList.get(i);
//                if (smodel.getAction_flag() > 0) {
//                    /**
//                     * * Call All API rlated to Unlike/Uncool/UnDaab Etc
//                     * */
//                    if (flag==1) {
//                        momentsFeed.deleteDataset(i, smodel.getAction_flag());
//                    }
//                    if (flag==2) {
//                        aboutFragment.deleteDataset(i, smodel.getAction_flag());
//                    }
//                } else {
//                    /**
//                     * * Show ANimation to User action Like/Cool/Nerd/Dab
//                     * */
//                    if (v.isSelected()) {
//                        hideMenu(itemRowHolder.coolbtn, itemRowHolder.arc_layout, itemRowHolder.framArcView);
//                    } else {
//                        showMenu(itemRowHolder.coolbtn, itemRowHolder.arc_layout, itemRowHolder.framArcView);
//                    }
//                    v.setSelected(!v.isSelected());
//
//                }
//
//
//            }
//        });
//        itemRowHolder.framArcView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                itemRowHolder.framArcView.setVisibility(View.INVISIBLE);
//            }
//        });
//
//
//        // user reactions
//        itemRowHolder.btnarc_cool.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideMenu(itemRowHolder.coolbtn, itemRowHolder.arc_layout, itemRowHolder.framArcView);
//                if (flag==1) {
//                    momentsFeed.updateDataset(i,1);
//                }else {
//                    aboutFragment.updateDataset(i,1);
//                }
//            }
//        });
//        itemRowHolder.btnarc_swag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideMenu(itemRowHolder.coolbtn, itemRowHolder.arc_layout, itemRowHolder.framArcView);
//                if (flag==1) {
//                    momentsFeed.updateDataset(i,2);
//                }else {
//                    aboutFragment.updateDataset(i,2);
//                }
//            }
//        });
//        itemRowHolder.btnarc_nerd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideMenu(itemRowHolder.coolbtn, itemRowHolder.arc_layout, itemRowHolder.framArcView);
//                if (flag==1) {
//                    momentsFeed.updateDataset(i,3);
//                }else {
//                    aboutFragment.updateDataset(i,3);
//                }
//            }
//        });
//        itemRowHolder.btnarc_dab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideMenu(itemRowHolder.coolbtn, itemRowHolder.arc_layout, itemRowHolder.framArcView);
//                if (flag==1) {
//                    momentsFeed.updateDataset(i,4);
//                }else {
//                    aboutFragment.updateDataset(i,4);
//                }
//            }
//        });
//        itemRowHolder.btnarc_heart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideMenu(itemRowHolder.coolbtn, itemRowHolder.arc_layout, itemRowHolder.framArcView);
//                if (flag==1) {
//                    momentsFeed.updateDataset(i,5);
//                }else {
//                    aboutFragment.updateDataset(i,5);
//                }
//            }
//        });
//    }
//
//    private void showMenu(ImageView imagview, ArcLayout arcLayout, final FrameLayout menuLayout) {
//        menuLayout.setVisibility(View.VISIBLE);
//
//        List<Animator> animList = new ArrayList<>();
//
//        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
//            animList.add(createShowItemAnimator(imagview, menuLayout, arcLayout.getChildAt(i)));
//        }
//
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.setDuration(400);
//        animSet.setInterpolator(new OvershootInterpolator());
//        animSet.playTogether(animList);
//        animSet.start();
//    }
//
//    private void hideMenu(ImageView imagview, ArcLayout arcLayout, final FrameLayout menuLayout) {
//        List<Animator> animList = new ArrayList<>();
//
//        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
//            animList.add(createHideItemAnimator(imagview, menuLayout, arcLayout.getChildAt(i)));
//        }
//
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.setDuration(400);
//        animSet.setInterpolator(new AnticipateInterpolator());
//        animSet.playTogether(animList);
//        animSet.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                menuLayout.setVisibility(View.INVISIBLE);
//            }
//        });
//        animSet.start();
//    }
//
//
//
//    private void userActionAnication(Animation anim, final ImageView imageAnim, int rid) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                imageAnim.setVisibility(View.GONE);
//
//            }
//        }, 300);
//        imageAnim.setImageResource(rid);
//        imageAnim.setVisibility(View.VISIBLE);
//        imageAnim.startAnimation(anim);
//        anim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                imageAnim.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//    @Override
//    public int getItemCount() {
//        return dataList.size();
//    }
//
//    public class ItemRowHolder extends RecyclerView.ViewHolder {
//
//        protected TextView itemTitle, taggednumber, txt_descriptn, total_image;
//        protected RecyclerView recycler_view_list;
//        protected RelativeLayout relativeLayout;
//        protected ImageView im, imageAnim, moreoption;
//        //    private View viewListContainer;
//        private ImageView postImage, coolbtn, likebtn, commentbtn, sharebtn;
//        ;
//        private TextView timeset, headrCount, textCommentcount, textShareCount, textReact, textReport;
//        private LinearLayout linearComment, linReact;
//        FrameLayout framArcView;
//        RelativeLayout layoutMain;
//        ArcLayout arc_layout;
//        ImageButton btnarc_heart, btnarc_cool, btnarc_dab, btnarc_nerd, btnarc_swag;
//        RecyclerView selfyListRecyce;
//        CardView more_view;
//        RelativeLayout relFeedThumb,relFeedThumb_share;
//        ImageView owner_pic,owner_post;
//        TextView owner_name,tagged_number_owner_creator,owner_post_title;
//
//        public ItemRowHolder(View view) {
//            super(view);
//            total_image = (TextView) view.findViewById(R.id.total_image);
//            itemTitle = (TextView) view.findViewById(R.id.posted_user_name);
//            taggednumber = (TextView) view.findViewById(R.id.tagged_number_post_creator);
//            txt_descriptn = (TextView) view.findViewById(R.id.text_post_description);
//            timeset = (TextView) view.findViewById(R.id.time_days_go);
//            im = (ImageView) view.findViewById(R.id.userViewImage);
//            likebtn = (ImageView) view.findViewById(R.id.like);
//            commentbtn = (ImageView) view.findViewById(R.id.comment);
//            sharebtn = (ImageView) view.findViewById(R.id.share);
//            postImage = (ImageView) view.findViewById(R.id.buzz_posted_image);
//            txt_descriptn.setTypeface(FontStyles.font4Profile(mContext));
//            layoutMain = (RelativeLayout) view.findViewById(R.id.top);
//            textReport = (TextView) itemView.findViewById(R.id.textReport);
//            more_view = (CardView) itemView.findViewById(R.id.card_view);
//            imageAnim = (ImageView) view.findViewById(R.id.imageAnim);
//            moreoption = (ImageView) view.findViewById(R.id.moreoption);
//            headrCount = (TextView) view.findViewById(R.id.headrCount);
//            textCommentcount = (TextView) view.findViewById(R.id.Commentcount);
//            textShareCount = (TextView) view.findViewById(R.id.textShareCount);
//            linearComment = (LinearLayout) view.findViewById(R.id.linearComment);
//            linReact = (LinearLayout) view.findViewById(R.id.linReact);
//            framArcView = (FrameLayout) view.findViewById(R.id.menu_layout);
//            arc_layout = (ArcLayout) view.findViewById(R.id.arc_layout);
//            btnarc_heart = (ImageButton) view.findViewById(R.id.btnarc_heart);
//            btnarc_cool = (ImageButton) view.findViewById(R.id.btnarc_cool);
//            btnarc_dab = (ImageButton) view.findViewById(R.id.btnarc_dab);
//            btnarc_nerd = (ImageButton) view.findViewById(R.id.btnarc_nerd);
//            btnarc_swag = (ImageButton) view.findViewById(R.id.btnarc_swag);
//            textReact = (TextView) view.findViewById(R.id.textReact);
//            coolbtn = (ImageView) view.findViewById(R.id.cool);
//            relFeedThumb = (RelativeLayout) view.findViewById(R.id.relFeedThumb);
//            relFeedThumb_share = (RelativeLayout) view.findViewById(R.id.relFeedThumb_share);
//            owner_name = (TextView) view.findViewById(R.id.owner_name);
//            tagged_number_owner_creator=(TextView) view.findViewById(R.id.tagged_number_owner_creator);
//            owner_post_title = (TextView) view.findViewById(R.id.owner_post_title);
//            owner_pic = (ImageView) view.findViewById(R.id.owner_pic);
//            owner_post = (ImageView) view.findViewById(R.id.owner_post);
//        }
//    }
//
//    private Animator createHideItemAnimator(final ImageView fab, FrameLayout frameLayout, final View item) {
//        /*float dx = frameLayout.getX() / 2;//fab.getX() - item.getX();
//        float dy = 0;//fab.getY()- item.getY();*/
//
//        float dx = fab.getX() - item.getX();
//        float dy = fab.getY() - item.getY();
//
//        dy = dy + 320;
//        dx = dx + 280;
//        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
//                item,
//                AnimatorUtils.rotation(720f, 0f),
//                AnimatorUtils.translationX(0f, dx),
//                AnimatorUtils.translationY(0f, dy)
//        );
//
//        anim.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                item.setTranslationX(0f);
//                item.setTranslationY(0f);
//            }
//        });
//
//        return anim;
//
//
//    }
//
//    private Animator createShowItemAnimator(final ImageView fab, FrameLayout frameLayout, View item) {
//
//       /* float dx = frameLayout.getX() / 2;//fab.getX();//- item.getX();
//        float dy = 0;//fab.getY();// - item.getY();*/
//        float dx = fab.getX() - item.getX();
//        float dy = fab.getY() - item.getY();
//
//        dy = dy + 320;
//        dx = dx + 280;
//        item.setRotation(0f);
//        item.setTranslationX(dx);
//        item.setTranslationY(dy);
//
//        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
//                item,
//                AnimatorUtils.rotation(0f, 720f),
//                AnimatorUtils.translationX(dx, 0f),
//                AnimatorUtils.translationY(dy, 0f)
//        );
//
//        return anim;
//
//
//    }
//
//}