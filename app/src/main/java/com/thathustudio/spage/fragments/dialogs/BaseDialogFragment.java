package com.thathustudio.spage.fragments.dialogs;

import android.support.v4.app.DialogFragment;

import com.squareup.leakcanary.RefWatcher;
import com.thathustudio.spage.CustomApplication;

public class BaseDialogFragment extends DialogFragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = CustomApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
