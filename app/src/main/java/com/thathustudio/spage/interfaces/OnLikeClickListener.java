package com.thathustudio.spage.interfaces;

import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by apple on 1/18/17.
 */

public interface OnLikeClickListener {
    public void onLikeClick(ProgressBar progressBar,View v, int posistion, boolean liked);
}
