package com.thathustudio.spage.fragments;


import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Exam;
import com.thathustudio.spage.utils.ExamRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link BaseFragment} subclass.
 * Use the {@link ExamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamsFragment extends BaseFragment implements ExamRecyclerViewAdapter.OnExamViewInteractionListener {

    public static ExamsFragment newInstance() {
        return new ExamsFragment();
    }

    private void recyclerViewInit(RecyclerView recyclerView) {
        // TODO: delete this and use Retrofit instead
        List<Exam> exams = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            exams.add(new Exam(i, "Test" + i));
        }

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        RecyclerViewTouchActionGuardManager touchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        touchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        touchActionGuardManager.setEnabled(true);

        // swipe manager
        RecyclerViewSwipeManager swipeManager = new RecyclerViewSwipeManager();

        // adapter
        RecyclerView.Adapter adapter = new ExamRecyclerViewAdapter(exams, this);
        RecyclerView.Adapter wrappedAdapter = swipeManager.createWrappedAdapter(adapter); // wrap for swiping

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Disable the change animation in order to make turning back animation of swiped item works properly.
        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();
        animator.setSupportsChangeAnimations(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(wrappedAdapter);  // requires *wrapped* adapter
        recyclerView.setItemAnimator(animator);

        // additional decorations
        // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)) {
            recyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.nine_patch_material_shadow_z1)));
        }

        // NOTE:
        // The initialization order is very important! This order determines the priority of touch event handling.
        //
        // priority: TouchActionGuard > Swipe > DragAndDrop
        touchActionGuardManager.attachRecyclerView(recyclerView);
        swipeManager.attachRecyclerView(recyclerView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exams, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rclrView_exams);
        if (recyclerView != null) {
            recyclerViewInit(recyclerView);
        }

        return view;
    }

    @Override
    public void onExamInfoClick(Exam exam) {
        Log.v("My tag", "Exam info with id = " + exam.getId() + " clicked");
    }

    @Override
    public void onExamStartClick(Exam exam) {
        Log.v("My tag", "Exam start with id = " + exam.getId() + " clicked");
    }
}
