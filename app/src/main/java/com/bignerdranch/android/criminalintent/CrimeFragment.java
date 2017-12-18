package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {
    public static final String CRIME_ID_ARG = "CRIME_ID_ARG";
    public static final String DIALOG_DATE = "DialogDate";
    public static final String DIALOG_TIME = "DialogTime";
    public static final int REQUEST_DATE = 0;
    public static final int REQUEST_TIME = 1;

    private Crime crime;
    private CrimeLab crimeLab;
    private EditText titleField;
    private Button dateButton;
    private Button timeButton;

    public static CrimeFragment newInstance(UUID uuid) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID_ARG, uuid);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }

        if(requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            crime.setDate(date);
            updateDate();
        }

        if(requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            crime.setDate(date);
            updateTime();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crimeLab = CrimeLab.get();
        crime = crimeLab.getCrime((UUID)getArguments().getSerializable(CRIME_ID_ARG));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        titleField = (EditText) view.findViewById(R.id.crime_title);
        titleField.setText(crime.getTitle());
        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        dateButton = (Button) view.findViewById(R.id.crime_date);
        updateDate();
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment().newInstance(crime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(getFragmentManager(), DIALOG_DATE);
            }
        });

        timeButton = (Button) view.findViewById(R.id.crime_time);
        updateTime();
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment().newInstance(crime.getDate());
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(getFragmentManager(), DIALOG_TIME);
            }
        });

        final CheckBox solved = (CheckBox) view.findViewById(R.id.solved);
        solved.setChecked(crime.isSolved());
        solved.setOnCheckedChangeListener(setIsSolvedOnCheckedListener());

        return view;
    }

    private void updateTime() {
        timeButton.setText(DateFormat.format("h:hh AA", crime.getDate()));
    }

    private void updateDate() {
        dateButton.setText(DateFormat.format("EEEE, MMM d, yyyy", crime.getDate()));
    }

    @NonNull
    private OnCheckedChangeListener setIsSolvedOnCheckedListener() {
        return new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
                crimeLab.solveCrime(crime.getId(), isChecked);
            }
        };
    }
}
