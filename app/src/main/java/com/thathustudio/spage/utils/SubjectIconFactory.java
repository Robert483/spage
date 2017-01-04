package com.thathustudio.spage.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SparseArrayCompat;

import com.thathustudio.spage.R;

public class SubjectIconFactory {
    private final SparseArrayCompat<Drawable> icons;
    private final Drawable defaultIcon;

    public SubjectIconFactory(Context context) {
        defaultIcon = ContextCompat.getDrawable(context, R.drawable.ic_sbj_unknown);

        int[] ids = {R.drawable.ic_sbj_literature, R.drawable.ic_sbj_physics};
        icons = new SparseArrayCompat<>(ids.length);
        for (int i = 0, len = ids.length; i < len; i++) {
            icons.put(i + 1, ContextCompat.getDrawable(context, ids[i]));
        }
    }

    public Drawable getSubjectIcon(int susbjectId) {
        Drawable icon = icons.get(susbjectId);
        return icon != null ? icon : defaultIcon;
    }
}
