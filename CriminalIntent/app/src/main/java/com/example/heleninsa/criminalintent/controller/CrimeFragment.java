package com.example.heleninsa.criminalintent.controller;

import android.os.Bundle;
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
import android.widget.EditText;

import com.example.heleninsa.criminalintent.model.Crime;
import com.example.heleninsa.criminalintent.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by heleninsa on 2017/1/14.
 */

public class CrimeFragment extends Fragment {

    private Crime mCrime;

    private EditText mTitleField;
    private CheckBox mCrimeSolvedBox;
    private Button mDateButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /* Layout ID , Father View , USE Static way to manage Fragment ? */
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mCrimeSolvedBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mDateButton = (Button) view.findViewById(R.id.crime_date);

        mTitleField.addTextChangedListener( new TextWatcher() {
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒", Locale.CHINA);
        mDateButton.setText(format.format(mCrime.getDate()));

        mDateButton.setEnabled(false);

        mCrimeSolvedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return view;
    }
}
