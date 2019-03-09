package com.mindzglobal.www.mindz.Feeds.adapters;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.mindzglobal.www.mindz.Feeds.BaseActivity;
import com.mindzglobal.www.mindz.Feeds.managers.PostManager;
import com.mindzglobal.www.mindz.Feeds.managers.listeners.OnPostChangedListener;
import com.mindzglobal.www.mindz.Feeds.model.Post;
import com.mindzglobal.www.mindz.Feeds.utils.LogUtil;
import com.mindzglobal.www.mindz.Home.HomeFragment;

import java.util.LinkedList;
import java.util.List;

public abstract class BasePostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = BasePostsAdapter.class.getSimpleName();

    protected List<Post> postList = new LinkedList<>();
    protected BaseActivity activity;
    protected int selectedPostPosition = -1;

    public BasePostsAdapter(BaseActivity activity) {
        this.activity = activity;
    }

    public BasePostsAdapter(HomeFragment fragment, SwipeRefreshLayout mSwipeRefreshLayout) {

    }

    protected void cleanSelectedPostInformation() {
        selectedPostPosition = -1;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return postList.get(position).getItemType().getTypeCode();
    }

    protected Post getItemByPosition(int position) {
        return postList.get(position);
    }

    private OnPostChangedListener createOnPostChangeListener(final int postPosition) {
        return new OnPostChangedListener() {
            @Override
            public void onObjectChanged(Post obj) {
                postList.set(postPosition, obj);
                notifyItemChanged(postPosition);
            }

            @Override
            public void onError(String errorText) {
                LogUtil.logDebug(TAG, errorText);
            }
        };
    }

    public void updateSelectedPost() {
        if (selectedPostPosition != -1) {
            Post selectedPost = getItemByPosition(selectedPostPosition);
            PostManager.getInstance(activity).getSinglePostValue(selectedPost.getId(), createOnPostChangeListener(selectedPostPosition));
        }
    }
}
