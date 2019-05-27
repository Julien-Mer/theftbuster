package com.theftbuster.theftbuster.Helpers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import static com.theftbuster.theftbuster.Helpers.Crypto.*;
import static com.theftbuster.theftbuster.Controllers.Variables.*;

public class Backup {
    static String[] paths = new String[]{ "/Android/Security", "/TheftBuster" }; // Les dossiers où l'on veut une Backup

    public static void createBackup(Context context) {
        for(String path : paths) {
            File dir = new File(Environment.getExternalStorageDirectory() + path, "");
            if (!dir.exists()) {
                dir.mkdirs(); // On crée le dossier s'il n'existe pas
            }
            try {
                File file = new File(dir, "." + crypt(context, "backup"));
                //TODO Save Backup
                /*
                String value = crypt(context, getValueCode(context)) + "/" + crypt(context, getValueInstance(context)) + "/" + crypt(context, getValueConfig(context));
                FileOutputStream out = new FileOutputStream(file);
                out.write(value.getBytes());
                out.close();
                */
            } catch (Exception ex) { }
        }
    }

    public static void deleteBackup(Context context) {
        for (String path : paths) {
            File dir = new File(Environment.getExternalStorageDirectory() + path, "");
            if (dir.exists()) {
                try {
                    File file = new File(dir, "." + crypt(context, "backup"));
                    file.delete();
                } catch (Exception ex) {
                }
            }
        }
    }

    public static String[] getBackup(Context context) {
        for(String path : paths) { // Pour chaque dossier
            File dir = new File(Environment.getExternalStorageDirectory() + path, ""); // On définit le dossier
            try {
            File file = new File(dir, "." + crypt(context, "backup")); // On définit le fichier backup
                int length = (int) file.length();
                byte[] bytes = new byte[length];
                FileInputStream in = new FileInputStream(file); // On instancie un lecteur pour lire les données
                try {
                    in.read(bytes); // On lit le nombre de bytes (la taille du fichier)
                } finally {
                    in.close(); // On ferme le lecteur
                }
                String backup = new String(bytes); // On convertit les bytes en String
                String[] values = backup.split("/"); // On split pour séparer les différentes parties de la mémoire
                String code = decrypt(context, values[0]); // On déchiffre tout
                String instance = decrypt(context, values[1]);
                String config = decrypt(context, values[2]);
                String salt = decrypt(context, values[3]);

                if(!code.equals("Exception") | !instance.equals("Exception") | !config.equals("Exception")) // Si les valeurs sont bien décryptées elles n'ont pas été modifiées donc on peut les utiliser
                    return new String[] { values[0], values[1], values[2] };
            } catch (Exception ex) { Log.e("Backup", "Exception: " + ex.getMessage()); }
        }
        return new String[] {"Exception"}; // Il n'y a pas eu de backup trouvée ou non modifiée
    }

}
