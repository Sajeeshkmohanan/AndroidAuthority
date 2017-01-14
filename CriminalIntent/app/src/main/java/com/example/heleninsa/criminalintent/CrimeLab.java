package com.example.heleninsa.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by heleninsa on 2017/1/14.
 */
public class CrimeLab {

    private static CrimeLab sCrimeLab ;
    public static CrimeLab getInstance(Context context) {
        if(sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private List<Crime> mCrimes;

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        for(int i = 0; i < 100; i ++) {
            Crime crime = new Crime();
            crime.setSolved(i%2 == 0);
            crime.setTitle("Crime #" + i);
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for(Crime each : mCrimes) {
            if(each.getId().equals(id))
                return each;
        }
        return null;
    }
}
