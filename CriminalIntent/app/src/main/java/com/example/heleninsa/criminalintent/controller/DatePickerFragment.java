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
import android.widget.DatePicker;

import com.example.heleninsa.criminalintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by heleninsa on 2017/1/16.
 */

public class DatePickerFragment extends DialogFragment {

    private final static String ARG__DATE = "date";

    public final static String EXTRA_DATE = "com.heleninsa.criminalintent.controller.date";

    private DatePicker mDatePicker;

    private Calendar mCalendar;

    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG__DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private DatePickerFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG__DATE);

        mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year, month, day, null);
        return new AlertDialog.Builder(getActivity()).
                setView(v).
                setTitle(R.string.date_picker_title).
                setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                                int minute = mCalendar.get(Calendar.MINUTE);
                                int second = mCalendar.get(Calendar.SECOND);
                                Date date = new GregorianCalendar(year, month, day, hour, minute, second).getTime();
                                sendResult(Activity.RESULT_OK, date);
                            }
                        }).
                create();
    }

    private void sendResult(int resultCode, Date date) {
        Fragment targetFragment = getTargetFragment();
        if (targetFragment != null) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATE, date);
            targetFragment.onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
    }
}
