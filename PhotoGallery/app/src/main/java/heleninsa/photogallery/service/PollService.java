package heleninsa.photogallery.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.List;

import heleninsa.photogallery.R;
import heleninsa.photogallery.controller.PhotoGalleryActivity;
import heleninsa.photogallery.controller.QueryPreferences;
import heleninsa.photogallery.module.GalleryItem;
import heleninsa.photogallery.util.PhotoFetcher;

/**
 * Created by heleninsa on 2017/2/13.
 */

public class PollService extends IntentService {

    private final static String TAG = "pollservice";

    private final static long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    public PollService() {
        super(TAG);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!checkNetwork()) {
            return;
        }

        String query = QueryPreferences.getStoredQueryKey(this);
        String result_id = QueryPreferences.getStoredID(this);
        List<GalleryItem> items = new PhotoFetcher().fetchItems(query, 1);
        if(items.isEmpty() || result_id == null) {
            return;
        }
        String newId = items.get(0).getId();
        if(!newId.equals(result_id)) {

            Resources resources = getResources();
            Intent i = PhotoGalleryActivity.newIntent(this);
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentText(resources.getString(R.string.new_pictures_text))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify(0, notification);
        }
        QueryPreferences.setStoredID(this, newId);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if(isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isAlarmOn(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);

        return pi != null;
    }

    private boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean netAvailable = cm.getActiveNetworkInfo() != null;
        boolean isConnected = netAvailable && cm.getActiveNetworkInfo().isConnected();
        return isConnected;
    }

}
