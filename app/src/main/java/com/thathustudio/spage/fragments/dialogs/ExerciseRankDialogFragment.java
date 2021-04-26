package com.thathustudio.spage.fragments.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Result;
import com.thathustudio.spage.utils.RankRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * A simple {@link BaseDialogFragment} subclass.
 * Use the {@link ExerciseRankDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseRankDialogFragment extends BaseDialogFragment implements DialogInterface.OnShowListener, View.OnLayoutChangeListener {
    private static final String EXERCISE_ID = "Exercise ID";
    public static final String EXERCISE_RANK = "Exercise Rank";
    private int exerciseId;
    private RankRecyclerViewAdapter adapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param exerciseId Exercise id.
     * @return A new instance of fragment ExerciseDetailsDialogFragment.
     */
    public static ExerciseRankDialogFragment newInstance(int exerciseId) {
        ExerciseRankDialogFragment fragment = new ExerciseRankDialogFragment();
        Bundle args = new Bundle();
        args.putInt(EXERCISE_ID, exerciseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseId = getArguments().getInt(EXERCISE_ID);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = View.inflate(getContext(), R.layout.fragment_dialog_exercise_rank, null);

        builder.setTitle(String.format(Locale.getDefault(), "%s: %d", getContext().getResources().getString(R.string.exercise_id), exerciseId))
                .setView(view);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rclrV_exerciseRank);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RankRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        // Create the AlertDialog object and return it
        Dialog dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {
        Dialog dialog1 = getDialog();
        View recyclerView = dialog1.findViewById(R.id.rclrV_exerciseRank);

        Random random = new Random();
        List<Result> results = new ArrayList<>();
        for (int i = 0, len = random.nextInt(5); i < len; i++) {
            results.add(new Result("pkh"));
        }
        for (int i = 0, len = random.nextInt(5); i < len; i++) {
            results.add(new Result("lq"));
        }
        for (int i = 0, len = random.nextInt(5); i < len; i++) {
            results.add(new Result("sonpham"));
        }
        Collections.shuffle(results);
        for (Result result : results) {
            result.setScore(random.nextInt(101));
        }
        Collections.sort(results, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                if (o1.getScore() < o2.getScore())
                    return 1;
                else if (o1.getScore() == o2.getScore())
                    return 0;

                return -1;
            }
        });
        adapter.replaceResults(results);
        recyclerView.addOnLayoutChangeListener(this);

        /*View divider = dialog1.findViewById(R.id.txtV_dump);
        View scrollView = dialog1.findViewById(R.id.srlV_exercise);
        View container = dialog1.findViewById(R.id.lnLyot_container);
        divider.setVisibility(scrollView.getHeight() < container.getHeight() + scrollView.getPaddingTop() + scrollView.getPaddingBottom() ? View.VISIBLE : View.INVISIBLE);*/
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Dialog dialog = getDialog();
        if (dialog.getWindow() != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            int size = 7 * (metrics.heightPixels > metrics.widthPixels ? metrics.heightPixels : metrics.widthPixels) / 10;
            if (bottom - top > size) {
                params.height = size;
                dialog.getWindow().setAttributes(params);
            } else {
                dialog.findViewById(R.id.txtV_dump).setVisibility(View.INVISIBLE);
            }
        }
        dialog.findViewById(R.id.rclrV_exerciseRank).removeOnLayoutChangeListener(this);
    }
}
