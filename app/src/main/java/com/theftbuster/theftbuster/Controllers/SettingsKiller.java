package com.theftbuster.theftbuster.Controllers;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.theftbuster.theftbuster.Activities.AppLockerActivity;
import com.theftbuster.theftbuster.Activities.LoginActivity;
import com.theftbuster.theftbuster.Helpers.Memory.DAO.StatesDAO;
import com.theftbuster.theftbuster.Helpers.Memory.Data.States;
import com.theftbuster.theftbuster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

/**
 * Created by Julien M on 15/12/2017.
 */

public class SettingsKiller extends Service {
public static int permission = 0;

    public SettingsKiller() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("SettingsKiller", "SettingsKiller crée");
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                if(StatesDAO.statesList == null) {
                    Intent i = new Intent(SettingsKiller.this, Core.class);
                    startService(i); // On démarre le core
                }
                States settingsKiller = StatesDAO.selectState("settingsKiller");
                if(settingsKiller.getValue() == 1) {
                    String currentApp = "";
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                        long time = System.currentTimeMillis();
                        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
                        if (appList != null && appList.size() > 0) {
                            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                            for (UsageStats usageStats : appList) {
                                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                            }
                            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                            }
                        }
                    } else {
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
                        currentApp = tasks.get(0).processName;
                    }

                    if (currentApp.equalsIgnoreCase("com.android.settings")) {
                        if (permission == 0) { // Si elle n'a pas été autorisée
                            Intent AppLocker = new Intent(getApplicationContext(), AppLockerActivity.class);
                            AppLocker.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(AppLocker);
                        }
                    } else { // Il y a changement donc on retire l'autorisation
                        if (permission == 1) permission = 0;
                    }
                }
                else {
                    Log.e("SettingsKiller", "Kill");
                    timer.cancel();
                }

    }}, 0, 1000); // Toutes les 1 secondes car on a rien le temps de faire et economies d'energie
        super.onCreate();
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        //API level 11
        return START_STICKY; // permet de relancer le SettingsKiller si jamais il est fermé
    }

    @Override
    public void onDestroy() {
        States settingsKiller = StatesDAO.selectState("settingsKiller");
        if(settingsKiller.getValue() == 1) {
            Intent i = new Intent(this, SettingsKiller.class);
            startService(i); // Le killer se relance
        }
        super.onDestroy();
    }

}