package com.example.heleninsa.criminalintent.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.heleninsa.criminalintent.R;

public class CrimeActivity extends SimpleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return new CrimeFragment();
    }
}
