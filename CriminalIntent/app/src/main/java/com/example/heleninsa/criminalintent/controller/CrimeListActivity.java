package com.example.heleninsa.criminalintent.controller;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.heleninsa.criminalintent.R;
import com.example.heleninsa.criminalintent.model.Crime;
import com.example.heleninsa.criminalintent.model.CrimeLab;

/**
 * Created by heleninsa on 2017/1/15.
 */

public class CrimeListActivity extends SimpleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected Fragment getFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePageActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            Fragment fragment = CrimeFragment.newInstance(crime.getId());
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.detail_fragment_container, fragment).commit();
        }
    }

    /**
     * @param crime
     */
    @Override
    public void onCrimeUpdate(Crime crime) {
        CrimeLab.getInstance(this).updateCrime(crime);
        updateView();
    }

    @Override
    public void onCrimeDelete() {
        updateView();
        FragmentManager manager = getSupportFragmentManager();
        Fragment details = manager.findFragmentById(R.id.detail_fragment_container);
        manager.beginTransaction().remove(details).commit();
    }

    private void updateView() {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        listFragment.updateUI();
    }

}
