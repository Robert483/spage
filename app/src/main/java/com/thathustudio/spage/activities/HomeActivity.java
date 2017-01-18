package com.thathustudio.spage.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.thathustudio.spage.R;
import com.thathustudio.spage.utils.PageAdapter;


public class HomeActivity extends AppCompatActivity
        implements TabLayout.OnTabSelectedListener {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initData();

        initLayout();

    }

    private void initLayout() {
        initTabLayout();

    }

    private void initTabLayout() {
        //Adding toolbar to the activity

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.pager);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), HomeActivity.this);
        viewPager.setAdapter(pageAdapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setSelectedTabIndicatorHeight(0);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pageAdapter.getTabView(i));
        }


        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);
        //tabLayout.getTabAt(0).getCustomView().setBackgroundColor(Color.WHITE);

    }

    private void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
