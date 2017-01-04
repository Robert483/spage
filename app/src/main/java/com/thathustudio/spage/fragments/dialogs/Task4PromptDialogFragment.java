package com.thathustudio.spage.fragments.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.thathustudio.spage.R;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

/**
 * A simple {@link BaseDialogFragment} subclass.
 * Use the {@link Task4PromptDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Task4PromptDialogFragment extends BaseDialogFragment implements DialogInterface.OnClickListener, DialogInterface.OnShowListener {
    private static final String TITLE = "Title";
    private static final String MESSAGE = "Message";
    private static final String POSITIVE = "Positive";
    private static final String NEGATIVE = "Negative";
    public static final int DONT_USE = -1;
    public static final String TASK4_PROMPT = "Task4 Prompt";
    // Use this instance of the interface to deliver action events
    private OnTask4PromptDialogInteractionListener listener;
    private int title;
    private int message;
    private int positive;
    private int negative;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title   Title.
     * @param message Message.
     * @return A new instance of fragment Task4PromptDialogFragment.
     */
    public static Task4PromptDialogFragment newInstance(int title, int message, int positive, int negative) {
        Task4PromptDialogFragment fragment = new Task4PromptDialogFragment();
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        args.putInt(MESSAGE, message);
        args.putInt(POSITIVE, positive);
        args.putInt(NEGATIVE, negative);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getInt(TITLE);
            message = getArguments().getInt(MESSAGE);
            positive = getArguments().getInt(POSITIVE);
            negative = getArguments().getInt(NEGATIVE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (title != DONT_USE) {
            builder.setTitle(title);
        }
        if (message != DONT_USE) {
            builder.setMessage(message);
        }
        if (positive != DONT_USE) {
            builder.setPositiveButton(positive, this);
        }
        if (negative != DONT_USE) {
            builder.setNegativeButton(negative, this);
        }

        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (OnTask4PromptDialogInteractionListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement OnTask4PromptDialogInteractionListener");
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                listener.onDialogPositiveClick(this);
                break;
            case BUTTON_NEGATIVE:
                listener.onDialogNegativeClick(this);
                break;
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        AlertDialog dialog1 = (AlertDialog) getDialog();
        dialog1.getButton(BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        dialog1.getButton(BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface OnTask4PromptDialogInteractionListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }
}
