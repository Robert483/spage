package com.thathustudio.spage.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thathustudio.spage.R;
import com.thathustudio.spage.activities.HomeActivity;
import com.thathustudio.spage.app.CustomApplication;
import com.thathustudio.spage.exception.SpageException;
import com.thathustudio.spage.model.Post;
import com.thathustudio.spage.model.User;
import com.thathustudio.spage.model.responses.EndPointResponse;
import com.thathustudio.spage.model.responses.Task4ListResponse;
import com.thathustudio.spage.model.responses.Task4Response;
import com.thathustudio.spage.service.retrofit.TranslateRetrofitException;
import com.thathustudio.spage.utils.ShareReferrentHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USERID = "USERID";
    private static final String USERNAME = "USERNAME";
    private static final String EMAIL = "EMAIL";

    // TODO: Rename and change types of parameters
    private int mUserID;
    private String mUserName;
    private String mEmail;


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this,view);
        //init Data

        //init Layout
        txtUserName.setText(mUserName);
        txtEmail.setText(mEmail);

        return view;
    }


}
