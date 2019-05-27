package com.theftbuster.theftbuster.Controllers;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.theftbuster.theftbuster.Activities.AppLockerActivity;
import com.theftbuster.theftbuster.Helpers.Memory.DAO.StatesDAO;
import com.theftbuster.theftbuster.Helpers.Memory.Data.States;
import com.theftbuster.theftbuster.Helpers.SettingsModifier;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

/**
 * Created by Julien M on 15/12/2017.
 */

public class DebuggingKiller extends Service {

    public DebuggingKiller() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("DebuggingKiller", "DebuggingKiller crée");
       Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                boolean adb = Settings.System.getInt(getContentResolver(), Settings.Global.ADB_ENABLED, 0) != 0;
               if(adb) SettingsModifier.changeSettingRoot(getApplicationContext(), Settings.Global.ADB_ENABLED, 0, "No Intent");
            }}, 0, 1000); // Toutes les 1 secondes car on a rien le temps de faire et economies d'energie

        super.onCreate();
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        //API level 11
        return START_STICKY; // permet de relancer le killer si jamais il est fermé
    }

    @Override
    public void onDestroy() {
        States debuggingKiller = StatesDAO.selectState("debuggingKiller");
        if(debuggingKiller.getValue() == 1) {
            Intent i = new Intent(this, DebuggingKiller.class);
            startService(i); // Le killer se relance
        }
        super.onDestroy();
    }

}