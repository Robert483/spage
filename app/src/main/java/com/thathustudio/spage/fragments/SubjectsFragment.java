package com.thathustudio.spage.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thathustudio.spage.R;
import com.thathustudio.spage.activities.HomeActivity;
import com.thathustudio.spage.activities.SpageActivity;
import com.thathustudio.spage.activities.SubjectPostsActivity;
import com.thathustudio.spage.adapter.SubjectAdapter;
import com.thathustudio.spage.model.Subject;
import com.thathustudio.spage.model.Subscription;
import com.thathustudio.spage.model.User;
import com.thathustudio.spage.model.responses.EndPointResponse;
import com.thathustudio.spage.service.callback.ForegroundTaskDelegate;
import com.thathustudio.spage.utils.ShareReferrentHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectsFragment extends BaseFragment implements SubjectAdapter.OnSubjectItemClickListener {

    private final String TAG = SubjectsFragment.class.getSimpleName();

    private RecyclerView rvSubjects;
    private SubjectAdapter adapter;
    private List<Subject> subjectList = new ArrayList<>();
    User mUser;

    //Fake userId

    int userId = 1;

    public static SubjectsFragment newInstance() {
        SubjectsFragment fragment = new SubjectsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void getSubjectList() {
        //((SubjectPostsActivity) getActivity()).getSpageService().getSubjectList(new GetSubjectListCallback((SpageActivity) getActivity()));
        ((HomeActivity) getActivity()).getCustomApplication().getSpageService().getSubjectList(new GetSubjectListCallback((SpageActivity) getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subjects, container, false);
        setupView(rootView);
        setupEvent();
        mUser = ShareReferrentHelper.getCurrentUser(getContext());
//        User m

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getSubjectList();
        }else{
        }
    }

    private void setupView(View rootView) {
        rvSubjects = (RecyclerView) rootView.findViewById(R.id.rvSubjects);
        rvSubjects.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new SubjectAdapter(getActivity(), subjectList);
        adapter.setOnItemClickListener(this);
        rvSubjects.setAdapter(adapter);
    }

    private void setupEvent() {
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        //TODO: go to posts of this subject
        Subject subject = subjectList.get(position);
        Intent  i= new Intent(getActivity(),SubjectPostsActivity.class);
        i.putExtra("POST",subject);
        i.putExtra("USER",mUser);
        startActivity(i);
    }

    @Override
    public void onSubscribeChange(int position, boolean subscribed) {
        if (subscribed) {
            Subscription subscription = new Subscription();
            subscription.setSubjectId(subjectList.get(position).getId());
            subscription.setUserId(userId);
            ((HomeActivity) getActivity()).getCustomApplication().getSpageService().
                    createSubscription(subscription, new CreateSubscriptionCallback((SpageActivity) getActivity()));
        }
    }

    private class GetSubjectListCallback extends ForegroundTaskDelegate<List<Subject>> {
        public GetSubjectListCallback(SpageActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExcute(List<Subject> subjects, Throwable throwable) {
            super.onPostExcute(subjects, throwable);
            if (subjects != null) {
                subjectList.clear();
                subjectList.addAll(subjects);
                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(getActivity().getApplicationContext(), "Cannot get subject list", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CreateSubscriptionCallback extends ForegroundTaskDelegate<EndPointResponse> {

        public CreateSubscriptionCallback(SpageActivity activity) {
            super(activity);
        }

        @Override
        public void onPostExcute(EndPointResponse endPointResponse, Throwable throwable) {
            super.onPostExcute(endPointResponse, throwable);
            if (throwable != null) {
                Toast.makeText(getActivity().getApplicationContext(), "Cannot create subscription", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
