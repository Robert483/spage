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
import com.thathustudio.spage.fragments.SubjectsFragment;
import com.thathustudio.spage.model.User;

/**
 * Created by SonPham on 1/16/2017.
 */
public class PageAdapter extends FragmentPagerAdapter {

    //integer to count number of tabs
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Subjects", "Exams", "Profile" };
    private Context context;
    private int[] imageResId = {R.drawable.ic_sbj_informatics, R.drawable.ic_sbj_literature, R.drawable.ic_sbj_english};

    //Constructor to the class
    public PageAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;

    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        // get user -> get id user
        User user = ShareReferrentHelper.getTempUser(context);
        int id = user.getId();


        //Returning the current tabs
        switch (position) {
            case 0:
                SubjectsFragment subjectsFragment = SubjectsFragment.newInstance("","");
                return subjectsFragment;
            case 1:
                ExercisesFragment exercisesFragment = ExercisesFragment.newInstance(id);
                return exercisesFragment;
            case 2:
                ProfileFragment profileFragment = ProfileFragment.newInstance("","");
                return  profileFragment;
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
        tv.setText(tabTitles[position]);
        ImageView img = (ImageView) v.findViewById(R.id.imgIconPage);
        img.setImageResource(imageResId[position]);
        return v;
    }


}

