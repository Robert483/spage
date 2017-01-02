package com.thathustudio.spage.fragments;


import android.support.v4.app.Fragment;

import com.squareup.leakcanary.RefWatcher;
import com.thathustudio.spage.app.CustomApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = CustomApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
