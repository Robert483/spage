package com.thathustudio.spage.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Exam;

import java.util.List;

public class ExamRecyclerViewAdapter extends RecyclerView.Adapter<ExamRecyclerViewAdapter.ExamViewHolder> {
    private final List<Exam> exams;
    private final OnExamViewInteractionListener listener;

    public ExamRecyclerViewAdapter(List<Exam> exams, OnExamViewInteractionListener listener) {
        this.exams = exams;
        this.listener = listener;
    }

    @Override
    public ExamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExamViewHolder holder, int position) {
        holder.exam = exams.get(position);
        holder.textViewExamName.setText(holder.exam.getName());
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    public static class ExamViewHolder extends RecyclerView.ViewHolder {
        public Exam exam;
        public final TextView textViewExamName;

        public ExamViewHolder(View itemView) {
            super(itemView);
            textViewExamName = (TextView) itemView.findViewById(R.id.txtV_examName);
        }
    }

    public interface OnExamViewInteractionListener {

    }
}
