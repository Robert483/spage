package com.thathustudio.spage.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.thathustudio.spage.R;
import com.thathustudio.spage.fragments.SubjectsFragment;
import com.thathustudio.spage.service.SpageService;

public class SubjectPostsActivity extends SpageActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SubjectsFragment fragment = SubjectsFragment.newInstance();
        commitFragment(fragment);
    }

    @Override
    protected int getRootLayoutRes() {
        return R.layout.activity_subject_posts;
    }

    private void commitFragment(SubjectsFragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment, "Subjects");
        ft.commit();
    }

    public SpageService getSpageService(){
        return spageService;
    }
}
