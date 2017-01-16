package com.example.heleninsa.criminalintent.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.heleninsa.criminalintent.R;
import com.example.heleninsa.criminalintent.model.Crime;
import com.example.heleninsa.criminalintent.model.CrimeLab;

import java.util.List;
import java.util.UUID;

/**
 * Created by heleninsa on 2017/1/16.
 */

public class CrimePageActivity extends FragmentActivity {

    private static final String EXTRA_CRIME_ID = "com.heleninsa.criminalintent.controller.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePageActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes = CrimeLab.getInstance(this).getCrimes();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager.setCurrentItem(getLocation(crimeId));

    }

    private int getLocation(UUID crimeID){
        if(crimeID == null) {
            return 0;
        }
        for(int i = 0; i < mCrimes.size(); i ++) {
            Crime each = mCrimes.get(i);
            if(each.getId().equals(crimeID)) {
                return i;
            }
        }
        return 0;
    }
}
