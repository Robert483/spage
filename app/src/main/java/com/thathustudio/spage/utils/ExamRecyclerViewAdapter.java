package com.thathustudio.spage.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Exam;

import java.util.List;

public class ExamRecyclerViewAdapter extends RecyclerView.Adapter<ExamRecyclerViewAdapter.ExamViewHolder> implements SwipeableItemAdapter<ExamRecyclerViewAdapter.ExamViewHolder> {
    private int pinnedPosition;
    private final List<Exam> exams;
    private final OnExamViewInteractionListener listener;

    public ExamRecyclerViewAdapter(List<Exam> exams, OnExamViewInteractionListener listener) {
        this.exams = exams;
        this.listener = listener;
        this.pinnedPosition = RecyclerView.NO_POSITION;

        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return exams.get(position).getId();
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

        // set swiping properties
        int underContainerWidth = holder.viewUnderContainer.getWidth();
        holder.setMaxLeftSwipeAmount(-underContainerWidth);
        holder.setMaxRightSwipeAmount(0);
        holder.setSwipeItemHorizontalSlideAmount(pinnedPosition == position ? -underContainerWidth : 0);
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    @Override
    public int onGetSwipeReactionType(ExamViewHolder holder, int position, int x, int y) {
        if (pinnedPosition != RecyclerView.NO_POSITION && pinnedPosition != position) {
            int temp = pinnedPosition;
            pinnedPosition = RecyclerView.NO_POSITION;
            notifyItemChanged(temp);
        }
        return SwipeableItemConstants.REACTION_CAN_SWIPE_LEFT;
    }

    @Override
    public void onSetSwipeBackground(ExamViewHolder holder, int position, int type) {
        // TODO
    }

    @Override
    public SwipeResultAction onSwipeItem(ExamViewHolder holder, int position, int result) {
        switch (result) {
            case SwipeableItemConstants.RESULT_SWIPED_LEFT:
                pinnedPosition = position;
                break;
            default:
                pinnedPosition = RecyclerView.NO_POSITION;
                break;
        }

        return null;
    }

    public interface OnExamViewInteractionListener {

    }

    public static class ExamViewHolder extends AbstractSwipeableItemViewHolder {
        public Exam exam;
        public final View viewUnderContainer;
        public final View viewContainer;
        public final TextView textViewExamName;

        public ExamViewHolder(View itemView) {
            super(itemView);
            viewUnderContainer = itemView.findViewById(R.id.lnLyot_underContainer);
            viewContainer = itemView.findViewById(R.id.lnLyot_container);
            textViewExamName = (TextView) itemView.findViewById(R.id.txtV_examName);
            setProportionalSwipeAmountModeEnabled(false);
        }

        @Override
        public View getSwipeableContainerView() {
            return viewContainer;
        }
    }
}
