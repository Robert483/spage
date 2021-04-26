package com.thathustudio.spage.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.thathustudio.spage.R;
import com.thathustudio.spage.fragments.dialogs.BaseDialogFragment;


public class SpageProgressDialogFragment extends BaseDialogFragment {

    public static SpageProgressDialogFragment newInstance() {
        SpageProgressDialogFragment frag = new SpageProgressDialogFragment();
        frag.setCancelable(false);
        return frag;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_progress, container, false);
    }

}
