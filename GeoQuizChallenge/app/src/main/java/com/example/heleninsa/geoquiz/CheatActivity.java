package com.example.heleninsa.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private final static String KEY_IS_ANSWER_SHOWN = "is_shown";
    private final static String EXTRA_ANSWER_IS_TRUE = "com.example.heleninsa.geoquiz.answer_is_true";
    private final static String EXTRA_ANSWER_IS_SHOWN = "com.example.heleninsa.geoquiz.answer_is_shown";

    private boolean mAnswerIsTrue;
    private boolean mIsAnswerShown;

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mAnswerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        if(savedInstanceState != null) {
            mIsAnswerShown = savedInstanceState.getBoolean(KEY_IS_ANSWER_SHOWN, false);
        }
        setContentView(R.layout.activity_cheat);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswer = (Button) findViewById(R.id.show_answer_button);

        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int text;
                if (mAnswerIsTrue) {
                    text = R.string.true_button;
                } else {
                    text = R.string.false_button;
                }
                mAnswerTextView.setText(text);
                setAnswerIsShown(true);
            }
        });
    }

    public void setAnswerIsShown(boolean isAnswerShown) {
        mIsAnswerShown = isAnswerShown;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_IS_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    public static boolean isAnswerShown(Intent data) {
        return data.getBooleanExtra(EXTRA_ANSWER_IS_SHOWN, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_ANSWER_SHOWN, mIsAnswerShown);
    }
}
