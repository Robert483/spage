package com.thathustudio.spage.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thathustudio.spage.R;
import com.thathustudio.spage.activities.CommentsActivity;
import com.thathustudio.spage.activities.ImageViewerActivity;
import com.thathustudio.spage.activities.PostingActivity;
import com.thathustudio.spage.activities.SpageActivity;
import com.thathustudio.spage.adapter.PhotoViewerAdapter;
import com.thathustudio.spage.adapter.PostAdapter;
import com.thathustudio.spage.app.CustomApplication;
import com.thathustudio.spage.interfaces.OnClickImageListener;
import com.thathustudio.spage.interfaces.OnCommentClickListener;
import com.thathustudio.spage.interfaces.OnCreatePostClickListener;
import com.thathustudio.spage.interfaces.OnLikeClickListener;
import com.thathustudio.spage.interfaces.OnLoadmorePostListener;
import com.thathustudio.spage.interfaces.OnMorePostItemClickListener;
import com.thathustudio.spage.interfaces.OnUserProfileClickListener;
import com.thathustudio.spage.model.Post;
import com.thathustudio.spage.model.Subject;
import com.thathustudio.spage.model.User;
import com.thathustudio.spage.model.responses.PostResponse;
import com.thathustudio.spage.service.SpageServiceCallback;
import com.thathustudio.spage.service.SpageServiceImpl;
import com.thathustudio.spage.util.LikeUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends BaseFragment implements OnCreatePostClickListener, SwipeRefreshLayout.OnRefreshListener, OnLoadmorePostListener, OnCommentClickListener, OnUserProfileClickListener, OnMorePostItemClickListener, OnClickImageListener, OnLikeClickListener {

 public static final int PAGE_NEWFEED = 0;
    public static final int PAGE_SUBJECT =1;
    public static final int PAGE_MYPOST =2;

    public static final int REQUEST_NEW_POST = 0;
    public static final int REQUEST_EDIT_POST = 1;

    ArrayList<Post> lstPost =  new ArrayList<>();

    RecyclerView rcvPost;
    PostAdapter mAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    ProgressBar prbLoading,prbLoadingLike;
    Subject mSubject;
    User mUser;

    boolean isHideHeader = false;


    public PostsFragment() {
        // Required empty public constructor


    }

    void createTestData(){
        PAGE = PAGE_SUBJECT;
        mUser = new User("Thai Nguyen","dds","thainguyen.hcmus@gmail.com",1,1);
        mSubject =  new Subject(4,"Tiếng Việt");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostsFragment.
     *
     * String
     */

    int PAGE;


    public  static final  String ARG_USER = "ARG_USER";
    public  static final  String ARG_PAGE_TYPE = "ARG_PAGE_TYPE";
    public static final String ARG_SUBJECT = "ARG_SUBJECT";
//    public static final String ARG_SUBJECT = "ARG_USER";
    // TODO: Rename and change types and number of parameters
    public static PostsFragment newInstance(User user,int page) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        args.putInt(ARG_PAGE_TYPE, page);
        fragment.setArguments(args);
        return fragment;
    }



    public static PostsFragment newInstance(User user,Subject subject,int page) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        // RYANVU: args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_SUBJECT, subject);
        args.putParcelable(ARG_USER, user);
        args.putInt(ARG_PAGE_TYPE, page);
        fragment.setArguments(args);


        return fragment;
    }




    private void bindView(View v){

        prbLoading = (ProgressBar)v.findViewById(R.id.progressBar);
        rcvPost = (RecyclerView)v.findViewById(R.id.rcvPost);
        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipe_refresh_layout);
    }

    private void setupEvent(){
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    private void setupRecyclerView(){
        mAdapter = new PostAdapter(lstPost,getContext(),isHideHeader);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        mAdapter.setOnCreatePostClickListener(this);
        mAdapter.setOnLoadmorePostListener(this);
        mAdapter.setOnCommentClickListener(this);
        mAdapter.setOnUserProfileClickListener(this);
        mAdapter.setOnLikeClickListener(this);
        mAdapter.setOnMorePostItemClickListener(this);
        mAdapter.setOnClickImageListener(this);
        rcvPost.setLayoutManager(mLayoutManager);
        rcvPost.setItemAnimator(new DefaultItemAnimator());
        rcvPost.setAdapter(mAdapter);
    }

    private void getListPostOfUser(User user)
    {
        ((SpageActivity)getActivity()).getCustomApplication().getSpageService().readPostsOfUser(user.getId(), new SpageServiceCallback<List<Post>>() {
            @Override
            public void setCall(Call call) {

            }

            @Override
            public void cancel() {

            }

            @Override
            public void onPreExcute() {

                prbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExcute(List<Post> posts, Throwable throwable) {
                prbLoading.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if(posts==null){
                    Log.d("Thai","null");
                    return;
                }

                for(int i=0;i<posts.size();++i){
                    posts.get(i).setUsername(mUser.getUserName());
                }
                Log.d("Thai","Size of list post = "+posts.size());
                lstPost.clear();
                lstPost.addAll(posts);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getListNewsFeedOfUser(User user)
    {
        ((SpageActivity)getActivity()).getCustomApplication().getSpageService().readNewFeedOfUser(1, new SpageServiceCallback<List<Post>>() {
            @Override
            public void setCall(Call call) {

            }

            @Override
            public void cancel() {

            }

            @Override
            public void onPreExcute() {
                prbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExcute(List<Post> posts, Throwable throwable) {
                prbLoading.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if(posts==null){

                    Log.d("Thai","null");
                    return;
                }
                for(int i=0;i<posts.size();++i){
                    posts.get(i).setUsername(mUser.getUserName());
                }
                lstPost.clear();
                lstPost.addAll(posts);
                mAdapter.notifyDataSetChanged();
            }
        });

        if(swipeRefreshLayout!=null)
            swipeRefreshLayout.setRefreshing(false);
    }



    private void getListData(){
        switch (PAGE){
            case PAGE_NEWFEED:{
                getListNewsFeedOfUser(mUser);
                break;
            }

            case PAGE_SUBJECT:{
                getListPostsOfSubject(mSubject);

                break;
            }
            case PAGE_MYPOST:{
                getListPostOfUser(mUser);
                break;
            }

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSubject = (Subject) getArguments().getSerializable(ARG_SUBJECT);
            mUser = (User) getArguments().getParcelable(ARG_USER);
            PAGE = getArguments().getInt(ARG_PAGE_TYPE);
            isHideHeader = (PAGE==PAGE_MYPOST);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_posts, container, false);
        bindView(v);
//        createTestData();
//        deleteData();

        getListData();
        setupEvent();
        setupRecyclerView();
        return v;
    }

    @Override
    public void onCreatePostClick(View v) {


        Intent i =  new Intent(getActivity(),PostingActivity.class);
        i.putExtra(PostingActivity.KEY_ACTION,PostingActivity.ACTION_CREATE);
        Post post = new Post();
        if(mSubject==null)
            mSubject = new Subject();
        post.setSubjectId(mSubject.getId());
        post.setUserId(mUser.getId());
        i.putExtra(PostingActivity.KEY_POST,post);
        i.putExtra(PostingActivity.KEY_USER, mUser);

        if(mSubject==null){
            mSubject = new Subject();
        }
        i.putExtra(PostingActivity.KEY_SUBJECT,mSubject);
        startActivityForResult(i,REQUEST_NEW_POST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){

            case REQUEST_NEW_POST:{
                switch (resultCode){
                    case Activity.RESULT_OK:{
                        Post newPost = (Post) data.getSerializableExtra("Post");
                        newPost.setUsername(mUser.getUsername());
                        if(newPost!=null){
                            lstPost.add(0,newPost);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }
                break;
            }

            case REQUEST_EDIT_POST:{
                switch (resultCode){
                    case Activity.RESULT_OK:{
                        Post updatePost = (Post) data.getSerializableExtra("Post");
                        updatePost.setUsername(mUser.getUsername());
                        if(updatePost!=null){
                            lstPost.get(selectedPos).clone(updatePost);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                    }
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getListPostsOfSubject(Subject subject)
    {
        ((SpageActivity)getActivity()).getCustomApplication().getSpageService().readPostOfSubject(subject.getId(), new SpageServiceCallback<List<Post>>() {
            @Override
            public void setCall(Call call) {

            }

            @Override
            public void cancel() {

            }

            @Override
            public void onPreExcute() {
                prbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExcute(List<Post> posts, Throwable throwable) {
                prbLoading.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if(posts==null){

                    Log.d("Thai","null");
                    return;
                }
//                for(int i=0;i<posts.size();++i){
//                    posts.get(i).setUsername(mUser.getUserName());
//                }
                Log.d("Thai","Size of list post = "+posts.size());
                lstPost.clear();
                lstPost.addAll(posts);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        getListData();
    }

    @Override
    public void onLoadMorePost() {
        Toast.makeText(getContext(),"Loadmore",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommentClick(Post post) {

        Intent i= new Intent(getActivity(),CommentsActivity.class);
        i.putExtra("POST",post);

        startActivity(i);
    }

    @Override
    public void onUserProfileClick(int userId) {

    }
    private void startEditPost(Post post){
        Intent i =  new Intent(getActivity(),PostingActivity.class);
        i.putExtra(PostingActivity.KEY_ACTION,PostingActivity.ACTION_EDIT);
        i.putExtra(PostingActivity.KEY_POST,post);



        i.putExtra(PostingActivity.KEY_USER, mUser);



        if(mSubject==null)
            mSubject = new Subject(1, "");
        i.putExtra(PostingActivity.KEY_SUBJECT, mSubject);

        startActivityForResult(i,REQUEST_EDIT_POST);
    }

    private void deletePost(final Post post){
//        SpageServiceImpl spageService =  new SpageServiceImpl()
        ((SpageActivity)getActivity()).getCustomApplication().getSpageService().deletePost(post.getId(), new SpageServiceCallback<Post>() {
            @Override
            public void setCall(Call call) {
            }

            @Override
            public void cancel() {

            }

            @Override
            public void onPreExcute() {
                prbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExcute(Post post, Throwable throwable) {
                prbLoading.setVisibility(View.GONE);
                lstPost.remove(selectedPos);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showConfirmDeletePost(final Post post){
        new AlertDialog.Builder(getActivity())
//                .setTitle("Delete entry")
                .setMessage(getString(R.string.fragment_post_dialog_delete_title))
                .setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deletePost(post);

                    }
                })
                .setNegativeButton(getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private int selectedPos;
    @Override
    public void onMorePostItemClick(View v, final Post post,int position) {
        selectedPos = position;
        //creating a popup menu
        PopupMenu popup = new PopupMenu(getContext(), v);
        //inflating menu from xml resource
        popup.inflate(R.menu.menu_row_post);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menu_delete:{
                        showConfirmDeletePost(post);
                        break;
                    }
                    case R.id.menu_edit:{
                        startEditPost(post);
                        break;
                    }
                }

                return false;
            }
        });
        //displaying the popup
        popup.show();
    }

    @Override
    public void onClickImage(String url) {
        Intent i =  new Intent(getActivity(),ImageViewerActivity.class);
        i.putExtra("URL",url);

        startActivity(i);
    }

    @Override
    public void onLikeClick(ProgressBar prbLoadingLike,View v,int posistion,boolean liked) {
        AppCompatImageView btnLike = (AppCompatImageView)v;
        this.prbLoadingLike = prbLoadingLike;
//        btnLike.setEnabled(false);
        updateLike(btnLike,posistion,liked);
        Log.d("Thai","onLikeClick");
    }

    private void updateLike(final AppCompatImageView btnLike, final int pos, final boolean liked){

         final Post post = lstPost.get(pos);
        Log.e("Son",post.toString());
        if(liked)
            post.setRating(post.getRating()+1);
        else
            post.setRating(post.getRating()-1);
        ((SpageActivity)getActivity()).getCustomApplication().getSpageService().updatePost(post.getId(), post, new SpageServiceCallback<PostResponse>() {
            @Override
            public void setCall(Call call) {

            }

            @Override
            public void cancel() {

            }

            @Override
            public void onPreExcute() {
                prbLoadingLike.setVisibility(View.VISIBLE);
                btnLike.setVisibility(View.GONE);

            }

            @Override
            public void onPostExcute(PostResponse postResponse, Throwable throwable) {

                prbLoadingLike.setVisibility(View.GONE);
                btnLike.setVisibility(View.VISIBLE);
                Log.d("Thai","onLikeClick onPostExcute "+post.getRating());
                mAdapter.notifyDataSetChanged();
                LikeUtil.saveLikeToSharePre(getContext(),post.getUserId(),post.getId());
            }
        });
    }
}
