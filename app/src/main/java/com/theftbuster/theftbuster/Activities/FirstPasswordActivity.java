package com.theftbuster.theftbuster.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.theftbuster.theftbuster.Controllers.Core;
import com.theftbuster.theftbuster.Helpers.Memory.DAO.*;
import com.theftbuster.theftbuster.Helpers.Memory.Data.Code;
import com.theftbuster.theftbuster.Helpers.Memory.DatabaseHandler;
import com.theftbuster.theftbuster.Helpers.Memory.Memory;
import com.theftbuster.theftbuster.R;

import static com.theftbuster.theftbuster.Helpers.Crypto.genSalt;
import static com.theftbuster.theftbuster.Helpers.Crypto.sha512;

public class FirstPasswordActivity extends AppCompatActivity {
public String code = "";
public String newCode = "";
public int step = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // On utilise une liste avec un for pour optimiser le code
        Button[] btnlist = {this.findViewById(R.id.btn1), this.findViewById(R.id.btn2), this.findViewById(R.id.btn3), this.findViewById(R.id.btn4), this.findViewById(R.id.btn5), this.findViewById(R.id.btn6), this.findViewById(R.id.btn7), this.findViewById(R.id.btn8), this.findViewById(R.id.btn9), this.findViewById(R.id.btn0)};
        final EditText viewCode = this.findViewById(R.id.viewCode);
        final TextView infoCode = this.findViewById(R.id.infoCode);
        infoCode.setText(getResources().getString(R.string.view_enterCode));
        for (Button btn : btnlist) {
            final String text = btn.getText().toString();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (code.length() >= 7) {
                        viewCode.setText(viewCode.getText().toString() + text);
                        code = code + text;
                        if (step == 1) {
                            newCode = code;
                            viewCode.setText("");
                            code = "";
                            step = 2;
                            infoCode.setText(getResources().getString(R.string.view_confirmCode));
                        }
                        else if (step == 2) {
                            if(code.equals(newCode)) {
                                viewCode.setText("");
                                code = "";

                                String salt = genSalt(); // On génère un sel de 10 caractères

                                DAOBase dbHelper = new DAOBase(getApplicationContext());
                                SQLiteDatabase mDb = dbHelper.open();
                                CodeDAO.code = new Code(sha512(newCode + salt), salt);
                                CodeDAO.addCode(mDb); // On enregistre le code
                                StatesDAO.initialise(mDb);
                                mDb.close();

                                Intent i = new Intent(FirstPasswordActivity.this, Core.class);
                                startService(i); // On démarre le core
                                Intent helpActivity = new Intent(getApplicationContext(), HelpActivity.class);
                                startActivity(helpActivity); // On ouvre l'application en mode tutorial
                                finish();
                            }
                            else {
                                Toast.makeText(FirstPasswordActivity.this, getResources().getString(R.string.errorPasswordDoesntMatch), Toast.LENGTH_SHORT).show();
                                infoCode.setText(getResources().getString(R.string.view_enterCode));
                                viewCode.setText("");
                                code = "";
                                step = 1;
                            }
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

}
