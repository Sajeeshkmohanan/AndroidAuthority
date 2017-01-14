package com.example.heleninsa.geoquiz;

/**
 * Created by heleninsa on 2017/1/12.
 */

public class Question {

    /**
     * Store id but not the string itself
     * Save more memory
     */
    private int mTextResId;

    private boolean mAnswerTrue;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }


}
