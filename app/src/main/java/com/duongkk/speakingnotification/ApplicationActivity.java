package com.duongkk.speakingnotification;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.duongkk.adapter.AdapterApplication;
import com.duongkk.models.Application;
import com.duongkk.speakingnotification.Utils.Constants;
import com.duongkk.speakingnotification.Utils.SharedPref;
import com.duongkk.views.ProgressDialogCustom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ApplicationActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<Application> mListApps;
    private AdapterApplication mAdapter;
    private ProgressDialogCustom mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcv_application);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ApplicationTask myTask = new ApplicationTask();
        mProgress = new ProgressDialogCustom(this);
        myTask.execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        List<Application> listChecked = mAdapter.getListAppChecked();
        String data = "";
        if (listChecked != null && listChecked.size() > 0) {
            for (Application app : listChecked) {
                data += app.getmPackage() + getString(R.string.signal);
            }
            data = data.subSequence(0, data.length() - 1).toString();
            SharedPref.getInstance(this).putString(Constants.LIST_APP, data);
        }else{
            SharedPref.getInstance(this).putString(Constants.LIST_APP, "");
        }


        super.onBackPressed();

    }

    class ApplicationTask extends AsyncTask<Void, Void, List<Application>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress.showDialog();
        }

        @Override
        protected List<Application> doInBackground(Void... params) {
            return getInstalledAppList();
        }

        private List<Application> getInstalledAppList() {
            List<Application> listApps = new ArrayList<>();
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            final List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
            ResolveInfoComparator resolveInfoComparator = new ResolveInfoComparator(getPackageManager());
            Collections.sort(pkgAppsList, resolveInfoComparator);
            String[] listPackageChecked = SharedPref.getInstance(getBaseContext()).getString(Constants.LIST_APP, "").split(getApplicationContext()
                                                             .getString(R.string.signal));
            for (Object object : pkgAppsList) {
                ResolveInfo info = (ResolveInfo) object;
                Drawable icon = getBaseContext().getPackageManager().getApplicationIcon(info.activityInfo.applicationInfo);
                String strPackageName = info.activityInfo.applicationInfo.packageName.toString();
                final String title = (String) ((info != null) ? getBaseContext().getPackageManager().getApplicationLabel(info.activityInfo.applicationInfo) : "???");
                Application app = new Application();
                app.setmIcon(icon);
                app.setmName(title);
                app.setmPackage(strPackageName);
                for (int i = 0; i < listPackageChecked.length; i++) {
                    if(strPackageName.equals(listPackageChecked[i])){
                        app.setChecked(true);
                        break;
                    }
                }
                listApps.add(app);
            }
            return listApps;
        }

        @Override
        protected void onPostExecute(List<Application> applications) {
            super.onPostExecute(applications);
            mListApps = applications;
            mAdapter = new AdapterApplication(getApplicationContext(), mListApps);
            mRecyclerView.setAdapter(mAdapter);
            mProgress.hideDialog();
        }
    }

    private class ResolveInfoComparator implements Comparator<ResolveInfo> {
        final PackageManager packageManager;

        public ResolveInfoComparator(final PackageManager packageManager) {
            this.packageManager = packageManager;
        }

        @Override
        public int compare(final ResolveInfo resolveInfo, final ResolveInfo resolveInfo2) {
            final String appLabel = resolveInfo.loadLabel(packageManager).toString();
            final String appLabel2 = resolveInfo2.loadLabel(packageManager).toString();
            return appLabel.compareTo(appLabel2);
        }
    }

}
