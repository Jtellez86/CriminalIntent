package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import java.util.UUID;


public class CrimeFragment extends Fragment {
    public static final String CRIME_ID_ARG = "CRIME_ID_ARG";

    private Crime crime;
    private CrimeLab crimeLab;
    private EditText titleField;

    public static CrimeFragment newInstance(UUID uuid) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID_ARG, uuid);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
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

        Button dateButton = (Button) view.findViewById(R.id.crime_date);
        dateButton.setText(crime.getDate().toString());
        dateButton.setEnabled(false);

        final CheckBox solved = (CheckBox) view.findViewById(R.id.solved);
        solved.setChecked(crime.isSolved());
        solved.setOnCheckedChangeListener(setIsSolvedOnCheckedListener());

        return view;


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
