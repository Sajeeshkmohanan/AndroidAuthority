package heleninsa.photogallery.controller;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import heleninsa.photogallery.R;
import heleninsa.photogallery.module.GalleryCache;
import heleninsa.photogallery.module.GalleryItem;
import heleninsa.photogallery.service.PollService;
import heleninsa.photogallery.util.PhotoFetcher;
import heleninsa.photogallery.util.ThumbnailDownloader;

/**
 * Created by heleninsa on 2017/2/6.
 */

public class PhotoGalleryFragment extends Fragment {

    private RecyclerView mPhotoRecyclerView;
    private View mLoadingView;

    private List<GalleryItem> mItems = new ArrayList<>();

    private int mPage = 1;

    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    private GalleryCache mCache;


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
        setHasOptionsMenu(true);
        loadImageList();
        mCache = new GalleryCache();

        //NetConnection
        mThumbnailDownloader = new ThumbnailDownloader<>(new Handler());
        mThumbnailDownloader.setListener(new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
            @Override
            public void onDownload(PhotoHolder target, String sourceUrl, Bitmap thumbnail) {
                Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                mCache.addImage(sourceUrl, thumbnail);
                target.bindPhoto(drawable);
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
    }

    private void setUpAdapter() {
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);
        final MenuItem search = menu.findItem(R.id.menu_search_view);
        final SearchView searchView = (SearchView) search.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                QueryPreferences.setStoredQuery(getContext(), query);
                //收起搜索栏和键盘
                searchView.onActionViewCollapsed();
                loadImageList();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery(QueryPreferences.getStoredQueryKey(getContext()), false);
            }
        });

        final MenuItem item = menu.findItem(R.id.menu_item_polling);
        if (PollService.isAlarmOn(getContext())) {
            item.setTitle(R.string.stop_polling);
        } else {
            item.setTitle(R.string.start_polling);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(getContext(), null);
                loadImageList();
                return true;
            case R.id.menu_item_polling:
                boolean turnOnAlarm = !PollService.isAlarmOn(getContext());
                PollService.setServiceAlarm(getContext(), turnOnAlarm);
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        //Init LoadingView
        mLoadingView = v.findViewById(R.id.fragment_photo_gallery_loading_view);
        mLoadingView.setVisibility(View.GONE);

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
                    //loadImageList next mPage
                    mPage++;
                    loadImageList();
                    Log.w("LOADMORE", "loadImageList more pics");
                } else if (first < onShow) {
                    //last mPage
                    mPage--;
                    if (mPage < 1) {
                        mPage = 1;
                    } else {
                        loadImageList();
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

    private void loadImageList() {
        new FetcherItemsTask(QueryPreferences.getStoredQueryKey(getContext())).execute(mPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        mThumbnailDownloader.quit();
        super.onDestroy();
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_photo_gallery_image_view, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem item = mGalleryItems.get(position);
            String url = item.getUrl();
            Bitmap image = mCache.getImage(url);
            if (image != null) {
                Drawable drawable = new BitmapDrawable(getResources(), image);
                holder.bindPhoto(drawable);
            } else {
                mThumbnailDownloader.queueThumbnail(holder, item.getUrl());
            }
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;


        public PhotoHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
        }

        public void bindPhoto(Drawable pic) {
            mImageView.setImageDrawable(pic);
        }

    }

    private class FetcherItemsTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        private String mQueryKey;

        public FetcherItemsTask(String query_key) {
            this.mQueryKey = query_key;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mPhotoRecyclerView != null && mLoadingView != null) {
                mPhotoRecyclerView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected List<GalleryItem> doInBackground(Integer... params) {
            int page;
            if (params == null) {
                page = 1;
            } else {
                page = params[0];
            }
            return new PhotoFetcher().fetchItems(mQueryKey, page);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            setUpAdapter();
            if (mPhotoRecyclerView != null && mLoadingView != null) {
                mPhotoRecyclerView.setVisibility(View.VISIBLE);
                mLoadingView.setVisibility(View.GONE);
            }
        }
    }
}
