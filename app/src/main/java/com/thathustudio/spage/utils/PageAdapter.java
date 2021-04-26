package com.thathustudio.spage.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thathustudio.spage.R;
import com.thathustudio.spage.fragments.ExercisesFragment;
import com.thathustudio.spage.fragments.PostsFragment;
import com.thathustudio.spage.fragments.ProfileFragment;
import com.thathustudio.spage.fragments.RoomsFragment;
import com.thathustudio.spage.fragments.SubjectsFragment;
import com.thathustudio.spage.model.User;

public class PageAdapter extends FragmentPagerAdapter {

    //integer to count number of tabs
    final int PAGE_COUNT = 5;
    private Context context;
    private int[] imageResId = {R.drawable.tab_home,R.drawable.tab_subject , R.drawable.tab_exercise,R.drawable.tab_challenge, R.drawable.tab_profile};

    //Constructor to the class
    public PageAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;

    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        // get user -> get id user
        User user = ShareReferrentHelper.getCurrentUser(context);
        int id = user.getId();


        //Returning the current tabs
        switch (position) {
            case 0:
                return PostsFragment.newInstance(user,PostsFragment.PAGE_NEWFEED);
            case 1:
                return SubjectsFragment.newInstance();
            case 2:
                return ExercisesFragment.newInstance(id);
            case 3:
                return RoomsFragment.newInstance(user.getUsername());
            case 4:
                return ProfileFragment.newInstance(user.getId(),user.getUsername(),user.getEmail());
        }
        return null;
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText("");
        ImageView img = (ImageView) v.findViewById(R.id.imgIconPage);
        img.setImageResource(imageResId[position]);
        return v;
    }


}

