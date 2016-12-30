package com.thathustudio.spage.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Question;
import com.thathustudio.spage.utils.QuestionRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    public static final String EXAM_ID = "Exam ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        int examId = getIntent().getIntExtra(QuestionsActivity.EXAM_ID, -1);

        // TODO: delete this and use Retrofit instead
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            questions.add(new Question("Test " + i, null));
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rclrView_questions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new QuestionRecyclerViewAdapter(questions, null));
    }
}
