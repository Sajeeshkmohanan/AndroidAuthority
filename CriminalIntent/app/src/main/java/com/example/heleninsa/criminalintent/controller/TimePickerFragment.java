package com.example.heleninsa.criminalintent.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.example.heleninsa.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by heleninsa on 2017/1/17.
 */

public class TimePickerFragment extends DialogFragment {

    public final static String EXTRA_TIME = "com.heleninsa.criminalintent.controller.time";
    private final static String ARG_TIME = "time";
    private TimePicker mTimePicker;

    private Calendar mCalendar;

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_TIME);
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity()).
                setView(v).
                setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mCalendar.get(Calendar.YEAR);
                                int month = mCalendar.get(Calendar.MONTH);
                                int day = mCalendar.get(Calendar.DAY_OF_MONTH);
                                int hour = mTimePicker.getCurrentHour();
                                int minute = mTimePicker.getCurrentMinute();
                                Calendar calendar = new GregorianCalendar(year, month, day, hour, minute);
                                sendResult(Activity.RESULT_OK, calendar.getTime());
                            }
                        }).
                setTitle("Crime Time").
                create();
    }

    private void sendResult(int resultCode, Date date) {
        Fragment target = getTargetFragment();
        if (target != null) {
            int requestCode = getTargetRequestCode();
            Intent data = new Intent();
            data.putExtra(EXTRA_TIME, date);
            target.onActivityResult(requestCode, resultCode, data);
        }
    }

}
