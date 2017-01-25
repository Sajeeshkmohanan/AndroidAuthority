package com.example.heleninsa.criminalintent.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.heleninsa.criminalintent.R;
import com.example.heleninsa.criminalintent.model.Crime;
import com.example.heleninsa.criminalintent.model.CrimeLab;
import com.example.heleninsa.criminalintent.util.PictureUtils;

import java.io.File;
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
    private final static String DIALOG_IMAGE =  "Dialog_Image";

    private final static int REQUEST_DATE = 0;
    private final static int REQUEST_TIME = 1;
    private final static int REQUEST_CONTACT = 2;
    private final static int REQUEST_PHOTO = 3;

    private Crime mCrime;
    private File mPhotoFile;

    private EditText mTitleField;
    private CheckBox mCrimeSolvedBox;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mDeleteButton;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    private boolean mIsDeleted = false;

    private CrimeFragment() {
    }

    public static CrimeFragment newInstance(UUID crimeID) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeID);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        CrimeLab lab = CrimeLab.getInstance(getActivity());
        mCrime = lab.getCrime(crimeId);
        mPhotoFile = lab.getPhotoFile(mCrime);
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
        mDeleteButton = (Button) view.findViewById(R.id.delete_crime);
        mReportButton = (Button) view.findViewById(R.id.crime_report);
        mSuspectButton = (Button) view.findViewById(R.id.crime_suspect);
        mCallButton = (Button) view.findViewById(R.id.crime_call);
        mPhotoButton = (ImageButton) view.findViewById(R.id.crime_camera);
        mPhotoView = (ImageView) view.findViewById(R.id.crime_photo);

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
        mDateButton.setOnClickListener((v) -> {
            FragmentManager manager = getFragmentManager();
            DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
            dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
            dialog.show(manager, DIALOG_DATE);
        });


        mTimeButton.setOnClickListener((v) -> {
            FragmentManager manager = getFragmentManager();
            TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
            dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
            dialog.show(manager, DIALOG_TIME);
        });

        mCrimeSolvedBox.setChecked(mCrime.isSolved());
        mCrimeSolvedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mDeleteButton.setOnClickListener((v) -> {
            CrimeLab.getInstance(getActivity()).removeCrime(mCrime);
            mIsDeleted = true;
            getActivity().finish();

        });

        mReportButton.setOnClickListener((v) -> {
            String report = getCrimeReport();
            Intent intent = new Intent(Intent.ACTION_SENDTO);

//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TEXT, report);
//                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
            intent.setData(Uri.parse("smsto:" + "15062278989"));
            intent.putExtra("sms_body", report);
            intent = Intent.createChooser(intent, getString(R.string.send_report));
//
//                Intent intent = ShareCompat.IntentBuilder.from(CrimeFragment.this.getActivity()).
//                        setChooserTitle(getString(R.string.send_report)).
//                        setType("text/plain").
//                        setText(getCrimeReport()).
//                        setSubject(getString(R.string.crime_report_subject)).
//                        addEmailBcc("15062278989").
//                        createChooserIntent();

            startActivity(intent);
        });

        PackageManager manager = getActivity().getPackageManager();
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        mSuspectButton.setOnClickListener((v) -> {
            startActivityForResult(pickContact, REQUEST_CONTACT);
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }


        if (manager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mCallButton.setOnClickListener((v) -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:15062278989"));
            startActivity(intent);
        });

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (manager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY) == null || mPhotoFile == null) {
            mPhotoButton.setEnabled(false);
        } else {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        mPhotoButton.setOnClickListener(v -> {
            startActivityForResult(captureImage, REQUEST_PHOTO);
        });

        ViewTreeObserver observer = mPhotoView.getViewTreeObserver();

        observer.addOnGlobalLayoutListener(() -> {
            int w = mPhotoView.getMeasuredWidth();
            int h = mPhotoView.getMeasuredHeight();
            updatePhotoView(w, h);
        });

        mPhotoView.setOnClickListener(v->{
            FragmentManager fragmentManager = getFragmentManager();
            ImageFragment img_fragment = ImageFragment.newInstance(mPhotoFile.getPath());
            img_fragment.show(fragmentManager, DIALOG_IMAGE);
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            String date_result = DatePickerFragment.EXTRA_DATE;
            Date date = (Date) data.getSerializableExtra(date_result);
            onReceiveDate(date);
        } else if (requestCode == REQUEST_TIME) {
            String date_result = TimePickerFragment.EXTRA_TIME;
            Date date = (Date) data.getSerializableExtra(date_result);
            onReceiveDate(date);
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String[] c_queryField = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};
            String[] p_queryField = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor c = getActivity().getContentResolver().query(contactUri, c_queryField, null, null, null);
            Cursor p = null;
            try {
                if (c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);

                //获取 _ID
                String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                //指定查询范围 _ID = ? , id
                //查询表Phone.
                //指定查询范围为 Phone.Number
                p = getActivity().getContentResolver().query(phoneUri, p_queryField, ContactsContract.Contacts._ID + " =?", new String[]{id}, null);
                //判断为空。（没必要
                if (p.getCount() != 0) {
                    p.moveToFirst();

                    String phone = p.getString(p.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.e("Number", phone);
                }
            } finally {
                c.close();
                if (p != null) {
                    p.close();
                }
            }
        }
//        else if (requestCode == REQUEST_PHOTO) {
//            updatePhotoView();
//        }
    }

    private void onReceiveDate(Date date_received) {
        mCrime.setDate(date_received);
        setDateText();
    }

    private void setDateText() {
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        SimpleDateFormat time_format = new SimpleDateFormat("hh时mm分", Locale.CHINA);
        mDateButton.setText(date_format.format(mCrime.getDate()));
        mTimeButton.setText(time_format.format(mCrime.getDate()));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mIsDeleted) {
            CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
        }
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    private void updatePhotoView(int w, int h) {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), w, h);
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
