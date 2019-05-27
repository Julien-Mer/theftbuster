package com.theftbuster.theftbuster.Helpers.Memory.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theftbuster.theftbuster.Helpers.Memory.Data.Code;
import com.theftbuster.theftbuster.Helpers.Memory.Data.States;

/**
 * Created by Julien M on 18/01/2019.
 */
public class CodeDAO extends DAOBase {

    public static final String[] COLUMNS = { "code", "salt"};
    public static final String[] COLUMNS_CREATION = { "TEXT", "TEXT" };
    public static final String NAME = "Code";

    public static Code code;

    public static void getCode(SQLiteDatabase mDb) {
        Cursor c = mDb.query(NAME, null, null, null, null, null, null);
        if(c.moveToNext()){
            code = new Code(c.getString(c.getColumnIndex("code")), c.getString(c.getColumnIndex("salt")));
        }
    }


    public static void addCode(SQLiteDatabase mDb) {
        ContentValues values = new ContentValues();
        values.put("code", code.getCode());
        values.put("salt", code.getSalt());
        mDb.insert(NAME, null, values);
    }

    public static void update(SQLiteDatabase mDb) {
        ContentValues values = new ContentValues();
        values.put("salt", code.getSalt());
        values.put("code", code.getCode());
        mDb.update(NAME, values, null, null);
    }

}