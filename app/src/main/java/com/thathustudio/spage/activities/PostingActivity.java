package com.thathustudio.spage.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.thathustudio.spage.R;
import com.thathustudio.spage.adapter.PhotoViewerAdapter;
import com.thathustudio.spage.adapter.ChooseSubjectAdapter;
import com.thathustudio.spage.firebase.Storage;
import com.thathustudio.spage.firebase.UploadCallback;
import com.thathustudio.spage.interfaces.OnRemovePhotoListener;
import com.thathustudio.spage.model.Post;
import com.thathustudio.spage.model.Subject;
import com.thathustudio.spage.model.User;
import com.thathustudio.spage.model.responses.PostResponse;
import com.thathustudio.spage.service.SpageServiceCallback;
import com.thathustudio.spage.util.TaskManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class PostingActivity extends SpageActivity implements View.OnClickListener, OnRemovePhotoListener, TextWatcher {

//    private int ACTION;
    public static final int ACTION_CREATE = 0;
    public static final int ACTION_EDIT = 1;
    public static final String KEY_ACTION = "KEY_ACTION";
    public static final String KEY_POST = "KEY_POST";



    //Key for get Subject
    public static final String KEY_SUBJECT= "KEY_SUBJECT";
    Subject mSubject;
    ArrayList<Subject> lstSubject =  new ArrayList<>();

    //Key for get User
    public static final String KEY_USER= "KEY_USER";
    User mUser;



    private final int INTENT_GALLERY = 0;
    private final int INTENT_CAMERA = 1;


    Toolbar mToolbar;
    AppCompatImageButton btnGallery,btnCamera;
    AppCompatEditText edtStatus;
    AppCompatButton btnPost;
    RecyclerView rcvPhotoViewer;
    PhotoViewerAdapter mAdapter;
    ProgressBar prbLoading;

    TaskManager taskManager =  new TaskManager();



    ArrayList<Uri> lstPhoto = new ArrayList<>();


    private Spinner subjectSpinner;
    private ChooseSubjectAdapter adapter;



    private int mActionMode;
    private Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout();
        setupEvent();
        getExtraData();
        setUpSubjectSpinner();
        getListSubject();
    }

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_posting;
    }


    private void setupLayout(){
        bindView();
        setupToolbar();
        setupRecyclerView();
    }

    private void bindView(){
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        btnCamera = (AppCompatImageButton)findViewById(R.id.btnCamera);
        btnGallery  = (AppCompatImageButton)findViewById(R.id.btnGallery);
        btnPost  = (AppCompatButton)findViewById(R.id.btnPost);
        rcvPhotoViewer = (RecyclerView) findViewById(R.id.recycler_view);
        edtStatus = (AppCompatEditText)findViewById(R.id.edtStatus);
        prbLoading = (ProgressBar)findViewById(R.id.prbLoading);
    }
    private void setupEvent(){
        btnGallery.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnPost.setOnClickListener(this);
        edtStatus.addTextChangedListener(this);
    }
    private void setUpSubjectSpinner()
    {
        adapter = new ChooseSubjectAdapter(this,
                R.layout.item_spinner,
                lstSubject);
        subjectSpinner = (Spinner) findViewById(R.id.spinner_nav);
        subjectSpinner.setAdapter(adapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Subject user = adapter.getItem(position);
                mSubject.setId(user.getId());
                mSubject.setName(user.getName());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }

    private void hideActionBar(){
        getSupportActionBar().hide();
    }

    private void setupToolbar(){
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(getString(R.string.activity_posting_title));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void setupRecyclerView(){
        mAdapter = new PhotoViewerAdapter(lstPhoto,this);
        mAdapter.setOnRemovePhotoListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        rcvPhotoViewer.setLayoutManager(mLayoutManager);
        rcvPhotoViewer.setItemAnimator(new DefaultItemAnimator());
        rcvPhotoViewer.setAdapter(mAdapter);
    }

    //Get data
    private void getExtraData(){
        mActionMode = getIntent().getIntExtra(KEY_ACTION,ACTION_CREATE);
        mUser = (User) getIntent().getSerializableExtra(KEY_USER);
        mPost = (Post) getIntent().getSerializableExtra(KEY_POST);
        mSubject = (Subject)getIntent().getSerializableExtra(KEY_SUBJECT);
        edtStatus.setText(mPost.getContent());


        switch (mActionMode){
            case ACTION_CREATE:{
                break;
            }
            case ACTION_EDIT:{
                btnPost.setText("UPDATE");
                if(mPost.getImage()!=null)
                    if(mPost.getImage().equals("")==false)
                    {
                        lstPhoto.add(Uri.parse(mPost.getImage()));
                        disableAddImage(true);
                    }

                break;
            }
            default:{
                break;
            }
        }
    }




    private void getListSubject()
    {
        getCustomApplication().getSpageService().readListSubject(new SpageServiceCallback<List<Subject>>() {
            @Override
            public void setCall(Call call) {
                taskManager.pushTaskToQueue(call);

            }
            @Override
            public void cancel() {

            }

            @Override
            public void onPreExcute() {

            }

            @Override
            public void onPostExcute(List<Subject> subjects, Throwable throwable) {
                if(subjects==null || subjects.size()==0){
                    getListSubject();
                    return;
                }
                Log.d("Thai","Size of list = "+subjects.size());
                lstSubject.addAll(subjects);
                adapter.notifyDataSetChanged();
                if(mSubject!=null)
                {
                    for(int i=0;i<lstSubject.size();++i){
                        if(lstSubject.get(i).getId()==mSubject.getId()){
                            subjectSpinner.setSelection(i);
                        }
                    }
                }
            }
        });
    }






    private void selectPhotoFromLibrary(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), INTENT_GALLERY);
    }

    public void takePhoto() {
        Intent intent= new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, INTENT_CAMERA);
    }

    public void disableAddImage(boolean disable)
    {
        btnCamera.setVisibility(disable?View.GONE:View.VISIBLE);
        btnGallery.setVisibility(disable?View.GONE:View.VISIBLE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // When an Image is picked
        if (requestCode == INTENT_GALLERY && resultCode == RESULT_OK
                && null != data) {
            ArrayList lstSelectedPhoto =  getListSelectedImage(data);
            if(lstSelectedPhoto!=null && lstSelectedPhoto.size()>0){
                lstPhoto.addAll(lstSelectedPhoto);
                mAdapter.notifyDataSetChanged();
                disableAddImage(true);

                Storage.upload("post/"+mPost.getId(), data.getData(), new UploadCallback()
                {
                    @Override
                    public void onUploadSuccess(String url)
                    {
                        Log.e("firebaseStorage", url);
                        mPost.setImage(url);

                        // display image to ImageView
                        try
                        {
//                            Storage.displayImage(UploadActivity.this, imageView, "post9");
                        }
                        catch (Exception e)
                        {
                            Log.e("firebaseStorage", e.getMessage());
                        }
                    }
                });
            }

        } else {
//            Toast.makeText(this, "You haven't picked Image",
//                    Toast.LENGTH_LONG).show();
        }

        // When an Image is picked
        if (requestCode == INTENT_CAMERA && resultCode == RESULT_OK
                && null != data) {
            Uri uri = data.getData();
            if(uri!=null){
                lstPhoto.add(uri);
                mAdapter.notifyDataSetChanged();
            }

            disableAddImage(true);

        } else {
//            Toast.makeText(this, "You haven't picked Image",
//                    Toast.LENGTH_LONG).show();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private ArrayList<Uri> getListSelectedImage(Intent data){
        ArrayList<Uri> mArrayUri = new ArrayList<>();
        mArrayUri.add(data.getData());
        return mArrayUri;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCamera:{
                takePhoto();
                break;
            }


            case R.id.btnGallery:{
                selectPhotoFromLibrary();
                break;
            }
            case R.id.btnPost:{
                if(isValidPost()){
                    postStatus();
                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.activity_posting_notify_post_error),Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


    private void createPost(){
        mPost.setSubjectId(mSubject.getId());
        mPost.setUserId(mUser.getId());
        mPost.setRating(0);
        mPost.setCommentCount(0);
//        mPost.set
//        mPost.setImage("abc");
//        mPost.set
        mPost.setContent(edtStatus.getText().toString());


        getCustomApplication().getSpageService().createPost(mPost, new SpageServiceCallback<PostResponse>() {
            @Override
            public void setCall(Call call) {

            }

            @Override
            public void cancel() {

            }

            @Override
            public void onPreExcute() {
                prbLoading.setVisibility(View.VISIBLE);
                edtStatus.setEnabled(false);
                btnPost.setEnabled(false);
            }

            @Override
            public void onPostExcute(PostResponse post, Throwable throwable) {
                if(post.getCode()!=200){
                    prbLoading.setVisibility(View.GONE);
                    edtStatus.setEnabled(true);
                    btnPost.setEnabled(true);
                    return;
                }

//                PostResponse postRes = (PostResponse) post;
                if(post==null){
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("Thai","Update status success "+post.getId());

                mPost.setId(post.getId());

//                String s = post.getResult();


                uploadImageToPost(mPost.getImage(),mPost);
            }
        });
    }


    void uploadImageToPost(String url,Post post)
    {
        getCustomApplication().getSpageService().updatePost(post.getId(), post, new SpageServiceCallback<PostResponse>() {
            @Override
            public void setCall(Call call) {

            }

            @Override
            public void cancel() {

            }

            @Override
            public void onPreExcute() {

            }

            @Override
            public void onPostExcute(PostResponse postResponse, Throwable throwable) {

                prbLoading.setVisibility(View.GONE);
                edtStatus.setEnabled(true);
                btnPost.setEnabled(true);
                closeActivity(Activity.RESULT_OK);

//                Intent i =
            }
        });

//        getCustomApplication().getSpageService().
    }

    private void closeActivity(int code){
        Intent i = new Intent();
        i.putExtra("Post",mPost);
        setResult(code, i);
        finish();
        taskManager.cancelAllTask();
    }

    private void editPost(){
        mPost.setContent(edtStatus.getText().toString());
        if(lstPhoto.size()==0)
        {
            mPost.setImage(null);
        }
//        mPost.setId(59);

        getCustomApplication().getSpageService().updatePost(mPost.getId(), mPost, new SpageServiceCallback<PostResponse>() {
            @Override
            public void setCall(Call call) {

            }

            @Override
            public void cancel() {

            }

            @Override
            public void onPreExcute() {
                prbLoading.setVisibility(View.VISIBLE);
                edtStatus.setEnabled(false);
                btnPost.setEnabled(false);
            }

            @Override
            public void onPostExcute(PostResponse postResponse, Throwable throwable) {
                prbLoading.setVisibility(View.GONE);
                edtStatus.setEnabled(true);
                btnPost.setEnabled(true);
                if(postResponse.getCode()==200){
                    closeActivity(Activity.RESULT_OK);
                }else{
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void postStatus(){
        switch (mActionMode){
            case ACTION_CREATE:{
                createPost();
                break;
            }
            case ACTION_EDIT:{
                editPost();
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                closeActivity(RESULT_CANCELED);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRemovePhoto(int pos) {
        lstPhoto.remove(pos);
        mAdapter.notifyDataSetChanged();
        if(lstPhoto.size()==0){
            disableAddImage(false);
        }
    }


    boolean isValidPost(){
        if(lstSubject.size()==0 || (isWholeWhiteSpace(edtStatus.getText().toString())||
                edtStatus.getText().equals("")) && !existAnyPhotosInPost()  ){
            return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    private boolean isWholeWhiteSpace(String str){
        for(int i =0;i<str.length();++i){
            if(str.charAt(i)!=' '){
                return false;
            }
        }
        return true;
    }

    private boolean existAnyPhotosInPost(){
        return lstPhoto.size()>0;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.equals("") || isWholeWhiteSpace(charSequence.toString())){
            //btnPost.setEnabled(false);
            if(!existAnyPhotosInPost()){
                Log.d("Thai","false");
            }
        }else{
            Log.d("Thai","true");
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        taskManager.cancelAllTask();
    }
}