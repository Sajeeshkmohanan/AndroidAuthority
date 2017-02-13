package heleninsa.photogallery.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by heleninsa on 2017/2/13.
 */

public class PollJobService extends JobService {

    private final static int JOB_ID = 1;
    private PollTask mPollTask;

    public static void setServiceAlarm(Context context, boolean isOn) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        if (isOn) {
            JobInfo info = new JobInfo.Builder(JOB_ID, new ComponentName(context, PollJobService.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .setPeriodic(1000 * 60 * 15)
                    .setPersisted(true)
                    .build();
            scheduler.schedule(info);
        } else {
            scheduler.cancel(JOB_ID);
        }
    }

    public static boolean isAlarmOn(Context context) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        boolean isScheduled = false;
        for (JobInfo info : scheduler.getAllPendingJobs()) {
            if (info.getId() == JOB_ID) {
                isScheduled = true;
                break;
            }
        }
        return isScheduled;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        mPollTask = new PollTask();
        mPollTask.execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (mPollTask != null || !mPollTask.isCancelled()) {
            mPollTask.cancel(true);
        }
        return true;
    }

    private class PollTask extends AsyncTask<JobParameters, Void, Void> {
        @Override
        protected Void doInBackground(JobParameters... params) {
            PollService.checkPhotoUpdate(PollJobService.this);
            return null;
        }
    }

}
