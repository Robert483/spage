package com.thathustudio.spage.utils;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Question;

import java.util.List;

public class QuestionRecyclerViewAdapter extends AbstractExpandableItemAdapter<QuestionRecyclerViewAdapter.QuestionContentViewHolder, QuestionRecyclerViewAdapter.ChoiceViewHolder> implements View.OnClickListener {
    private RecyclerViewExpandableItemManager manager;
    private final List<Question> questions;

    public QuestionRecyclerViewAdapter(RecyclerViewExpandableItemManager manager, List<Question> questions) {
        this.manager = manager;
        this.questions = questions;

        // ExpandableItemAdapter requires stable ID, and also
        // have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
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

    @Override
    public int getGroupCount() {
        return questions.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return questions.get(groupPosition).getChoices().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public QuestionContentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionContentViewHolder(view);
    }

    @Override
    public ChoiceViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice, parent, false);
        view.findViewById(R.id.rdBtn_choice).setOnClickListener(this);
        return new ChoiceViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(QuestionContentViewHolder holder, int groupPosition, int viewType) {
        holder.question = questions.get(groupPosition);
        holder.textViewQuestionContent.setText(holder.question.getContent());
    }

    @Override
    public void onBindChildViewHolder(ChoiceViewHolder holder, int groupPosition, int childPosition, int viewType) {
        Question question = questions.get(groupPosition);
        holder.choice = question.getChoices().get(childPosition);
        holder.radioButtonChoice.setText(getAlphabetId(childPosition + 1) + ". " + holder.choice);
        holder.radioButtonChoice.setTag(new Position(groupPosition, childPosition));
        holder.radioButtonChoice.setChecked(question.getUserChoice() == childPosition);

        if (childPosition == question.getChoices().size() - 1) {
            holder.itemView.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.fake_card_bottom));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white));
        }
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(QuestionContentViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rdBtn_choice:
                Position position = (Position) v.getTag();
                Question question = questions.get(position.getGroupPosition());
                int newPosition = position.getChildPosition();
                int oldPosition = question.getUserChoice();
                if (oldPosition != newPosition) {
                    question.setUserChoice(newPosition);
                    manager.notifyChildItemChanged(position.groupPosition, oldPosition);
                }
                break;
        }
    }

    private static class Position {
        private final int groupPosition;
        private final int childPosition;

        public Position(int groupPosition, int childPosition) {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
        }

        public int getGroupPosition() {
            return groupPosition;
        }

        public int getChildPosition() {
            return childPosition;
        }
    }

    public static class QuestionContentViewHolder extends AbstractExpandableItemViewHolder {
        public Question question;
        public final TextView textViewQuestionContent;

        public QuestionContentViewHolder(View itemView) {
            super(itemView);
            textViewQuestionContent = (TextView) itemView.findViewById(R.id.txtV_questionContent);
        }
    }

    public static class ChoiceViewHolder extends AbstractExpandableItemViewHolder {
        public String choice;
        public final RadioButton radioButtonChoice;

        public ChoiceViewHolder(View itemView) {
            super(itemView);
            radioButtonChoice = (RadioButton) itemView.findViewById(R.id.rdBtn_choice);
        }
    }
}
