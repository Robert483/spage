package com.thathustudio.spage.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.thathustudio.spage.R;
import com.thathustudio.spage.app.CustomApplication;
import com.thathustudio.spage.exception.SpageException;
import com.thathustudio.spage.fragments.dialogs.Task4PromptDialogFragment;
import com.thathustudio.spage.model.Question;
import com.thathustudio.spage.model.Room;
import com.thathustudio.spage.model.responses.Task4ListResponse;
import com.thathustudio.spage.service.retrofit.TranslateRetrofitException;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener, Task4PromptDialogFragment.OnTask4PromptDialogInteractionListener {
    private static final String UP_DIALOG = "Up Dialog";
    private static final String BACK_DIALOG = "Back Dialog";
    private static final java.lang.String GENERATE_QUESTION = "Generate questions?";
    public static final String USER_NAME = "User name";
    public static final String ROOM_ID = "Room ID";
    public static final String IS_OWNER = "Is owner?";
    private DatabaseReference roomDatabase;
    private DatabaseReference hallDatabase;
    private boolean generateQuestions;

    private OpponentInitListener opponentInitListener;
    private OpponentScoreListener opponentScoreListener;
    private QuestionListener questionListener;
    private QuestionIndexListener questionIndexListener;
    private PlayersFinishedListener playersFinishedListener;
    private RoomDestroyedListener roomDestroyedListener;

    private View viewQuestion;
    private View viewDuelNotification;
    private TextView textViewOpponentScore;
    private TextView textViewSelfScore;
    private TextView textViewQuestionContent;
    private TextView textViewResult;
    private RadioButton radioButtonChoiceA;
    private RadioButton radioButtonChoiceB;
    private RadioButton radioButtonChoiceC;
    private RadioButton radioButtonChoiceD;
    private RadioGroup radioGroupChoices;

    private int selfScore;
    private int playersFinished;
    private Question question;
    private int questionIndex;
    private boolean isOwner;
    private boolean destroyed;
    private boolean backByUser;

    private void showGoBackDialog(String tag) {
        Task4PromptDialogFragment.newInstance(R.string.go_back, R.string.you_are_in_middle_of_the_game, R.string.back, R.string.keep_playing).show(getSupportFragmentManager(), tag);
    }

    private void removeListener() {
        roomDatabase.child("player1Name").removeEventListener(roomDestroyedListener);

        if (opponentInitListener != null) {
            if (isOwner) {
                roomDatabase.child("player2Name").removeEventListener(opponentInitListener);
            } else {
                roomDatabase.child("player1Name").removeEventListener(opponentInitListener);
            }
        }

        if (opponentScoreListener != null) {
            if (isOwner) {
                roomDatabase.child("player2Score").removeEventListener(opponentScoreListener);
            } else {
                roomDatabase.child("player1Score").removeEventListener(opponentScoreListener);
            }
        }

        if (questionListener != null) {
            roomDatabase.child("questions").child(String.format(Locale.getDefault(), "%d", questionIndex)).removeEventListener(questionListener);
        }

        if (questionIndexListener != null) {
            roomDatabase.child("currentQuestionIndex").removeEventListener(questionIndexListener);
        }

        if (playersFinishedListener != null) {
            roomDatabase.child("nPlayersFinishedCurrentQuestion").removeEventListener(playersFinishedListener);
        }
    }

    private void compareScore() {
        int opponentScore = Integer.parseInt(textViewOpponentScore.getText().toString());
        if (selfScore > opponentScore) {
            textViewResult.setText(getResources().getString(R.string.you_win));
        } else if (selfScore == opponentScore) {
            textViewResult.setText(getResources().getString(R.string.draw));
        } else {
            textViewResult.setText(getResources().getString(R.string.you_lose));
        }
    }

    private void backNormally() {
        backByUser = true;
        hallDatabase.setValue(null);
        roomDatabase.setValue(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        backByUser = false;
        destroyed = false;
        selfScore = 0;
        playersFinished = 0;
        questionIndex = 0;

        generateQuestions = savedInstanceState != null && savedInstanceState.getBoolean(GENERATE_QUESTION, false);

        viewQuestion = findViewById(R.id.crdV_question);
        viewDuelNotification = findViewById(R.id.txtV_duelNotification);
        textViewSelfScore = (TextView) findViewById(R.id.txtV_selfScore);
        textViewOpponentScore = (TextView) findViewById(R.id.txtV_opponentScore);
        textViewQuestionContent = (TextView) findViewById(R.id.txtV_questionContent);
        textViewResult = (TextView) findViewById(R.id.txtV_result);
        radioButtonChoiceA = (RadioButton) findViewById(R.id.rdBtn_choiceA);
        radioButtonChoiceA.setOnClickListener(this);
        radioButtonChoiceB = (RadioButton) findViewById(R.id.rdBtn_choiceB);
        radioButtonChoiceB.setOnClickListener(this);
        radioButtonChoiceC = (RadioButton) findViewById(R.id.rdBtn_choiceC);
        radioButtonChoiceC.setOnClickListener(this);
        radioButtonChoiceD = (RadioButton) findViewById(R.id.rdBtn_choiceD);
        radioButtonChoiceD.setOnClickListener(this);
        radioGroupChoices = (RadioGroup) findViewById(R.id.rdG_choices);
        findViewById(R.id.btn_next).setOnClickListener(this);

        Intent intent = getIntent();
        String key = intent.getStringExtra(ROOM_ID);
        isOwner = intent.getBooleanExtra(IS_OWNER, false);
        hallDatabase = FirebaseDatabase.getInstance().getReference();
        roomDatabase = hallDatabase.child("playingRooms").child(key);
        if (isOwner) {
            if (!generateQuestions) {
                CustomApplication customApplication = (CustomApplication) getApplication();
                Call<Task4ListResponse<Question>> duelResponseCall = customApplication.getTask4Service().getDuelQuestions(10);
                duelResponseCall.enqueue(new GetDuelQuestionsCallback(key, intent.getStringExtra(USER_NAME)));
                Log.v("SPage", "Sent");
                generateQuestions = true;
            }
            opponentInitListener = new OpponentInitListener(this, "player2Name", "player2Score", "player1Score");
            roomDatabase.child("player2Name").addValueEventListener(opponentInitListener);
        } else {
            opponentInitListener = new OpponentInitListener(this, "player1Name", "player1Score", "player2Score");
            roomDatabase.child("player1Name").addListenerForSingleValueEvent(opponentInitListener);
        }
        hallDatabase = hallDatabase.child("hall").child(key);
        hallDatabase.onDisconnect().setValue(null);
        roomDatabase.onDisconnect().setValue(null);
        roomDestroyedListener = new RoomDestroyedListener(this);
        roomDatabase.child("player1Name").addValueEventListener(roomDestroyedListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(GENERATE_QUESTION, generateQuestions);
    }

    @Override
    protected void onDestroy() {
        removeListener();
        destroyed = true;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (textViewResult.getVisibility() == View.VISIBLE) {
                    backNormally();
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    showGoBackDialog(UP_DIALOG);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (textViewResult.getVisibility() == View.VISIBLE) {
            backNormally();
            super.onBackPressed();
        } else {
            showGoBackDialog(BACK_DIALOG);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rdBtn_choiceA:
                question.setUserChoice(0);
                break;
            case R.id.rdBtn_choiceB:
                question.setUserChoice(1);
                break;
            case R.id.rdBtn_choiceC:
                question.setUserChoice(2);
                break;
            case R.id.rdBtn_choiceD:
                question.setUserChoice(3);
                break;
            case R.id.btn_next:
                roomDatabase.child("nPlayersFinishedCurrentQuestion").runTransaction(new PlayersFinishedTransaction(this));
                break;
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        switch (dialog.getTag()) {
            case UP_DIALOG:
                backNormally();
                NavUtils.navigateUpFromSameTask(this);
                break;
            case BACK_DIALOG:
                backNormally();
                super.onBackPressed();
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Log.v("SPage", String.format(Locale.US, "Cancel dialog: %s", dialog.getTag()));

    }

    public static class PlayersFinishedTransaction implements Transaction.Handler {
        public WeakReference<RoomActivity> weakReferenceRoomActivity;

        public PlayersFinishedTransaction(RoomActivity roomActivity) {
            weakReferenceRoomActivity = new WeakReference<>(roomActivity);
        }

        @Override
        public Transaction.Result doTransaction(MutableData mutableData) {
            Integer playersFinished = mutableData.getValue(Integer.class);
            RoomActivity roomActivity = weakReferenceRoomActivity.get();
            if (playersFinished == null) {
                if (roomActivity != null && !roomActivity.destroyed) {
                    if (!roomActivity.backByUser)
                        Toast.makeText(roomActivity.getApplicationContext(), "Your opponent left the room", Toast.LENGTH_LONG).show();
                    roomActivity.destroyed = true;
                    roomActivity.finish();
                }
                return Transaction.success(mutableData);
            }

            roomActivity.playersFinished = playersFinished + 1;
            mutableData.setValue(roomActivity.playersFinished);
            return Transaction.success(mutableData);
        }

        @Override
        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
            if (databaseError != null)
                Log.d("SPage", databaseError.getMessage());
        }
    }

    public static class GetDuelQuestionsCallback implements Callback<Task4ListResponse<Question>> {
        private final String key;
        private final String userName;

        public GetDuelQuestionsCallback(String key, String userName) {
            this.key = key;
            this.userName = userName;
        }

        private boolean isException(SpageException spageException) {
            return spageException != null;
        }

        @Override
        public void onResponse(Call<Task4ListResponse<Question>> call, Response<Task4ListResponse<Question>> response) {
            Log.v("SPage", "Success");
            SpageException spageException = TranslateRetrofitException.translateServiceException(call, response);

            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
            DatabaseReference database = root.child("playingRooms").child(key);

            if (isException(spageException)) { // ignore warning purpose
                database.setValue(null);
                return;
            }

            List<Question> questions = response.body().getResponse();
            if (questions.size() == 0) {
                database.setValue(null);
                return;
            }

            database.child("currentQuestionIndex").setValue(questions.size() - 1);
            database = database.child("questions");
            for (int i = 0, len = questions.size(); i < len; i++) {
                database.child(Integer.toString(i)).setValue(questions.get(i));
            }
            root.child("hall").child(key).setValue(new Room(userName, 1));
        }

        @Override
        public void onFailure(Call<Task4ListResponse<Question>> call, Throwable t) {
            FirebaseDatabase.getInstance().getReference().child("playingRooms").child(key).setValue(null);
        }
    }

    public static class RoomDestroyedListener extends RoomValueEventListener {
        public RoomDestroyedListener(RoomActivity roomActivity) {
            super(roomActivity);
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            RoomActivity roomActivity = weakReferenceRoomActivity.get();
            if (roomActivity != null) {
                if (dataSnapshot.getValue() == null && !roomActivity.destroyed) {
                    if (!roomActivity.backByUser)
                        Toast.makeText(roomActivity, "Generating questions request failed", Toast.LENGTH_LONG).show();
                    roomActivity.finish();
                }
            }
        }
    }

    public static class OpponentInitListener extends RoomValueEventListener {
        private final String opponentNameKey;
        private final String opponentScoreKey;
        private final String selfScore;

        public OpponentInitListener(RoomActivity roomActivity, String opponentNameKey, String opponentScoreKey, String selfScore) {
            super(roomActivity);
            this.opponentNameKey = opponentNameKey;
            this.opponentScoreKey = opponentScoreKey;
            this.selfScore = selfScore;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.getValue() != null) {
                RoomActivity roomActivity = weakReferenceRoomActivity.get();
                if (roomActivity != null) {
                    // Remove listen for opponent name
                    roomActivity.roomDatabase.child(opponentNameKey).removeEventListener(roomActivity.opponentInitListener);
                    roomActivity.opponentInitListener = null;
                    TextView textView = (TextView) roomActivity.findViewById(R.id.txtV_opponentName);
                    textView.setText(dataSnapshot.getValue(String.class));

                    // Listen for opponent score
                    roomActivity.opponentScoreListener = new OpponentScoreListener(roomActivity);
                    roomActivity.roomDatabase.child(opponentScoreKey).addValueEventListener(roomActivity.opponentScoreListener);

                    // Init self score
                    roomActivity.textViewSelfScore.setText(String.format(Locale.getDefault(), "%d", roomActivity.selfScore));

                    // Question index listener
                    roomActivity.questionIndexListener = new QuestionIndexListener(roomActivity);
                    roomActivity.roomDatabase.child("currentQuestionIndex").addValueEventListener(roomActivity.questionIndexListener);

                    // nPlayersFinishedCurrentQuestion
                    roomActivity.playersFinishedListener = new PlayersFinishedListener(roomActivity, selfScore);
                    roomActivity.roomDatabase.child("nPlayersFinishedCurrentQuestion").addValueEventListener(roomActivity.playersFinishedListener);
                }
            }
        }
    }

    public static class OpponentScoreListener extends RoomValueEventListener {
        public OpponentScoreListener(RoomActivity roomActivity) {
            super(roomActivity);
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            RoomActivity roomActivity = weakReferenceRoomActivity.get();
            if (roomActivity != null) {
                if (dataSnapshot.getValue() == null) {
                    if (!roomActivity.destroyed) {
                        if (!roomActivity.backByUser)
                            Toast.makeText(roomActivity.getApplicationContext(), "Your opponent left the room", Toast.LENGTH_LONG).show();
                        roomActivity.destroyed = true;
                        roomActivity.finish();
                    }
                    return;
                }
                roomActivity.textViewOpponentScore.setText(dataSnapshot.getValue().toString());
                roomActivity.compareScore();
            }
        }
    }

    public static class QuestionIndexListener extends RoomValueEventListener {
        public QuestionIndexListener(RoomActivity roomActivity) {
            super(roomActivity);
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            RoomActivity roomActivity = weakReferenceRoomActivity.get();
            if (roomActivity != null) {
                if (dataSnapshot.getValue() == null) {
                    if (!roomActivity.destroyed) {
                        if (!roomActivity.backByUser)
                            Toast.makeText(roomActivity.getApplicationContext(), "Your opponent left the room", Toast.LENGTH_LONG).show();
                        roomActivity.destroyed = true;
                        roomActivity.finish();
                    }
                    return;
                }
                String index = dataSnapshot.getValue().toString();
                roomActivity.questionIndex = Integer.parseInt(index);
                if (roomActivity.questionIndex < 0) {
                    roomActivity.questionIndex = 0;
                    roomActivity.textViewResult.setVisibility(View.VISIBLE);
                } else {
                    roomActivity.questionListener = new QuestionListener(roomActivity);
                    roomActivity.roomDatabase.child("questions").child(index).addListenerForSingleValueEvent(roomActivity.questionListener);
                }
            }
        }
    }

    public static class QuestionListener extends RoomValueEventListener {
        public QuestionListener(RoomActivity roomActivity) {
            super(roomActivity);
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            RoomActivity roomActivity = weakReferenceRoomActivity.get();
            if (roomActivity != null) {
                if (dataSnapshot.getValue() == null) {
                    if (!roomActivity.destroyed) {
                        if (!roomActivity.backByUser)
                            Toast.makeText(roomActivity.getApplicationContext(), "Your opponent left the room", Toast.LENGTH_LONG).show();
                        roomActivity.destroyed = true;
                        roomActivity.finish();
                    }
                    return;
                }
                roomActivity.questionListener = null;
                roomActivity.question = dataSnapshot.getValue(Question.class);
                QuestionsActivity.shuffleQuestion(roomActivity.question);
                roomActivity.textViewQuestionContent.setText(roomActivity.question.getContent());

                int i, len;
                List<String> choices = roomActivity.question.getChoices();
                RadioButton[] radioButtons = new RadioButton[]{roomActivity.radioButtonChoiceA, roomActivity.radioButtonChoiceB, roomActivity.radioButtonChoiceC, roomActivity.radioButtonChoiceD};
                for (i = 0, len = choices.size(); i < len; i++) {
                    radioButtons[i].setVisibility(View.VISIBLE);
                    radioButtons[i].setText(choices.get(i));
                }
                for (; i < 4; i++) {
                    radioButtons[i].setVisibility(View.GONE);
                }
            }
        }
    }

    public static class PlayersFinishedListener extends RoomValueEventListener {
        private final String selfScore;

        public PlayersFinishedListener(RoomActivity roomActivity, String selfScore) {
            super(roomActivity);
            this.selfScore = selfScore;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            RoomActivity roomActivity = weakReferenceRoomActivity.get();
            if (roomActivity != null) {
                if (dataSnapshot.getValue() == null) {
                    if (!roomActivity.destroyed) {
                        if (!roomActivity.backByUser)
                            Toast.makeText(roomActivity.getApplicationContext(), "Your opponent left the room", Toast.LENGTH_LONG).show();
                        roomActivity.destroyed = true;
                        roomActivity.finish();
                    }
                    return;
                }
                int original = roomActivity.playersFinished;
                roomActivity.playersFinished = dataSnapshot.getValue(Integer.class);
                if (roomActivity.playersFinished <= 0) {
                    roomActivity.viewDuelNotification.setVisibility(View.INVISIBLE);
                    roomActivity.viewQuestion.setVisibility(View.VISIBLE);
                } else if (roomActivity.playersFinished >= 2) {
                    if (original == roomActivity.playersFinished) {
                        roomActivity.roomDatabase.child("nPlayersFinishedCurrentQuestion").setValue(0);
                        roomActivity.roomDatabase.child("currentQuestionIndex").setValue(--roomActivity.questionIndex);
                    }
                    if (roomActivity.question.isCorrect()) {
                        roomActivity.roomDatabase.child(selfScore).setValue(++roomActivity.selfScore);
                        roomActivity.textViewSelfScore.setText(String.format(Locale.getDefault(), "%d", roomActivity.selfScore));
                        roomActivity.compareScore();
                    }
                    roomActivity.radioGroupChoices.clearCheck();
                } else if (original == roomActivity.playersFinished) {
                    roomActivity.viewDuelNotification.setVisibility(View.VISIBLE);
                    roomActivity.viewQuestion.setVisibility(View.GONE);
                }
            }
        }
    }

    public static abstract class RoomValueEventListener implements ValueEventListener {
        protected final WeakReference<RoomActivity> weakReferenceRoomActivity;

        public RoomValueEventListener(RoomActivity roomActivity) {
            weakReferenceRoomActivity = new WeakReference<>(roomActivity);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Do nothing
            Log.v("SPage", "Listener cancel");
        }
    }
}
