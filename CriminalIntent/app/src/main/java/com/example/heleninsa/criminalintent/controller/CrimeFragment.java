package com.example.heleninsa.criminalintent.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.heleninsa.criminalintent.model.Crime;
import com.example.heleninsa.criminalintent.R;
import com.example.heleninsa.criminalintent.model.CrimeLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by heleninsa on 2017/1/14.
 */

public class CrimeFragment extends Fragment {

    private final static String ARG_CRIME_ID = "crime_id";
    private final static String DIALOG_DATE = "Dialog_Date";
    private final static String DIALOG_TIME = "Dialog_Time";

    private final static int REQUEST_DATE = 0;
    private final static int REQUEST_TIME = 1;

    private Crime mCrime;

    private EditText mTitleField;
    private CheckBox mCrimeSolvedBox;
    private Button mDateButton;
    private Button mTimeButton;

    public static CrimeFragment newInstance(UUID crimeID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeID);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CrimeFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Layout ID , Father View , USE Static way to manage Fragment ? */
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mCrimeSolvedBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mDateButton = (Button) view.findViewById(R.id.crime_date);
        mTimeButton = (Button) view.findViewById(R.id.crime_time);

        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        setDateText();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mCrimeSolvedBox.setChecked(mCrime.isSolved());
        mCrimeSolvedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        String  date_result = null ;
        if(requestCode == REQUEST_DATE) {
            date_result = DatePickerFragment.EXTRA_DATE;
        } else if (requestCode == REQUEST_TIME) {
            date_result = TimePickerFragment.EXTRA_TIME;
        }
        Date date = (Date) data.getSerializableExtra(date_result);
        onReceiveDate(date);
    }

    private void onReceiveDate(Date date_received) {
        mCrime.setDate(date_received);
        setDateText();
    }

    private void setDateText() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分", Locale.CHINA);
        mDateButton.setText(format.format(mCrime.getDate()));
    }

}
