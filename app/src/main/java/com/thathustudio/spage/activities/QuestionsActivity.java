package com.thathustudio.spage.activities;

import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.thathustudio.spage.R;
import com.thathustudio.spage.fragments.dialogs.Task4PromptDialogFragment;
import com.thathustudio.spage.model.Question;
import com.thathustudio.spage.utils.QuestionRecyclerViewAdapter;
import com.turingtechnologies.materialscrollbar.TouchScrollBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener, Task4PromptDialogFragment.OnTask4PromptDialogInteractionListener {
    private static final String SUBMIT_DIALOG = "Submit Dialog";
    private static final String UP_DIALOG = "Up Dialog";
    private static final String BACK_DIALOG = "Back Dialog";
    public static final String EXERCISE_ID = "Exercise ID";
    private QuestionRecyclerViewAdapter adapter;

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
        Collections.shuffle(questions);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rclrV_questions);
        RecyclerViewExpandableItemManager recyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(null);

        // Expand all group items by default. This method must be called before creating a wrapper adapter.
        recyclerViewExpandableItemManager.setDefaultGroupsExpandedState(true);

        //adapter
        adapter = new QuestionRecyclerViewAdapter(this, recyclerViewExpandableItemManager, questions);
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

    private void showGoBackDialog(String tag) {
        Task4PromptDialogFragment.newInstance(R.string.go_back, R.string.your_choices_will_not_be_saved, R.string.back, R.string.keep_doing).show(getSupportFragmentManager(), tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        int exerciseId = getIntent().getIntExtra(QuestionsActivity.EXERCISE_ID, -1);

        recyclerViewInit(savedInstanceState);

        TouchScrollBar touchScrollBar = (TouchScrollBar) findViewById(R.id.tchSrlBr);
        touchScrollBar.setHideDuration(1000);

        findViewById(R.id.btn_finish).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                showGoBackDialog(UP_DIALOG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                Task4PromptDialogFragment.newInstance(R.string.submit_for_score, R.string.you_will_not_be_able_to_go_back, R.string.submit, R.string.keep_doing).show(getSupportFragmentManager(), SUBMIT_DIALOG);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showGoBackDialog(BACK_DIALOG);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String tag = dialog.getTag();
        switch (tag) {
            case SUBMIT_DIALOG:
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBooleanArray(ResultActivity.RESULT, adapter.getResult());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case UP_DIALOG:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case BACK_DIALOG:
                super.onBackPressed();
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Do nothing
    }
}
