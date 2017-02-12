package heleninsa.photogallery.module;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by heleninsa on 2017/2/12.
 */

public class GalleryCache {

    private LruCache<String, Bitmap> mCache;

    public GalleryCache() {
        int memory_size = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cache_size = memory_size >> 3;
        mCache = new LruCache<String, Bitmap>(cache_size) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //使用 KB 计算
                return value.getByteCount() / 1024;
            }
        };
    }

    public void addImage(String url, Bitmap image) {
        if (mCache.get(url) == null) {
            mCache.put(url, image);
        }
    }

    public Bitmap getImage(String key) {
        return mCache.get(key);
    }

}
