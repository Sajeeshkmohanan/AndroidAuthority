package heleninsa.photogallery.module;

/**
 * Created by heleninsa on 2017/2/8.
 */

public class GalleryItem {

    private String id;
    //图片说明
    private String title;

    private String url_s;

    @Override
    public String toString() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url_s;
    }

    public void setUrl(String url) {
        url_s = url;
    }
}
