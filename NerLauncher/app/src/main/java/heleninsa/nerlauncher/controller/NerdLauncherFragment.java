package heleninsa.nerlauncher.controller;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import heleninsa.nerlauncher.R;

/**
 * Created by heleninsa on 2017/2/5.
 */
public class NerdLauncherFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public static NerdLauncherFragment newInstance() {
        Bundle args = new Bundle();

        NerdLauncherFragment fragment = new NerdLauncherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_nerd_launcher_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setUpAdapter();
        return v;
    }

    private void setUpAdapter() {
        Intent startUpIntent = new Intent(Intent.ACTION_MAIN);
        startUpIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager manager = getActivity().getPackageManager();
        List<ResolveInfo> activities = manager.queryIntentActivities(startUpIntent, 0);

        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(o1.loadLabel(pm).toString(), o2.loadLabel(pm).toString());
            }
        });

        ActivityAdapter adapter = new ActivityAdapter(activities);
        mRecyclerView.setAdapter(adapter);
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {

        private List<ResolveInfo> mResolveInfos;

        public ActivityAdapter(List<ResolveInfo> resolveInfos) {
            mResolveInfos = resolveInfos;
        }

        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_activity, parent, false);

            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            ResolveInfo info = mResolveInfos.get(position);
            holder.bindActivity(info);
        }

        @Override
        public int getItemCount() {
            return mResolveInfos.size();
        }
    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ResolveInfo mResolveInfo;
        private TextView mTextView;
        private ImageView mImageView;

        public ActivityHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.list_item_activity_name_text_view);
            mImageView = (ImageView) itemView.findViewById(R.id.list_item_activity_icon_image_view);

            mTextView.setOnClickListener(this);
        }

        public void bindActivity(ResolveInfo info) {
            mResolveInfo = info;
            PackageManager pm = getActivity().getPackageManager();
            CharSequence name = mResolveInfo.loadLabel(pm);
            Drawable icon = mResolveInfo.loadIcon(pm);
            mTextView.setText(name);
            mImageView.setImageDrawable(icon);
        }

        @Override
        public void onClick(View v) {
            ActivityInfo activityInfo = mResolveInfo.activityInfo;

            Intent i = new Intent(Intent.ACTION_MAIN).
                    setClassName(activityInfo.packageName, activityInfo.name).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(i);
        }
    }

}
