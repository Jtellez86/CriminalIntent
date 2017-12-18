package com.bignerdranch.android.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Date;


public class TimePickerFragment extends DialogFragment {
    private static final String TIME_ARG = "date";
//    static final String EXTRA_TIME = "com.bignerdranch.android.criminalintent.time";

    TimePicker timePicker;

    public TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(TIME_ARG, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View inflatedView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        timePicker = (TimePicker) inflatedView.findViewById(R.id.dialog_time_picker);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.crime_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setView(inflatedView)
                .create();
    }
}
