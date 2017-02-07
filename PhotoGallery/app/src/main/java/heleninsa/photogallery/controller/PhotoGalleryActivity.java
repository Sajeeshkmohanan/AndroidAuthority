package heleninsa.photogallery.controller;

import android.support.v4.app.Fragment;

public class PhotoGalleryActivity extends SimpleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
