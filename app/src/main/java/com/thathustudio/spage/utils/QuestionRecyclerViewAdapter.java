package com.thathustudio.spage.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Question;

import java.util.List;

public class QuestionRecyclerViewAdapter extends RecyclerView.Adapter<QuestionRecyclerViewAdapter.QuestionViewHolder> {
    private final List<Question> questions;
    private final OnQuestionViewInteractionListener listener;

    public QuestionRecyclerViewAdapter(List<Question> questions, OnQuestionViewInteractionListener listener) {
        this.questions = questions;
        this.listener = listener;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        holder.question = questions.get(position);
        holder.textViewQuestionContent.setText(holder.question.getContent());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public interface OnQuestionViewInteractionListener {

    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        public Question question;
        public final TextView textViewQuestionContent;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            textViewQuestionContent = (TextView) itemView.findViewById(R.id.txtV_questionContent);
        }
    }
}
