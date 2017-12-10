package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity{
    private static final String CRIME_ID_EXTRA = "CRIME_ID_EXTRA";

    ViewPager viewPager;
    List<Crime> crimes;
    String crimeId;

    public static Intent newIntent(Context context, UUID uuid) {

        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(CRIME_ID_EXTRA, uuid.toString());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        crimeId = getIntent().getStringExtra(CRIME_ID_EXTRA);

        viewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        crimes = CrimeLab.get().getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return CrimeFragment.newInstance(crimes.get(position).getId());
            }

            @Override
            public int getCount() {
                return crimes.size();
            }
        });

        for (int i = 0; i < crimes.size(); i++) {
            if(crimes.get(i).getId().equals(UUID.fromString(crimeId))) {
                viewPager.setCurrentItem(i);

            }
        }
    }
}
