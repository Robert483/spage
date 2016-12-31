package com.thathustudio.spage.utils;

import android.support.v7.widget.LinearLayoutManager;
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

    public QuestionRecyclerViewAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rclrV_choices);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new ChoiceRecyclerViewAdapter());
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        holder.question = questions.get(position);
        holder.textViewQuestionContent.setText(holder.question.getContent());
        ChoiceRecyclerViewAdapter adapter = (ChoiceRecyclerViewAdapter) holder.recyclerViewChoices.getAdapter();
        adapter.replaceChoices(holder.question.getChoices());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        public Question question;
        public final TextView textViewQuestionContent;
        public final RecyclerView recyclerViewChoices;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            textViewQuestionContent = (TextView) itemView.findViewById(R.id.txtV_questionContent);
            recyclerViewChoices = (RecyclerView) itemView.findViewById(R.id.rclrV_choices);
        }
    }
}
