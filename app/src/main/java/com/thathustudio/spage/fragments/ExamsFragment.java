package com.thathustudio.spage.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Exam;
import com.thathustudio.spage.utils.ExamRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExamsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters


    public ExamsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExamsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExamsFragment newInstance() {
        ExamsFragment fragment = new ExamsFragment();
        Bundle args = new Bundle();
        // TODO: Add args
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Handle args
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exams, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rclrView_exams);
        if (recyclerView != null) {
            // TODO: delete this and use Retrofit instead
            List<Exam> exams = new ArrayList<>();
            for (int i = 0; i < 25; i++) {
                exams.add(new Exam(i, "Test" + i));
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new ExamRecyclerViewAdapter(exams, null));
        }

        return view;
    }

}
