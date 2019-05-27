package com.theftbuster.theftbuster.Helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.theftbuster.theftbuster.Commands.Call;
import com.theftbuster.theftbuster.Commands.Lock;
import com.theftbuster.theftbuster.Controllers.Core;
import com.theftbuster.theftbuster.Controllers.Variables;
import com.theftbuster.theftbuster.Helpers.Memory.DAO.OwnerDAO;
import com.theftbuster.theftbuster.Helpers.Memory.DAO.SimsDAO;
import com.theftbuster.theftbuster.Helpers.Memory.DAO.StatesDAO;
import com.theftbuster.theftbuster.Helpers.Memory.Data.States;
import com.theftbuster.theftbuster.Helpers.Memory.Memory;
import com.theftbuster.theftbuster.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kapte on 17/04/2018.
 */

public class SimProtection {

    public static void waitRio(final Context context) {
        Log.e("Sim", "waitRio");
        States waitingRio = StatesDAO.selectState("waitingRio");
        waitingRio.setValue(1);
        StatesDAO.updateState(Core.mDb, waitingRio);
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                States waitingRio = StatesDAO.selectState("waitingRio");
                if(waitingRio.getValue() == 1) {
                    Call.CallPhone(context, "3179", "No Number", true);
                    Log.e("waitRio", "Call");
                }
                else {
                    timer.cancel();
                }
            }}, 0, 20000);
        }


    static Boolean waitData = true;
    public static void waitData(final Context context) {
        Log.e("Sim", "waitData");
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final PhoneStateListener mPhoneListener = new PhoneStateListener() {
            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                Variables var = (Variables)context.getApplicationContext();
                Log.e("waitData", "Changed");
                if(waitData) {
                    int state = serviceState.getState();
                    Log.e("waitData", "State: " + state);
                    switch (state) {
                        case ServiceState.STATE_IN_SERVICE:
                            Log.e("waitData", "In service");
                            String trustNumber = OwnerDAO.owner.getTrustNumber();
                            SmsSender.SendSMS(context, trustNumber, context.getResources().getString(R.string.unknownSim));
                            waitData = false;
                            waitRio(context);
                            break;
                    }
                }
                super.onServiceStateChanged(serviceState);
            }
        };
        telephonyManager.listen(mPhoneListener, PhoneStateListener.LISTEN_SERVICE_STATE);
    }

    public static void waitSim(final Context context) {
        Log.e("SimProtection", "waitSim");
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephonyManager.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_READY:
                checkSim(context);
                break;
        }
    }
    public static String getSim(Context context) {
        Log.e("SimProtection", "Get");
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ContextCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED) { // Verification si la permission est accordée
                return telephonyManager.getSimSerialNumber();
            } else {
                Log.e("Get Sim","Autorisation Sim non accordée");
                return "";
            }
        } catch (NullPointerException ex) { return ""; }
    }

    public static void checkSim(Context context) {
        Variables var = (Variables)context.getApplicationContext();
        try {
            String sim = getSim(context);
            if (!sim.equals("")) {
                if (sim.equals(SimsDAO.getSim(0)) | sim.equals(SimsDAO.getSim(1)) | sim.equals(SimsDAO.getSim(2)))
                    return; // Stoppe la fonction ici si la SIM est enregistrée
                if(!var.getLockSim()) {
                    var.setLockSim(true);
                    Lock.Lock(context, "No Number"); // Verrouillage du téléphone
                    waitData(context);
                }
            }
        } catch (Exception ex) { Log.e("SimProtection", "Aucune Sim"); }

    }
}
