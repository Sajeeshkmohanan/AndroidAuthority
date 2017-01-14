package com.example.heleninsa.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_CHEAT = 0;
    private final static String KEY_INDEX = "index";
    private final static String KEY_IS_CHEAT = "is_cheat";
    private final static String EXTRA_ANSWER_IS_TRUE = "com.example.heleninsa.geoquiz.answer_is_true";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mLastButton;
    private TextView mQuestionView;

    private Question[] mQuestions = new Question[]{
            new Question(R.string.question_yym, true),
            new Question(R.string.question_hhy, false),
            new Question(R.string.question_yzw, true)
    };

    private boolean[] mIsCheat = new boolean[]{
            false, false, false
    };

    private int mCurIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurIndex = savedInstanceState.getInt(KEY_INDEX);
            mIsCheat = savedInstanceState.getBooleanArray(KEY_IS_CHEAT);
        }

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mLastButton = (ImageButton) findViewById(R.id.last_button);
        mQuestionView = (TextView) findViewById(R.id.question);
        updateQuestion();

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mQuestionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        mLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastQuestion();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = newIntent(MainActivity.this, mQuestions[mCurIndex].isAnswerTrue());
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
    }

    private void lastQuestion() {
        if (mCurIndex == 0) {
            mCurIndex = mQuestions.length - 1;
        } else {
            mCurIndex = (mCurIndex - 1) % mQuestions.length;
        }

        updateQuestion();
    }

    private void nextQuestion() {
        mCurIndex = (mCurIndex + 1) % mQuestions.length;
        updateQuestion();
    }

    private void checkAnswer(boolean answer) {
        boolean true_answer = mQuestions[mCurIndex].isAnswerTrue();

        int result;
        if (true_answer ^ answer) {
            result = R.string.incorrect_ans;
        } else {
            if (mIsCheat[mCurIndex]) {
                result = R.string.cheat_button;
            } else {
                result = R.string.correct_ans;
            }
        }

        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion() {
//        mIsCheat[mCurIndex] = false;
        int question = mQuestions[mCurIndex].getTextResId();
        mQuestionView.setText(question);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.w("Information", "Save");

        outState.putInt(KEY_INDEX, mCurIndex);
        outState.putBooleanArray(KEY_IS_CHEAT, mIsCheat);
    }

    private static Intent newIntent(Context packagetContext, boolean answerIsTrue) {
        Intent intent = new Intent(packagetContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data != null) {
                mIsCheat[mCurIndex] = CheatActivity.isAnswerShown(data);
            }
        }
    }

}
