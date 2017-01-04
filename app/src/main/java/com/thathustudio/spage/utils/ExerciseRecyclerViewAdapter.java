package com.thathustudio.spage.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExerciseRecyclerViewAdapter extends RecyclerView.Adapter<ExerciseRecyclerViewAdapter.ExerciseViewHolder> implements SwipeableItemAdapter<ExerciseRecyclerViewAdapter.ExerciseViewHolder>, View.OnClickListener {
    private int pinnedPosition;
    private int currentPosition;
    private final List<Exercise> exercises;
    private final OnExerciseViewInteractionListener listener;
    private final SubjectIconFactory subjectIconFactory;

    public ExerciseRecyclerViewAdapter(Context context, OnExerciseViewInteractionListener listener) {
        this.exercises = new ArrayList<>();
        this.listener = listener;
        this.pinnedPosition = RecyclerView.NO_POSITION;
        this.currentPosition = RecyclerView.NO_POSITION;
        this.subjectIconFactory = new SubjectIconFactory(context);

        // SwipeableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    private void unpinPinnedItem() {
        int temp = pinnedPosition;
        pinnedPosition = RecyclerView.NO_POSITION;
        notifyItemChanged(temp);
    }

    public void replaceExercises(List<Exercise> exercises) {
        this.exercises.clear();
        this.exercises.addAll(exercises);
        notifyDataSetChanged();
    }

    public void unpinPinnedExercise() {
        if (pinnedPosition != RecyclerView.NO_POSITION) {
            unpinPinnedItem();
        }
    }

    @Override
    public long getItemId(int position) {
        return exercises.get(position).getId();
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        view.findViewById(R.id.imgBtn_exerciseInfo).setOnClickListener(this);
        view.findViewById(R.id.imgBtn_exerciseStart).setOnClickListener(this);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        holder.exercise = exercises.get(position);
        holder.textViewExerciseName.setText(holder.exercise.getName());
        holder.textViewExerciseDescription.setText(holder.exercise.getContent());
        holder.imageViewSubject.setImageDrawable(subjectIconFactory.getSubjectIcon(holder.exercise.getSubjectId()));

        // set swiping properties
        int underContainerWidth = holder.viewUnderContainer.getMeasuredWidth();
        holder.setProportionalSwipeAmountModeEnabled(false);
        holder.setMaxLeftSwipeAmount(-underContainerWidth);
        holder.setMaxRightSwipeAmount(0);
        holder.setSwipeItemHorizontalSlideAmount(pinnedPosition == position ? -underContainerWidth : 0);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    @Override
    public int onGetSwipeReactionType(ExerciseViewHolder holder, int position, int x, int y) {
        currentPosition = position;
        if (pinnedPosition != RecyclerView.NO_POSITION && pinnedPosition != position) {
            unpinPinnedItem();
        }
        return SwipeableItemConstants.REACTION_CAN_SWIPE_LEFT;
    }

    @Override
    public void onSetSwipeBackground(ExerciseViewHolder holder, int position, int type) {
        Log.v("SPage", String.format(Locale.US, "Set Swipe Background %d", position));
    }

    @Override
    public SwipeResultAction onSwipeItem(ExerciseViewHolder holder, int position, int result) {
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

    @Override
    public void onClick(View v) {
        if (listener != null) {
            Exercise exercise = exercises.get(currentPosition);

            switch (v.getId()) {
                case R.id.imgBtn_exerciseInfo:
                    listener.onExerciseInfoClick(exercise);
                    break;
                case R.id.imgBtn_exerciseStart:
                    listener.onExerciseStartClick(exercise);
                    break;
            }
        }
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public interface OnExerciseViewInteractionListener {
        void onExerciseInfoClick(Exercise exercise);

        void onExerciseStartClick(Exercise exercise);
    }

    public static class ExerciseViewHolder extends AbstractSwipeableItemViewHolder {
        public Exercise exercise;
        public final View viewUnderContainer;
        public final View viewContainer;
        public final TextView textViewExerciseName;
        public final TextView textViewExerciseDescription;
        public final ImageView imageViewSubject;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            viewUnderContainer = itemView.findViewById(R.id.lnLyot_underContainer);
            viewContainer = itemView.findViewById(R.id.rltLyot_container);
            textViewExerciseName = (TextView) itemView.findViewById(R.id.txtV_exerciseName);
            textViewExerciseDescription = (TextView) itemView.findViewById(R.id.txtV_exerciseContent);
            imageViewSubject = (ImageView) itemView.findViewById(R.id.imgV_exerciseSubject);
        }

        @Override
        public View getSwipeableContainerView() {
            return viewContainer;
        }
    }
}
