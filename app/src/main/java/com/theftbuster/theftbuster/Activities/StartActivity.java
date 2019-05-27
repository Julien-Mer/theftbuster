package com.theftbuster.theftbuster.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.theftbuster.theftbuster.Helpers.Memory.DAO.*;
import com.theftbuster.theftbuster.Helpers.Memory.DAO.DAOBase;
import com.theftbuster.theftbuster.Helpers.Memory.Memory;


public class StartActivity extends AppCompatActivity {
    public String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DAOBase dbHelper = new DAOBase(getApplicationContext());
        SQLiteDatabase mDb = dbHelper.open();
        Memory.getMemory(mDb);
        mDb.close();

        if (CodeDAO.code == null || OwnerDAO.owner == null || StatesDAO.statesList == null) {
            Log.e("StartActivity", "Welcome");
            Intent welcomeActivity = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(welcomeActivity);
            finish();
        } else {
            Intent permissionsActivity = new Intent(getApplicationContext(), PermissionsActivity.class);
            startActivity(permissionsActivity);
            finish();
        }
    }
}