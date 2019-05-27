package com.theftbuster.theftbuster.Commands;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.theftbuster.theftbuster.Controllers.Core;
import com.theftbuster.theftbuster.Controllers.Variables;
import com.theftbuster.theftbuster.Helpers.Memory.DAO.StatesDAO;
import com.theftbuster.theftbuster.Helpers.Memory.Data.States;
import com.theftbuster.theftbuster.Helpers.Memory.Memory;
import com.theftbuster.theftbuster.Helpers.TakePhoto;

/**
 * Created by Hugo.C on 15/01/2018.
 */

public class Photo {
    public static void detectPhoto(Context context, String phoneNumber) {
        Log.e("detectPhoto", "detectPhoto débuté");

        States photo = StatesDAO.selectState("photo");
        photo.setValue(1);
        StatesDAO.updateState(Core.mDb, photo); // On l'enregistre en mémoire

        Intent service = new Intent(context, TakePhoto.class);
        service.putExtra("phoneNumber", phoneNumber);
        context.startService(service);
    }
}