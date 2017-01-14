package com.example.heleninsa.geoquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionView;

    private Question[] mQuestions = new Question[]{
            new Question(R.string.question_yym, true),
            new Question(R.string.question_hhy, false),
            new Question(R.string.question_yzw, true)
    };

    private int mCurIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mNextButton = (Button) findViewById(R.id.next_button);
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

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurIndex = (mCurIndex + 1) % mQuestions.length;
                updateQuestion();
            }
        });
    }

    private void checkAnswer(boolean answer) {
        boolean true_answer = mQuestions[mCurIndex].isAnswerTrue();

        int result;
        if (true_answer ^ answer) {
            result = R.string.incorrect_ans;
        } else {
            result = R.string.correct_ans;
        }

        Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion() {
        int question = mQuestions[mCurIndex].getTextResId();
        mQuestionView.setText(question);
    }
}
