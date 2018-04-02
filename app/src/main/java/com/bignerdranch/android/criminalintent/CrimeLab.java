package com.bignerdranch.android.criminalintent;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab crimeLab;
    private Context context;
    private SQLiteDatabase database;

    private CrimeLab(Context context) {
        this.context = context.getApplicationContext();
        database = new CrimeBaseHelper(this.context).getWritableDatabase();
    }

    public void addCrime(Crime crime) {
        ContentValues contentValues = getContentValues(crime);
        database.insert(CrimeDbSchema.CrimeTable.NAME, null, contentValues);
    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);

        database.update(CrimeDbSchema.CrimeTable.NAME, contentValues,
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public static CrimeLab get(Context context) {
        if (crimeLab == null) {
            crimeLab = new CrimeLab(context);
        }
        return crimeLab;
    }

    public Crime getCrime(UUID uuid) {
        CrimeCursorWrapper cursor = new CrimeCursorWrapper(queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID + " = ? ", new String[]{uuid.toString()}));
        try {
            if(cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    public static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().toString());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);

        return contentValues;
    }

    public void solveCrime(UUID uuid, boolean isChecked) {
        getCrime(uuid).setSolved(isChecked);
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(CrimeDbSchema.CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);

        return new CrimeCursorWrapper(cursor);

    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public void deleteCrime(UUID id) {
        database.delete(CrimeDbSchema.CrimeTable.NAME,
                CrimeDbSchema.CrimeTable.Cols.UUID + " = ? ",
                new String[]{id.toString()});
    }
}
