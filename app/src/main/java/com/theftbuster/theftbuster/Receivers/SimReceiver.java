package com.theftbuster.theftbuster.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.theftbuster.theftbuster.Helpers.SimProtection;

/**
 * Created by Julien M on 01/02/2018.
 */


public class SimReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Change Sim", "Detected");
        SimProtection.waitSim(context);
    }

}