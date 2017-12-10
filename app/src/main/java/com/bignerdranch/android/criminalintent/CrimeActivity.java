package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {
    private static final String CRIME_ID_EXTRA = "CRIME_ID_EXTRA";

    public static Intent newIntent(Context context, UUID uuid) {
        return new Intent(context, CrimeActivity.class).putExtra(CRIME_ID_EXTRA, uuid.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Fragment createFragment() {
        return CrimeFragment.newInstance(UUID.fromString(getIntent().getStringExtra(CRIME_ID_EXTRA)));
    }
}