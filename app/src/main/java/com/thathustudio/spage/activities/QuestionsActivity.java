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
import java.util.Random;

public class QuestionsActivity extends AppCompatActivity {

    public static final String EXAM_ID = "Exam ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        int examId = getIntent().getIntExtra(QuestionsActivity.EXAM_ID, -1);

        // TODO: delete this and use Retrofit instead
        Random random = new Random(System.currentTimeMillis());
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            List<String> choices = new ArrayList<>();
            for (int j = 0, len = random.nextInt(3) + 2; j < len; j++) {
                choices.add("Test " + i + " - Choice " + j + ": Mauris consequat mauris sed risus mollis, vitae hendrerit orci commodo. Proin tincidunt velit a erat elementum, et eleifend diam tincidunt.");
            }
            questions.add(new Question("Test " + i + ": Lorem ipsum dolor sit amet, consectetur adipiscing elit?", choices));
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rclrV_questions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new QuestionRecyclerViewAdapter(questions));
    }
}
