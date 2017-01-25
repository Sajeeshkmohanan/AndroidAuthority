package com.example.heleninsa.criminalintent.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.example.heleninsa.criminalintent.R;
import com.example.heleninsa.criminalintent.util.PictureUtils;

/**
 * Created by heleninsa on 2017/1/25.
 */

public class ImageFragment extends DialogFragment {

    private final static String ARG_IMG_PATH = "path";

    public static ImageFragment newInstance(String img_path) {
        Bundle args = new Bundle();
        args.putString(ARG_IMG_PATH, img_path);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String path = getArguments().getString(ARG_IMG_PATH);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo_details, null);
        ImageView img = (ImageView) v.findViewById(R.id.dialog_photo_view);

        img.setImageBitmap(PictureUtils.getScaledBitmap(path, getActivity()));
//        ViewTreeObserver observer = img.getViewTreeObserver();
//        observer.addOnGlobalLayoutListener(()->{
//            int w = img.getMeasuredWidth();
//            int h = img.getMeasuredHeight();
//            img.setImageBitmap(PictureUtils.getScaledBitmap(path, w, h));
//        });

        return new AlertDialog.Builder(getActivity()).setTitle("Photo Details").setView(v).create();
    }
}
