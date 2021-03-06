package heleninsa.photogallery.util;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import heleninsa.photogallery.module.GalleryItem;

/**
 * Created by heleninsa on 2017/2/6.
 */

public class PhotoFetcher {

    private final static String API_KEY = "b0506759b4ec77ddcece26fb1eebb4fe";

    private final static String API_PS = "0474781d99be478b";

    private final static String METHOD_GET_RECENT = "flickr.photos.getRecent";

    private final static String METHOD_SEARCH = "flickr.photos.search";

    private List<GalleryItem> mGalleryItems = new ArrayList<>();


    public static byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + " : with" + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }

            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }

    }

    public static String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    /**
     * @param keyword : Search Keyword
     * @param page    : Page of image
     * @return
     */
    public List<GalleryItem> fetchItems(String keyword, int page) {
//        Log.d("Net", "Here");
        try {
            String url = urlConstructor(keyword, page);

            String jsonString = getUrlString(url);
//            Log.d("Net", "fetchItems:  " + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
//            JsonObject jsonObject = new JsonObject();

            parseItems(mGalleryItems, jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mGalleryItems;
    }

    private String urlConstructor(String keyword, int page) {

        Uri.Builder url = Uri.parse("https://api.flickr.com/services/rest/").buildUpon().
                appendQueryParameter("format", "json").
                appendQueryParameter("nojsoncallback", "1").
                appendQueryParameter("page", String.format("%d", page < 1 ? 1 : page)).
                appendQueryParameter("extras", "url_s").
                appendQueryParameter("api_key", API_KEY);
        String method;
        if (keyword == null) {
            method = METHOD_GET_RECENT;

        } else {
            method = METHOD_SEARCH;
            url.appendQueryParameter("text", keyword);
        }
        url.appendQueryParameter("method", method);
        return url.build().toString();
    }


    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws JSONException {
        JSONObject photosJson = jsonBody.getJSONObject("photos");
        JSONArray photoArray = photosJson.getJSONArray("photo");
        for (int i = 0; i < photoArray.length(); i++) {
            JSONObject object = photoArray.getJSONObject(i);
            if (!object.has("url_s")) {
                continue;
            }
            GalleryItem item = new GalleryItem();
            item.setTitle(object.getString("title"));
            item.setId(object.getString("id"));
            item.setUrl(object.getString("url_s"));
            items.add(item);
        }
    }

    //Bean 一定要写全
    private void parseItems(List<GalleryItem> items, JsonObject jsonBody) {
        JsonObject photos = jsonBody.get("photos").getAsJsonObject();
        JsonArray photoArray = photos.get("photo").getAsJsonArray();
        for (int i = 0; i < photoArray.size(); i++) {
            JsonObject each = photoArray.get(i).getAsJsonObject();
            Gson gson = new Gson();
            GalleryItem item = gson.fromJson(each, GalleryItem.class);
            items.add(item);
        }
    }

}
