package heleninsa.photogallery.controller;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by heleninsa on 2017/2/12.
 */

public class QueryPreferences {

    private final static String SEARCH_QUERY = "search_key";

    public static String getStoredQueryKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query_key) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(SEARCH_QUERY, query_key).apply();
    }

}
