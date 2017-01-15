package com.example.heleninsa.criminalintent.controller;

import android.support.v4.app.Fragment;

/**
 * Created by heleninsa on 2017/1/15.
 */

public class CrimeListActivity extends SimpleFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return new CrimeListFragment();
    }
}
