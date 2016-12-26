package com.thathustudio.spage.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Exam;

import java.util.List;

public class ExamRecyclerViewAdapter extends RecyclerView.Adapter<ExamRecyclerViewAdapter.ExamViewHolder> implements SwipeableItemAdapter<ExamRecyclerViewAdapter.ExamViewHolder> {
    private ExamViewHolder pinnedViewHolder;
    private int pinnedPosition = -1;
    private final List<Exam> exams;
    private final OnExamViewInteractionListener listener;

    public ExamRecyclerViewAdapter(List<Exam> exams, OnExamViewInteractionListener listener) {
        this.exams = exams;
        this.listener = listener;

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
        holder.setMaxLeftSwipeAmount(-2f / 6);
        holder.setMaxRightSwipeAmount(0);
        holder.setSwipeItemHorizontalSlideAmount(holder.isPinned() ? -2f / 6 : 0);
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    @Override
    public int onGetSwipeReactionType(ExamViewHolder holder, int position, int x, int y) {
        if (pinnedViewHolder != null) {
            pinnedViewHolder.setPinned(false);
            notifyItemChanged(pinnedPosition);
            pinnedViewHolder = null;
            pinnedPosition = -1;
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
                pinnedViewHolder = holder;
                pinnedPosition = position;
                holder.setPinned(true);
                break;
            default:
                holder.setPinned(false);
                break;
        }

        return null;
    }

    public interface OnExamViewInteractionListener {

    }

    public static class ExamViewHolder extends AbstractSwipeableItemViewHolder {
        private boolean pinned;
        public Exam exam;
        public final FrameLayout frameLayoutContainer;
        public final TextView textViewExamName;

        public ExamViewHolder(View itemView) {
            super(itemView);
            frameLayoutContainer = (FrameLayout) itemView.findViewById(R.id.container);
            textViewExamName = (TextView) itemView.findViewById(R.id.txtV_examName);
        }

        @Override
        public View getSwipeableContainerView() {
            return frameLayoutContainer;
        }

        public boolean isPinned() {
            return pinned;
        }

        public void setPinned(boolean pinned) {
            this.pinned = pinned;
        }
    }
}
