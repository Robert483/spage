package com.thathustudio.spage.activities;

import android.content.Context;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.thathustudio.spage.R;
import com.thathustudio.spage.app.CustomApplication;
import com.thathustudio.spage.exception.SpageException;
import com.thathustudio.spage.fragments.dialogs.Task4PromptDialogFragment;
import com.thathustudio.spage.model.Question;
import com.thathustudio.spage.model.responses.EndPointResponse;
import com.thathustudio.spage.model.responses.Task4ListResponse;
import com.thathustudio.spage.service.retrofit.TranslateRetrofitException;
import com.thathustudio.spage.utils.QuestionRecyclerViewAdapter;
import com.turingtechnologies.materialscrollbar.TouchScrollBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener, Task4PromptDialogFragment.OnTask4PromptDialogInteractionListener {
    private static final String SUBMIT_DIALOG = "Submit Dialog";
    private static final String UP_DIALOG = "Up Dialog";
    private static final String BACK_DIALOG = "Back Dialog";
    private static final String DATA_INITIALIZED = "Data Initialized";
    private static final String QUESTIONS = "Questions";
    public static final String EXERCISE_ID = "Exercise ID";
    private QuestionRecyclerViewAdapter adapter;
    private boolean dataInitialized;
    private int exerciseId;
    private ArrayList<Call> calls;
    private boolean reload;

    private static void shuffleQuestions(List<Question> questions) {
        Collections.shuffle(questions);
        for (Question question : questions) {
            String[] temps = new String[]{question.a, question.b, question.c, question.d};
            List<String> choices = new ArrayList<>();
            for (String temp : temps) {
                if (temp != null && !temp.equals("")) {
                    choices.add(temp);
                }
            }
            question.setAnswer(choices.get(0));
            Collections.shuffle(choices);
            question.setChoices(choices);
        }
    }

    private void recyclerViewInit() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rclrV_questions);
        RecyclerViewExpandableItemManager recyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(null);

        // Expand all group items by default. This method must be called before creating a wrapper adapter.
        recyclerViewExpandableItemManager.setDefaultGroupsExpandedState(true);

        //adapter
        adapter = new QuestionRecyclerViewAdapter(this, recyclerViewExpandableItemManager);
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

        dataInitialized = false;
        calls = new ArrayList<>();
        exerciseId = getIntent().getIntExtra(QuestionsActivity.EXERCISE_ID, -1);

        TouchScrollBar touchScrollBar = (TouchScrollBar) findViewById(R.id.tchSrlBr);
        touchScrollBar.setHideDuration(1000);
        recyclerViewInit();
        findViewById(R.id.btn_finish).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload = false;
        if (!dataInitialized) {
            CustomApplication customApplication = (CustomApplication) getApplication();
            Call<Task4ListResponse<Question>> exerciseListResponseCall = customApplication.getTask4Service().getQuestions(exerciseId);
            exerciseListResponseCall.enqueue(new GetQuestionsCallback(this));
            calls.add(exerciseListResponseCall);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DATA_INITIALIZED, dataInitialized);
        if (dataInitialized) {
            outState.putParcelableArrayList(QUESTIONS, new ArrayList<>(adapter.getQuestions()));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            dataInitialized = savedInstanceState.getBoolean(DATA_INITIALIZED, false);
            if (dataInitialized) {
                List<Question> questions = savedInstanceState.getParcelableArrayList(QUESTIONS);
                View viewContainer = findViewById(R.id.rltLyot_container);
                if (viewContainer.getVisibility() == View.INVISIBLE) {
                    viewContainer.setVisibility(View.VISIBLE);
                    findViewById(R.id.prgBr_questions).setVisibility(View.INVISIBLE);
                }
                adapter.replaceQuestions(questions);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        reload = true;

        for (Call call : calls) {
            call.cancel();
        }
        calls.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (dataInitialized) {
                    showGoBackDialog(UP_DIALOG);
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
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
        if (dataInitialized) {
            showGoBackDialog(BACK_DIALOG);
        } else {
            super.onBackPressed();
        }
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
        Log.v("SPage", String.format(Locale.US, "Cancel dialog: %s", dialog.getTag()));
    }

    public static class GetQuestionsCallback extends ResultActivityCallback<Task4ListResponse<Question>> {
        public GetQuestionsCallback(QuestionsActivity questionsActivity) {
            super(questionsActivity);
        }

        @Override
        public void onResponse(Call<Task4ListResponse<Question>> call, Response<Task4ListResponse<Question>> response) {
            QuestionsActivity questionsActivity = weakReferenceQuestionsActivity.get();
            if (questionsActivity != null) {
                SpageException spageException = TranslateRetrofitException.translateServiceException(call, response);

                if (spageException != null) {
                    try {
                        questionsActivity.finish();
                        showToast(questionsActivity.getApplicationContext(), spageException);
                    } catch (Exception ex) {
                        Log.e("SPage", ex.getMessage());
                    }
                    questionsActivity.calls.remove(call);
                    return;
                }

                try {
                    questionsActivity.findViewById(R.id.prgBr_questions).setVisibility(View.GONE);
                    questionsActivity.findViewById(R.id.rltLyot_container).setVisibility(View.VISIBLE);
                    List<Question> questions = response.body().getResponse();
                    if (questions.size() == 0) {
                        throw new Exception("No questions available");
                    }
                    shuffleQuestions(questions);
                    questionsActivity.adapter.replaceQuestions(questions);
                    questionsActivity.dataInitialized = true;
                } catch (Exception ex1) {
                    try {
                        questionsActivity.finish();
                        showToast(questionsActivity.getApplicationContext(), ex1);
                    } catch (Exception ex2) {
                        Log.e("SPage", ex2.getMessage());
                    }
                } finally {
                    questionsActivity.calls.remove(call);
                }
            }
        }
    }

    public static abstract class ResultActivityCallback<T extends EndPointResponse> implements Callback<T> {
        protected final WeakReference<QuestionsActivity> weakReferenceQuestionsActivity;

        public ResultActivityCallback(QuestionsActivity questionsActivity) {
            weakReferenceQuestionsActivity = new WeakReference<>(questionsActivity);
        }

        protected void showToast(Context context, Throwable throwable) {
            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            QuestionsActivity questionsActivity = weakReferenceQuestionsActivity.get();
            if (questionsActivity != null) {
                try {
                    // Handle
                    if (!questionsActivity.reload) {
                        questionsActivity.finish();
                        showToast(questionsActivity.getApplicationContext(), t);
                    }
                } catch (Exception ex) {
                    Log.e("SPage", ex.getMessage());
                } finally {
                    questionsActivity.calls.remove(call);
                }
            }
        }
    }
}
