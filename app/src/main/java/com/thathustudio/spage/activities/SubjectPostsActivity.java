package com.thathustudio.spage.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.thathustudio.spage.R;
import com.thathustudio.spage.fragments.PostsFragment;
import com.thathustudio.spage.fragments.SubjectsFragment;
import com.thathustudio.spage.model.Subject;
import com.thathustudio.spage.model.User;
import com.thathustudio.spage.service.SpageService;

public class SubjectPostsActivity extends SpageActivity {

    Subject mSubject;
    User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getExtraData();
        setupToolbar();
        addPostFragment();



    }


    void setupToolbar(){
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        toolbar.setTitle(mSubject.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addPostFragment() {
        PostsFragment fragment = PostsFragment.newInstance(mUser,mSubject,PostsFragment.PAGE_SUBJECT);
        commitFragment(fragment);
    }

    void getExtraData(){
        mSubject = (Subject) getIntent().getSerializableExtra("POST");
        mUser = (User) getIntent().getSerializableExtra("USER");
    }

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_subject_posts;
    }

    private void commitFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment, "Subjects");
        ft.commit();
    }

    public SpageService getSpageService(){
        return spageService;
    }
}
