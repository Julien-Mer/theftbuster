package com.theftbuster.theftbuster.Controllers;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Julien M.
 */

public class Variables extends Application {
    private Application Var = this;
    private static SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0); // Initialisation du soundpool
    private static SoundPool soundPoolCall = new SoundPool(1, AudioManager.STREAM_VOICE_CALL, 0); // Initialisation du soundpool
    private static AudioManager audio;
    private static int sirenManager; // Id du manager de la sirène pour pouvoir y accéder
    private static boolean airplane_mode, gps_mode, inOwner, inCall, isRoot, lockSim, confirmFormat, volumeProtection; // Statut des modes
    private static SecretKeySpec aesKey;

    //region Variables Generales

    public Application getVar() { return Var; }

    public static int getSirenManager() {
        return sirenManager;
    }

    public void setSirenManager(int value) {
        sirenManager = value;
    }

    public static SoundPool getSoundPool() {
        return soundPool;
    }

    public void setSoundPool(SoundPool value) {
        soundPool = value;
    }

    public static AudioManager getAudio() {
        return audio;
    }

    public void setAudio(AudioManager value) {
        audio = value;
    }

    //endregion

    //region Variables Statut

    public boolean getAirPlane_Mode() {
        return airplane_mode;
    }

    public void setAirplane_mode(boolean value) {
        airplane_mode = value;
    }

    public boolean getGps_mode() {
        return gps_mode;
    }

    public void setGps_mode(boolean value) {
        gps_mode = value;
    }

    public boolean getInOwner() {
        return inOwner;
    }

    public void setInOwner(boolean value) {
        inOwner = value;
    }

    public boolean getInCall() {
        return inCall;
    }

    public void setInCall(boolean value) {
        inCall = value;
    }

    public boolean getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean value) {
        isRoot = value;
    }

    public boolean getLockSim() {
        return lockSim;
    }

    public void setLockSim(boolean value) {
        lockSim = value;
    }

    public boolean getConfirmFormat() {
        return confirmFormat;
    }

    public void setConfirmFormat(boolean value) {
        confirmFormat = value;
    }

    public void setVolumeProtection(boolean value) { volumeProtection = value; }

    public boolean getVolumeProtection() { return volumeProtection; }

    //endregion


    public boolean locate;

    public boolean getLocate() {
        return this.locate;
    }

    public void setLocate(Boolean bool) {
        this.locate = bool;
    }

    //region Cryptographie
    public SecretKeySpec getKey() {
        return aesKey;
    }

    public void setKey(SecretKeySpec key) {
        aesKey = key;
    }

    //enregion
}
