package com.example.heleninsa.criminalintent.database;

import android.database.Cursor;

import com.example.heleninsa.criminalintent.database.CrimeDbSchema.CrimeTable;
import com.example.heleninsa.criminalintent.model.Crime;

import java.util.Date;
import java.util.UUID;

/**
 * Created by heleninsa on 2017/1/22.
 */

public class CrimeCursorWrapper extends android.database.CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuid = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspect = getString(getColumnIndex(CrimeTable.Cols.SUSPECTED));
        Crime crime = new Crime(UUID.fromString(uuid));
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspect(suspect);
        crime.setTitle(title);
        return crime;
    }

}
