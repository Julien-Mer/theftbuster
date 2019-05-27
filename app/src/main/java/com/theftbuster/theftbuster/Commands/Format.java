package com.theftbuster.theftbuster.Commands;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.theftbuster.theftbuster.Helpers.SmsSender;
import com.theftbuster.theftbuster.Helpers.Memory.Memory;
import com.theftbuster.theftbuster.R;

import java.io.File;

/**
 * Created by Hugo.C on 15/01/2018.
 */

public class Format {
    public static void Format(Context context, String phoneNumber) {
        Log.e("Format", "Formatage débuté");
        String[] directories = { "Pictures", "documents", "document", "DCIM", "Movies", "Download", "Music", "Snapchat", "CloudDrive", "Sounds", "Android/data/com.android.providers.media/thumbnail_cache"};
        for(String directory : directories) {
            try {
                Log.e("Format","Directory: " + directory);
                String pathFiles = Environment.getExternalStorageDirectory().toString() + "/" + directory;
                DeleteAll(context, pathFiles);
                DeleteAll(context, pathFiles); // On en ajoute un par sécurité
            } catch (Exception ex) { }
        }

        // Si c'est une commande reçue par sms, on envoie la réponse
        if (!phoneNumber.equals("No Number")) {
            SmsSender.SendSMS(context, phoneNumber, context.getResources().getString(R.string.format));
        }
    }

    public static void DeleteAll(Context context, String path) {
        File directoryFiles = new File(path);

        File[] objects = directoryFiles.listFiles();
        for(File object : objects) {
            if (object.isDirectory()) {
                String newPath = object.getPath();
                DeleteAll(context, newPath);
            } else {
                Log.e("Format", "File: " + object.getAbsolutePath());
                object.delete();
            }
        }
    }

}