package com.thathustudio.spage.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.thathustudio.spage.R;

import java.util.ArrayList;
import java.util.List;

public class ChoiceRecyclerViewAdapter extends RecyclerView.Adapter<ChoiceRecyclerViewAdapter.ChoiceViewHolder> implements View.OnClickListener {
    private int checkedPosition;
    private final List<String> choices;

    public ChoiceRecyclerViewAdapter() {
        this.choices = new ArrayList<>();
        this.checkedPosition = RecyclerView.NO_POSITION;
    }

    private static String getAlphabetId(int numberId) {
        int dividend = numberId;
        String alphabetId = "";
        int modulo;

        while (dividend > 0) {
            modulo = (dividend - 1) % 26;
            char c = (char) (65 + modulo);
            alphabetId = c + alphabetId;
            dividend = (dividend - modulo) / 26;
        }

        return alphabetId;
    }

    public void replaceChoices(List<String> choices, int userChoice) {
        this.choices.clear();
        this.choices.addAll(choices);
        this.checkedPosition = userChoice;
        notifyDataSetChanged();
    }

    public int getUserChoiceIndex() {
        return checkedPosition;
    }

    @Override
    public ChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice, parent, false);
        view.findViewById(R.id.rdBtn_choice).setOnClickListener(this);
        return new ChoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChoiceViewHolder holder, int position) {
        holder.choice = choices.get(position);
        holder.radioButtonChoice.setText(getAlphabetId(position + 1) + ". " + holder.choice);
        holder.radioButtonChoice.setTag(position);
        holder.radioButtonChoice.setChecked(checkedPosition == position);
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rdBtn_choice:
                int newPosition = (int) v.getTag();
                int oldPosition = this.checkedPosition;
                if (oldPosition != newPosition) {
                    this.checkedPosition = newPosition;
                    notifyItemChanged(oldPosition);
                }
                break;
        }
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
