package com.thathustudio.spage.utils;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RankRecyclerViewAdapter extends RecyclerView.Adapter<RankRecyclerViewAdapter.ResultViewHolder> {
    private final List<Result> results;
    private final Locale locale;

    public RankRecyclerViewAdapter() {
        results = new ArrayList<>();
        locale = Locale.getDefault();
    }

    public void replaceResults(List<Result> results) {
        this.results.clear();
        this.results.addAll(results);
        notifyDataSetChanged();
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        holder.result = results.get(position);
        holder.textViewUserName.setText(holder.result.getUserName());
        holder.textViewScore.setText(String.format(locale, "%1$.2f", 1.0 * holder.result.getScore() / 100));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        public Result result;
        public final TextView textViewUserName;
        public final TextView textViewScore;

        public ResultViewHolder(View itemView) {
            super(itemView);
            textViewUserName = (TextView) itemView.findViewById(R.id.txtV_rankUserName);
            textViewScore = (TextView) itemView.findViewById(R.id.txtV_rankScore);
        }
    }
}
