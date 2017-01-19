package com.thathustudio.spage.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.thathustudio.spage.R;

import java.util.List;
import java.util.Locale;

public class ResultRecyclerViewAdapter extends RecyclerView.Adapter<ResultRecyclerViewAdapter.ResultViewHolder> {
    private final List<Boolean> results;
    private final String questionPrefix;
    private final Drawable correctDrawable;
    private final Drawable wrongDrawable;
    private final Locale locale;

    public ResultRecyclerViewAdapter(Context context, List<Boolean> results) {
        this.results = results;
        this.questionPrefix = context.getResources().getString(R.string.question);
        this.correctDrawable = ContextCompat.getDrawable(context, R.drawable.bg_it_result_correct);
        this.wrongDrawable = ContextCompat.getDrawable(context, R.drawable.bg_it_result_wrong);
        this.locale = Locale.getDefault();
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        holder.result = results.get(position);
        holder.textViewQuestionNo.setText(String.format(locale, "%s %d", questionPrefix, position + 1));
        holder.textViewCorrect.setBackground(holder.result ? correctDrawable : wrongDrawable);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        public boolean result;
        public final TextView textViewQuestionNo;
        public final TextView textViewCorrect;

        public ResultViewHolder(View itemView) {
            super(itemView);
            textViewQuestionNo = (TextView) itemView.findViewById(R.id.txtV_questionNo);
            textViewCorrect = (TextView) itemView.findViewById(R.id.txtV_correct);
        }
    }
}
