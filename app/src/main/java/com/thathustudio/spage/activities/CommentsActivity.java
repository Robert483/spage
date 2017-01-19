package com.thathustudio.spage.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thathustudio.spage.R;
import com.thathustudio.spage.adapter.CommentListAdapter;
import com.thathustudio.spage.firebase.Storage;
import com.thathustudio.spage.firebase.UploadCallback;
import com.thathustudio.spage.model.Comment;
import com.thathustudio.spage.model.responses.EndPointResponse;
import com.thathustudio.spage.model.responses.Post;
import com.thathustudio.spage.service.callback.ForegroundTaskDelegate;
import com.thathustudio.spage.views.UserHeader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;


public class CommentsActivity extends SpageActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int GALLERY_REQUEST_CODE = 1;
    private Post post;

    private UserHeader postHeader;
    private List<Comment> commentList = new ArrayList<>();
    private RecyclerView rvComments;
    private CommentListAdapter adapter;

    private EditText etAddComment;
    private ImageButton btnChooseImage, btnSend, btnDeletePhoto;
    private ImageView ivAttachedPhoto;
    private RelativeLayout layout_attachment;

    private Uri selectedImageUri;

    //Fake postId
    int postId = 3;

    //Fake userId;
    int userId = 1;
    String username = "pkh";
    private Comment currentComment = new Comment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setUpView();

        setUpEvent();

        readCommentsOfPost();
    }

    private void setUpEvent() {
        btnSend.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this);
        btnDeletePhoto.setOnClickListener(this);
    }

    private void setUpView() {
        rvComments = (RecyclerView) findViewById(R.id.rvComments);
        postHeader = (UserHeader) findViewById(R.id.postHeader);

        rvComments.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(layoutManager);
        adapter = new CommentListAdapter(commentList, this);
        rvComments.setAdapter(adapter);

        btnChooseImage = (ImageButton) findViewById(R.id.btnChooseImage);
        btnSend = (ImageButton) findViewById(R.id.btnSend);
        etAddComment = (EditText) findViewById(R.id.etAddComment);
        ivAttachedPhoto = (ImageView) findViewById(R.id.ivAttachedPhoto);
        btnDeletePhoto = (ImageButton) findViewById(R.id.btnDeletePhoto);
        layout_attachment = (RelativeLayout) findViewById(R.id.layout_attachment);
    }

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_comments;
    }

    private void readCommentsOfPost() {
        spageService.readCommentsOfPost(3, new ReadCommentsOfPostCallback(this));
    }

    private void createComment() {
//        currentComment = new Comment();
        currentComment.setPostId(3);
        currentComment.setRating(0);
        currentComment.setContent(etAddComment.getText().toString());
        currentComment.setUserId(userId);
        currentComment.setUsername(username);
        adapter.addComment(currentComment);
        spageService.createComment(currentComment, new CreateCommentCallback(this));
    }

    public void updateComment(Comment comment) {
        comment.setPostId(3);
        spageService.updateComment(comment.getId(), comment, new UpdateCommentCallback(this));

    }

    private void deleteComment() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnChooseImage:
                chooseImage();
                break;

            case R.id.btnSend:
                if (!etAddComment.getText().toString().equals("")) {
                    if(selectedImageUri != null){
                        uploadImage(selectedImageUri, new UploadCallback() {
                            @Override
                            public void onUploadSuccess(String url) {
                                currentComment.setImage(url);
                                createComment();
                            }
                        });
                    }
                    else{
                        createComment();
                    }
                }
                break;
            case R.id.btnDeletePhoto:
                ivAttachedPhoto.setImageDrawable(null);
                layout_attachment.setVisibility(View.GONE);
                selectedImageUri = null;
                break;
        }
    }

    private void chooseImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        TextView title = new TextView(this);
        title.setText("Add Photo!");
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(title);

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
                else if (items[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, GALLERY_REQUEST_CODE);
                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    layout_attachment.setVisibility(View.VISIBLE);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    selectedImageUri = getImageUri(this, photo);
                    Picasso.with(this).load(selectedImageUri).fit().into(ivAttachedPhoto);
                }
                break;
            case GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        layout_attachment.setVisibility(View.VISIBLE);
                        selectedImageUri = selectedImage;
                        Picasso.with(this).load(selectedImage).fit().into(ivAttachedPhoto);
                    }
                }
                break;
        }
    }

    private void uploadImage(Uri selectedImageUri, UploadCallback callback) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        Storage.upload(imageFileName, selectedImageUri, callback);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private class ReadCommentsOfPostCallback extends ForegroundTaskDelegate<List<Comment>> {
        public ReadCommentsOfPostCallback(SpageActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExcute(List<Comment> comments, Throwable throwable) {
            super.onPostExcute(comments, throwable);
            if (comments != null) {
//                for (int i = 0; i < comments.size(); ++i) {
//                    commentList.add(comments.get(i));
//                }
                commentList.clear();
                commentList.addAll(comments);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class UpdateCommentCallback extends ForegroundTaskDelegate<EndPointResponse> {
        public UpdateCommentCallback(SpageActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExcute(EndPointResponse endPointResponse, Throwable throwable) {
            super.onPostExcute(endPointResponse, throwable);
            if (throwable != null) {
                Toast.makeText(getApplicationContext(), "Cannot update rating", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class CreateCommentCallback extends ForegroundTaskDelegate<EndPointResponse> {
        public CreateCommentCallback(SpageActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExcute(EndPointResponse endPointResponse, Throwable throwable) {
            super.onPostExcute(endPointResponse, throwable);
            if (throwable != null) {
                Toast.makeText(getApplicationContext(), "Cannot create comment", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
