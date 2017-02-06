package heleninsa.beatbox.controller;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import heleninsa.beatbox.R;

public class BeatBoxActivity extends SimpleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return BeatBoxFragment.newInstance();
    }
}
