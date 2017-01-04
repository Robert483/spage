package com.thathustudio.spage.fragments.dialogs;

import android.support.v7.app.AppCompatDialogFragment;

import com.squareup.leakcanary.RefWatcher;
import com.thathustudio.spage.app.CustomApplication;

public class BaseDialogFragment extends AppCompatDialogFragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = CustomApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
