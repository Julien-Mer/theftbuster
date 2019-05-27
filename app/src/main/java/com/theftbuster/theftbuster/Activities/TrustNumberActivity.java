package com.theftbuster.theftbuster.Activities;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.theftbuster.theftbuster.Helpers.Memory.DAO.*;
import com.theftbuster.theftbuster.R;

public class TrustNumberActivity extends AppCompatActivity {

     static EditText textTrustNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trustnumber);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /* Définition des objets */
        Button btnSave = findViewById(R.id.btnSave);
        textTrustNumber = findViewById(R.id.textTrustNumber);

        this.findViewById(R.id.btnContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() { // Evenement clic sur sauvegarder
            @Override
            public void onClick(View v) {
               String trustNumber = textTrustNumber.getText().toString();
                if(trustNumber.toString().trim().length() > 6) {

                    DAOBase dbHelper = new DAOBase(getApplicationContext());
                    SQLiteDatabase mDb = dbHelper.open();
                    OwnerDAO.getOwner(mDb);
                    OwnerDAO.owner.setTrustNumber(trustNumber);
                    OwnerDAO.update(mDb);
                    mDb.close();

                    Intent firstPasswordActivity = new Intent(getApplicationContext(), FirstPasswordActivity.class);
                    startActivity(firstPasswordActivity);
                    finish();
                }
                else
                {
                    Toast.makeText(TrustNumberActivity.this, getResources().getString(R.string.errorTrustNumberInvalid), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (1):
                if (resultCode == Activity.RESULT_OK) {
                    String number = "";
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (phones.moveToNext()) {
                            number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            break;
                        }
                        phones.close();
                        if (number.trim().length() > 6)
                            textTrustNumber.setText(number);
                        else
                            Toast.makeText(TrustNumberActivity.this, getResources().getString(R.string.phoneNumberIncorrect), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}

