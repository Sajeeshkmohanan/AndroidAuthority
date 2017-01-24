package com.example.heleninsa.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.heleninsa.criminalintent.database.CrimeBaseHelper;
import com.example.heleninsa.criminalintent.database.CrimeCursorWrapper;
import com.example.heleninsa.criminalintent.database.CrimeDbSchema;
import com.example.heleninsa.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.heleninsa.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.DATE;
import static com.example.heleninsa.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SOLVED;
import static com.example.heleninsa.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SUSPECTED;
import static com.example.heleninsa.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.TITLE;
import static com.example.heleninsa.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.UUID;

/**
 * Created by heleninsa on 2017/1/14.
 */
public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public static CrimeLab getInstance(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(UUID, crime.getId().toString());
        values.put(TITLE, crime.getTitle());
        values.put(DATE, crime.getDate().getTime());
        values.put(SOLVED, crime.isSolved() ? 1 : 0);
        values.put(SUSPECTED, crime.getSuspect());
        return values;
    }

    public void updateCrime(Crime crime) {
        String uuid = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values, UUID + " = ?", new String[]{uuid});
    }

    public void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void removeCrime(Crime crime) {
        String uuid = crime.getId().toString();
        mDatabase.delete(CrimeTable.NAME, UUID + " = ?", new String[]{uuid});
    }

    public int size() {
        return getCrimes().size();
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Crime each = cursor.getCrime();
                crimes.add(each);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});
        try {
            if(cursor.getCount() == 0) {
                return  null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }

}
