package com.thathustudio.spage.fragments.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Exercise;

import java.util.Locale;

/**
 * A simple {@link BaseDialogFragment} subclass.
 * Use the {@link ExerciseDetailsDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseDetailsDialogFragment extends BaseDialogFragment implements DialogInterface.OnShowListener {
    private static final String EXERCISE = "Exercise";
    public static final String EXERCISE_DETAILS = "Exercise Details";
    private Exercise exercise;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param exercise Exercise.
     * @return A new instance of fragment ExerciseDetailsDialogFragment.
     */
    public static ExerciseDetailsDialogFragment newInstance(Exercise exercise) {
        ExerciseDetailsDialogFragment fragment = new ExerciseDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXERCISE, exercise);
        fragment.setArguments(args);
        return fragment;
    }

    private String getSubjectName(int subjectId) {
        // TODO: delete this and load from server
        switch (subjectId) {
            case 1:
                return "Tiếng việt";
            case 2:
                return "Vật lý";
            default:
                return "Unknown";
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exercise = getArguments().getParcelable(EXERCISE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = View.inflate(getContext(), R.layout.fragment_dialog_exercise_details, null);
        TextView textView = (TextView) view.findViewById(R.id.txtV_exerciseName);
        textView.setText(exercise.getName());
        textView = (TextView) view.findViewById(R.id.txtV_exerciseContent);
        textView.setText(exercise.getContent());
        textView = (TextView) view.findViewById(R.id.txtV_exerciseSubject);
        textView.setText(getSubjectName(exercise.getSubjectId()));
        textView = (TextView) view.findViewById(R.id.txtV_exerciseDifficulty);
        textView.setText(exercise.getDifficulty());

        builder.setTitle(String.format(Locale.getDefault(), "%s: %d", getContext().getResources().getString(R.string.exercise_id), exercise.getId()))
                .setView(view);

        // Create the AlertDialog object and return it
        Dialog dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {
        Dialog dialog1 = getDialog();
        View scrollView = dialog1.findViewById(R.id.srlV_exercise);

        if (dialog1.getWindow() != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            WindowManager.LayoutParams params = dialog1.getWindow().getAttributes();
            int size = 3 * (metrics.heightPixels > metrics.widthPixels ? metrics.heightPixels : metrics.widthPixels) / 4;
            if (scrollView.getHeight() > size) {
                params.height = size;
                dialog1.getWindow().setAttributes(params);
            } else {
                dialog1.findViewById(R.id.txtV_dump).setVisibility(View.INVISIBLE);
            }
        }

        /*View divider = dialog1.findViewById(R.id.txtV_dump);
        View scrollView = dialog1.findViewById(R.id.srlV_exercise);
        View container = dialog1.findViewById(R.id.lnLyot_container);
        divider.setVisibility(scrollView.getHeight() < container.getHeight() + scrollView.getPaddingTop() + scrollView.getPaddingBottom() ? View.VISIBLE : View.INVISIBLE);*/
    }
}
