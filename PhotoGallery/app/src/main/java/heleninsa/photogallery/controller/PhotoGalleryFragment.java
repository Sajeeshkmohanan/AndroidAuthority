package heleninsa.photogallery.controller;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import heleninsa.photogallery.R;
import heleninsa.photogallery.module.GalleryItem;
import heleninsa.photogallery.util.PhotoFetcher;

/**
 * Created by heleninsa on 2017/2/6.
 */

public class PhotoGalleryFragment extends Fragment {

    private RecyclerView mPhotoRecyclerView;

    private List<GalleryItem> mItems = new ArrayList<>();

    private int mPage = 1;

    public static PhotoGalleryFragment newInstance() {
        Bundle args = new Bundle();

        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("Net", "N H");
        setRetainInstance(true);
        load();
        //NetConnection
    }

    private void setUpAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //第一次设置后就要把它 remove 掉， 不然无限循环
                mPhotoRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.w("LOADMORE", "layout");
                updateLayout();
            }
        });
        updateLayout();
        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int onShow = recyclerView.getChildCount();
                int total = recyclerView.getAdapter().getItemCount();
                int first = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
//                Log.w("LOADMORE", first + " pic");
                if ((first + onShow) + 1 >= total) {
                    recyclerView.scrollToPosition(0);
                    //load next mPage
                    mPage++;
                    load();
                    Log.w("LOADMORE", "load more pics");
                } else if (first < onShow) {
                    //last mPage
                    mPage--;
                    if(mPage < 1) {
                        mPage = 1;
                    } else {
                        load();
                    }
                }
            }
        });
        setUpAdapter();

        return v;
    }

    private void updateLayout() {
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int num = 3;
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), num));
    }

    private void load() {
        new FetcherItemsTask().execute(mPage);
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            holder.bindGalleryItem(mGalleryItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private TextView mText;

        private GalleryItem mGalleryItem;

        public PhotoHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item) {
            mGalleryItem = item;
            mText.setText(mGalleryItem.getTitle());
        }


    }

    private class FetcherItemsTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Integer... params) {
            int page;
            if (params == null) {
                page = 1;
            } else {
                page = params[0];
            }
            return new PhotoFetcher().fetchItems(page);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            setUpAdapter();
        }
    }
}
