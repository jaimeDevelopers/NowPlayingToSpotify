package com.jaime.addtracksspotifynowplaying.db.database;

import android.database.Cursor;

import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class DButils {
    public static ArrayList<String> getColumns(SupportSQLiteDatabase db, String tableName) {
        ArrayList<String> al = null;
        Cursor c = null;
        try {
            c = db.query("SELECT * FROM " + tableName + " LIMIT 1", null);
            if (c != null) {
                al = new ArrayList<>(Arrays.asList(c.getColumnNames()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return al;
    }
}
