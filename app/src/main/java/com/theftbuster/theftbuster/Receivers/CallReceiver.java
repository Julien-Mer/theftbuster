package com.theftbuster.theftbuster.Receivers;

/**
 * Created by Julien M. on 17/04/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.theftbuster.theftbuster.Controllers.Core;
import com.theftbuster.theftbuster.Controllers.OverlayLock;
import com.theftbuster.theftbuster.Controllers.Variables;
import com.theftbuster.theftbuster.Helpers.Memory.DAO.OwnerDAO;
import com.theftbuster.theftbuster.Helpers.Memory.DAO.StatesDAO;


public class CallReceiver extends BroadcastReceiver {

    private int state;
    static Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); //TelephonyManager object
        CallReceiverListener customPhoneListener = new CallReceiverListener();
        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE); //Register our listener with TelephonyManager
    }

    /* Custom PhoneStateListener */
    public class CallReceiverListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int newState, String incomingNumber){
            Variables var = (Variables)mContext.getApplicationContext();

            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    //On ne fait rien
                    state = newState;
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK: // Déccroché
                    var.setInCall(true);
                    if(var.getInOwner()) {
                        OverlayLock.StateOwner(false);
                    }
                    state = newState;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    if(var.getInCall()){
                        state = newState;
                        var.setInCall(false);
                        if(StatesDAO.selectState("alarm").getValue() == 1) {
                            var.getAudio().setMode(AudioManager.MODE_NORMAL);
                            var.setVolumeProtection(false);
                        }
                        if(var.getInOwner()) {
                            OverlayLock.StateOwner(true);
                        }
                        //Answered Call which is ended
                    }
                    if((state == TelephonyManager.CALL_STATE_RINGING)){
                        state = newState;
                        //Rejected or Missed call
                    }
                    break;
            }
        }
    }
}
