package com.example.heleninsa.criminalintent.controller;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.heleninsa.criminalintent.R;

/**
 * Created by heleninsa on 2017/1/15.
 */

public abstract class SimpleFragmentActivity extends AppCompatActivity{

    protected abstract Fragment getFragment();

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        //获得Fragment管理器
        FragmentManager fm = getSupportFragmentManager();
        //如果之前创建过Fragment，可以通过指定的ID获取到
        //Fragment不是Activity，Activity 销毁Fragment不一定销毁
        Fragment fragment = fm.findFragmentById(R.id.frame_container);
        if(fragment == null) {
            fragment = getFragment();
            fm.beginTransaction().add(R.id.frame_container, fragment).commit();
        }
    }
}
