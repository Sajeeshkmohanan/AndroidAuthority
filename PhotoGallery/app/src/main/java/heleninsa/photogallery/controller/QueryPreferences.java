package heleninsa.photogallery.controller;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by heleninsa on 2017/2/12.
 */

public class QueryPreferences {

    private final static String SEARCH_QUERY = "search_key";
    private final static String LAST_RESULT_ID = "last_result_id";

    public static String getStoredQueryKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query_key) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(SEARCH_QUERY, query_key).apply();
    }

    public static String getStoredID(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LAST_RESULT_ID, null);
    }

    public static void setStoredID(Context context, String id) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(LAST_RESULT_ID, id).apply();
    }

}
