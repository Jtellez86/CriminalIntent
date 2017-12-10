package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimesListActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, CrimeActivity.class);
    }

    @Override
    public Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(CrimesListActivity.newIntent(this));
        finish();
    }
}
