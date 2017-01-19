package com.thathustudio.spage.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thathustudio.spage.R;
import com.thathustudio.spage.activities.LoginActivity;
import com.thathustudio.spage.app.CustomApplication;
import com.thathustudio.spage.exception.SpageException;
import com.thathustudio.spage.firebase.Storage;
import com.thathustudio.spage.firebase.UploadCallback;
import com.thathustudio.spage.model.User;
import com.thathustudio.spage.model.responses.EndPointResponse;
import com.thathustudio.spage.model.responses.Task4Response;
import com.thathustudio.spage.service.retrofit.TranslateRetrofitException;
import com.thathustudio.spage.utils.ShareReferrentHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment implements UploadCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USERID = "USERID";
    private static final String USERNAME = "USERNAME";
    private static final String EMAIL = "EMAIL";
    private static final int OPEN_GALLERY = 100;

    // TODO: Rename and change types of parameters
    private int mUserID;
    private String mUserName;
    private String mEmail;
    private List<Call> calls;
    private User newUser;



    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(int id, String userName, String email) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(USERID, id);
        args.putString(USERNAME, userName);
        args.putString(EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserID = getArguments().getInt(USERID);
            mUserName = getArguments().getString(USERNAME);
            mEmail = getArguments().getString(EMAIL);
        }
    }

    @BindView(R.id.txtUserName) TextView  txtUserName;
    @BindView(R.id.txtEmail) TextView  txtEmail;
    @BindView(R.id.imgBtn_userAvatar) ImageButton imgBtn_userAvatar;
    @BindView(R.id.imgUserLogout) ImageView imgUserLogout;
    @BindView(R.id.imgUserProfileEdit) ImageView imgUserProfileEdit;
    View progressbarView = null;
    Dialog dialogEditProfile = null;

    @OnClick(R.id.imgUserProfileEdit)
    public void onClickedEditProfile(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_edit_profile);

        //init layout
        final EditText etEmail = (EditText) dialog.findViewById(R.id.etEmail);
        final EditText etPass = (EditText) dialog.findViewById(R.id.etPass);
        final EditText etUserName = (EditText) dialog.findViewById(R.id.etUserName);

        newUser = ShareReferrentHelper.getCurrentUser(getContext());
        etEmail.setText(newUser.getEmail());
        etPass.setText(newUser.getPassword());
        etUserName.setText(newUser.getUsername());

        //btn edit
        Button btnEdit = (Button) dialog.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbarView.setVisibility(View.VISIBLE);
                newUser.setEmail(etEmail.getText().toString());
                newUser.setPassword(etPass.getText().toString());
                newUser.setUsername(etUserName.getText().toString());

                CustomApplication customApplication = (CustomApplication) getActivity().getApplication();
                Call<Task4Response<Integer>> editProfileResponseCall = customApplication.getTask4Service().editProflie(newUser.getId(), newUser);
                editProfileResponseCall.enqueue(new EditProfileCallback(ProfileFragment.this));
                calls.add(editProfileResponseCall);
            }
        });

        //btn cancel
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogEditProfile = dialog;
        progressbarView = dialogEditProfile.findViewById(R.id.progressbarView);
        //show dialog
        dialogEditProfile.show();

    }

    @OnClick(R.id.imgUserLogout)
    public void onClickLogout(){
        ShareReferrentHelper.removeCurrentUser(getContext());
        Intent intent = new Intent(getContext().getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.imgBtn_userAvatar)
    public void onClickedEditAavatar(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), OPEN_GALLERY);

    }


    private void addPostFragment() {
        User mUser = ShareReferrentHelper.getCurrentUser(getContext());
        PostsFragment fragment = PostsFragment.newInstance(mUser,PostsFragment.PAGE_MYPOST);
        commitFragment(fragment);
    }


    private void commitFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment, "Subjects");
        ft.commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);
        //init Data
        calls = new ArrayList<>();
        addPostFragment();

        //init Layout
        initLayout();

        return view;
    }

    private void initLayout() {
        txtUserName.setText(mUserName);
        txtEmail.setText(mEmail);
        Storage.displayImage(getContext(), imgBtn_userAvatar, "" + mUserID);
    }

    @Override
    public void onPause() {
        super.onPause();

        for (Call call : calls) {
            call.cancel();
        }
        calls.clear();
//        FragmentManager fm = getChildFragmentManager();
//        Fragment post_fragment1 = (fm.findFragmentById(R.id.post_fragment));
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.remove(post_fragment1);
//        ft.commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case OPEN_GALLERY:
                {
                    Uri uri = data.getData();
                    if (uri != null)
                    {
                        Storage.upload("" + mUserID, uri, this);
                    }

                }
            }
        }
    }

    @Override
    public void onUploadSuccess(String url) {
        Log.e("firebaseStorage", url);

        // display image to ImageView
        try
        {
            Storage.displayImage(getContext(), this.imgBtn_userAvatar, "" + mUserID);
        }
        catch (Exception e)
        {
            Log.e("firebaseStorage", e.getMessage());
        }
    }

    public static class EditProfileCallback extends ProfileFragmentCallback<Task4Response<Integer>> {
        public EditProfileCallback(ProfileFragment profileFragment) {
            super(profileFragment);
        }

        @Override
        public void onResponse(Call<Task4Response<Integer>> call, Response<Task4Response<Integer>> response) {
            ProfileFragment profileFragment = weakReference.get();
            if (profileFragment != null) {
                SpageException spageException = TranslateRetrofitException.translateServiceException(call, response);

                try {
                    if (spageException != null) {
                        showToast(profileFragment.getContext().getApplicationContext(), spageException); // Fix warning
                        return;
                    }

                    Log.e("Son", "login: " + response.body().getResponse().toString());
                    //save data user
                    if(response.body().getResponse() != profileFragment.newUser.getId()) {
                        Toast.makeText(profileFragment.getContext(), "Wrong ID user", Toast.LENGTH_LONG).show();
                        return;
                    }

                    ShareReferrentHelper.setCurrentUser(profileFragment.getContext().getApplicationContext(), profileFragment.newUser);
                    //update layout
                    profileFragment.mUserID = profileFragment.newUser.getId();
                    profileFragment.mUserName = profileFragment.newUser.getUsername();
                    profileFragment.mEmail = profileFragment.newUser.getEmail();

                    profileFragment.txtUserName.setText(profileFragment.mUserName);
                    profileFragment.txtEmail.setText(profileFragment.mEmail);
                    profileFragment.dialogEditProfile.dismiss();


                } catch (Exception ex) {
                    Log.e("SPage", ex.getMessage());
                } finally {
                    profileFragment.calls.remove(call);
                    profileFragment.progressbarView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public static abstract class ProfileFragmentCallback<T extends EndPointResponse> implements Callback<T> {
        protected final WeakReference<ProfileFragment> weakReference;

        public ProfileFragmentCallback(ProfileFragment profileFragment) {
            weakReference = new WeakReference<>(profileFragment);
        }

        protected void showToast(Context context, Throwable throwable) {
            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            ProfileFragment profileFragment = weakReference.get();
            if (profileFragment != null) {
                try {
                    // Handle
                    showToast(profileFragment.getContext().getApplicationContext(), t);
                } catch (Exception ex) {
                    Log.e("SPage", ex.getMessage());
                } finally {
                    profileFragment.calls.remove(call);
                    profileFragment.progressbarView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

}
