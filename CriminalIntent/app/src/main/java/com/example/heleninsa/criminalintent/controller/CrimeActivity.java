package com.example.heleninsa.criminalintent.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.heleninsa.criminalintent.model.Crime;

import java.util.UUID;

public class CrimeActivity extends SimpleFragmentActivity implements CrimeFragment.Callbacks {

    public final static String EXTRA_CRIME_ID = "com.heleninsa.criminalintent.controller.crime_id";

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected Fragment getFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

    @Override
    public void onCrimeUpdate(Crime crime) {
    }

    @Override
    public void onCrimeDelete() {

    }

}
