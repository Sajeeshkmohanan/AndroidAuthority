package heleninsa.photogallery.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by heleninsa on 2017/2/9.
 */

public class ThumbnailDownloader<T> extends HandlerThread {

    private final static String TAG = "ThumbnailDownloader";
    private final static int MESSAGE_DOWNLOAD = 0;
    private boolean mHasQuit = false;
    private Handler mResponseHandle;

    private ThumbnailDownloadListener<T> mListener;

    private Handler mHandler;

    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();

    public ThumbnailDownloader(Handler responseHandle) {
        super(TAG);
        mResponseHandle = responseHandle;
    }

    public void queueThumbnail(T target, String url) {
        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();
        }
    }

    public void setListener(ThumbnailDownloadListener<T> listener) {
        mListener = listener;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    handleRequest(target);
                }
            }
        };

    }

    private void handleRequest(final T target) {
        final String url = mRequestMap.get(target);
        if (url != null) {
            try {
                byte[] bitmapBytes = PhotoFetcher.getUrlBytes(url);
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                //使用主线程的 handle 进行响应以更新UI 事件
                mResponseHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mRequestMap.get(target) != url || mHasQuit) {
                            return;
                        }
                        //清楚消息
                        mRequestMap.remove(target);
                        mListener.onDownload(target, url, bitmap);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    public interface ThumbnailDownloadListener<T> {
        public void onDownload(T target, String sourceUrl, Bitmap thumbnail);
    }
}
