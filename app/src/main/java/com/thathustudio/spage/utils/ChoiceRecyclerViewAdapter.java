package com.thathustudio.spage.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.thathustudio.spage.R;

import java.util.ArrayList;
import java.util.List;

public class ChoiceRecyclerViewAdapter extends RecyclerView.Adapter<ChoiceRecyclerViewAdapter.ChoiceViewHolder> {
    private final List<String> choices;

    public ChoiceRecyclerViewAdapter() {
        this.choices = new ArrayList<>();
    }

    public void replaceChoices(List<String> choices) {
        this.choices.clear();
        this.choices.addAll(choices);
        notifyDataSetChanged();
    }

    @Override
    public ChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice, parent, false);
        return new ChoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChoiceViewHolder holder, int position) {
        holder.choice = choices.get(position);
        holder.radioButtonChoice.setText(holder.choice);
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public static class ChoiceViewHolder extends RecyclerView.ViewHolder {
        public String choice;
        public RadioButton radioButtonChoice;

        public ChoiceViewHolder(View itemView) {
            super(itemView);
            radioButtonChoice = (RadioButton) itemView.findViewById(R.id.rdBtn_choice);
        }
    }
}
