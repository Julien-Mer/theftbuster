package com.theftbuster.theftbuster.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.theftbuster.theftbuster.Controllers.Core;
import com.theftbuster.theftbuster.Controllers.SettingsKiller;
import com.theftbuster.theftbuster.Helpers.Password;
import com.theftbuster.theftbuster.R;
import com.theftbuster.theftbuster.Receivers.Admin;

import java.io.IOException;
import java.util.List;

public class AppLockerActivity extends AppCompatActivity {
   static String code = "";

    @Override
    protected void onStart() {
        super.onStart();

        setContentView(R.layout.login); // On affiche le layout
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Partie Listeners
        final EditText viewCode = this.findViewById(R.id.viewCode);

        // On utilise une liste avec un for pour optimiser le code
        Button[] btnlist = {this.findViewById(R.id.btn1),
                this.findViewById(R.id.btn2),
                this.findViewById(R.id.btn3),
                this.findViewById(R.id.btn4),
                this.findViewById(R.id.btn5),
                this.findViewById(R.id.btn6),
                this.findViewById(R.id.btn7),
                this.findViewById(R.id.btn8),
                this.findViewById(R.id.btn9),
                this.findViewById(R.id.btn0)};
        final TextView infoCode = this.findViewById(R.id.infoCode);

        for (Button btn : btnlist) {
            final String text = btn.getText().toString();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (code.length() >= 5) {
                        viewCode.setText(viewCode.getText().toString() + text);
                        code = code + text;
                            if (Password.onPasswordModified(getApplicationContext(), code)) {
                                Log.e("AppLocker", "Code juste");
                                viewCode.setText(""); // On efface le code visuellement
                                code = ""; // On efface le code
                                Log.e("AppLocker", "Textbox effacée");

                                SettingsKiller.permission = 1;
                                finishAffinity();
                            } else {
                                viewCode.setText("");
                                code = "";
                                Password.onPasswordFailed(getApplicationContext()); // Sinon on appelle le onPasswordFailed
                            }
                    }
                    else {
                        viewCode.setText(viewCode.getText().toString() + text);
                        code = code + text;
                    }
                }
            });
        }
       Button btndel = this.findViewById(R.id.btnDelete);
        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.length() > 0) {
                    viewCode.setText(viewCode.getText().toString().substring(0, viewCode.getText().toString().length() - 1));
                    code = code.substring(0, code.length() - 1);
                }
            }
        });

    }

    public boolean firstOpen = true;
    @Override
    public void onResume() {
        if(!firstOpen) {
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivity(loginActivity);
            this.finish();
        }
        firstOpen = false;
        super.onResume();
    }

}