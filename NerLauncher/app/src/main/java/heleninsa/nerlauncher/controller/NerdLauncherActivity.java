package heleninsa.nerlauncher.controller;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import heleninsa.nerlauncher.R;

public class NerdLauncherActivity extends SimpleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return NerdLauncherFragment.newInstance();
    };

}
