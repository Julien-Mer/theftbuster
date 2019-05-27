package com.theftbuster.theftbuster.Commands;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.theftbuster.theftbuster.Controllers.Variables;
import com.theftbuster.theftbuster.Helpers.SmsSender;
import com.theftbuster.theftbuster.R;


/**
 * Created by Julien M. on 16/04/2018.
 */

public class Call {

    static Variables var;

    public static void CallPhone(Context context, String target, String phoneNumber, boolean hidden) {
        if(!hidden) {
            Log.e("CallPhone", "Not hidden");
            var = (Variables) context.getApplicationContext();
            var.setVolumeProtection(true);
            var.getAudio().setMode(AudioManager.MODE_IN_CALL); // On met le téléphone en communication car on peut forcer l'utilisation des hauts-parleurs
            var.getAudio().setStreamVolume(AudioManager.STREAM_VOICE_CALL, var.getAudio().getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);
            var.getAudio().setSpeakerphoneOn(true); // On active les hauts-parleurs
            }
        String uri = "tel:" + target.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // New task car on ouvre une activité depuis un service
        intent.setData(Uri.parse(uri));
        if (ContextCompat.checkSelfPermission(context, "android.permission.CALL_PHONE") == PackageManager.PERMISSION_GRANTED) { // Verification si la permission est accordée
            context.startActivity(intent); // On envoie l'intent pour qu'il appelle
            if (!phoneNumber.equals("No Number"))
            SmsSender.SendSMS(context, phoneNumber, context.getResources().getString(R.string.callStarted));
        }
        else {
            if (!phoneNumber.equals("No Number"))
            SmsSender.SendSMS(context, phoneNumber, context.getResources().getString(R.string.errorCall));
        }
    }
}
