package com.thathustudio.spage.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thathustudio.spage.R;

import java.util.List;

public class ResultRecyclerViewAdapter extends RecyclerView.Adapter<ResultRecyclerViewAdapter.ResultViewHolder> {
    private List<Boolean> results;
    private String questionPrefix;

    public ResultRecyclerViewAdapter(Context context, List<Boolean> results) {
        this.results = results;
        this.questionPrefix = context.getResources().getString(R.string.question);
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        holder.result = results.get(position);
        holder.textViewQuestionNo.setText(questionPrefix + " " + position);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        public boolean result;
        public final TextView textViewQuestionNo;

        public ResultViewHolder(View itemView) {
            super(itemView);
            textViewQuestionNo = (TextView) itemView.findViewById(R.id.txtV_questionNo);
        }
    }
}
