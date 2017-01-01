package com.thathustudio.spage.activities;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.thathustudio.spage.R;
import com.thathustudio.spage.model.Question;
import com.thathustudio.spage.utils.QuestionRecyclerViewAdapter;
import com.turingtechnologies.materialscrollbar.TouchScrollBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionsActivity extends AppCompatActivity {

    public static final String EXAM_ID = "Exam ID";

    private void recyclerViewInit(Bundle savedInstanceState) {
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
        RecyclerViewExpandableItemManager recyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(null);

        // Expand all group items by default. This method must be called before creating a wrapper adapter.
        recyclerViewExpandableItemManager.setDefaultGroupsExpandedState(true);

        //adapter
        RecyclerView.Adapter adapter = new QuestionRecyclerViewAdapter(recyclerViewExpandableItemManager, questions);
        RecyclerView.Adapter wrappedAdapter = recyclerViewExpandableItemManager.createWrappedAdapter(adapter);       // wrap for expanding

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Need to disable them when using animation indicator.
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wrappedAdapter);
        recyclerView.setItemAnimator(animator);

        // additional decorations
        // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(this, R.drawable.nine_patch_material_shadow_z1)));
        }

        recyclerViewExpandableItemManager.attachRecyclerView(recyclerView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        int examId = getIntent().getIntExtra(QuestionsActivity.EXAM_ID, -1);

        recyclerViewInit(savedInstanceState);

        TouchScrollBar touchScrollBar = (TouchScrollBar) findViewById(R.id.tchSrlBr);
        touchScrollBar.setHideDuration(1000);
    }
}
