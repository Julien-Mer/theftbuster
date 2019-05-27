package com.theftbuster.theftbuster.Helpers.Memory.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.theftbuster.theftbuster.Helpers.Memory.Data.States;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien M on 23/01/2019.
 */
public class StatesDAO extends DAOBase {

    public static final String[] COLUMNS = { "name", "value"};
    public static final String[] COLUMNS_CREATION = { "TEXT PRIMARY KEY", "INTEGER" };
    public static final String NAME = "States";

    public static List<States> statesList = new ArrayList<>();

    public static void addState(SQLiteDatabase mDb, String name, int value) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("value", value);
        mDb.insert(NAME, null, values);
    }

    public static void getStates(SQLiteDatabase mDb) {
        statesList = new ArrayList<>();
        Cursor c = mDb.query(NAME, null, null, null, null, null, null);
        while (c.moveToNext()){
            States state = new States(c.getString(c.getColumnIndex("name")), c.getInt(c.getColumnIndex("value")));
            statesList.add(state);
        }
    }

    public static States selectState(String name) {
        for(States state : statesList) {
            if(state.getName().equals(name))
                return state;
        }
        return null;
    }

    public static void updateState(SQLiteDatabase mDb, States state) {
        ContentValues values = new ContentValues();
        values.put("value", state.getValue());
        String[] args = { state.getName() };
        mDb.update(NAME, values, "name=?", args);
    }

    public static void saveStates(SQLiteDatabase mDb) {
        for(States state : statesList) {
            ContentValues values = new ContentValues();
            values.put("value", state.getValue());
            String[] args = { state.getName() };
            mDb.update(NAME, values, "name=", args);
        }
    }

    public static void initialise(SQLiteDatabase mDb) {
        addState(mDb,"alarm", 0);
        addState(mDb,"lock", 0);
        addState(mDb,"photo", 0);
        addState(mDb,"attempts", 2);
        addState(mDb,"passwordFails", 0);
        addState(mDb,"settingsKiller", 0);
        addState(mDb,"debuggingKiller", 0);
        addState(mDb,"adminProtection", 0);
        addState(mDb,"alarmId", 1);
        addState(mDb,"waitingRio", 0);
    }

}
