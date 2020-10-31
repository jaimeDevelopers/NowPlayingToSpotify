package com.jaime.addtracksspotifynowplaying.db.database;

import android.text.TextUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.jaime.addtracksspotifynowplaying.MyValues;

import java.util.ArrayList;


@Entity(tableName = MyValues.TABLE_NAME)
public class Song {


    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = MyValues.NOW_PLAYING_SONG)
    private String nowPlayingSong;


    @ColumnInfo(name = MyValues.STREAMING_NAME)
    private String streamingName;


    @ColumnInfo(name = MyValues.DATE)
    private String date;

    @ColumnInfo(name = MyValues.INFO_SEARCH)
    private String infoSearch;


    //Song = PERSON
    public Song(String nowPlayingSong, String streamingName, String date, String infoSearch) {
        this.nowPlayingSong = nowPlayingSong;
        this.streamingName = streamingName;
        this.date = date;
        this.infoSearch = infoSearch;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNowPlayingSong() {
        return nowPlayingSong;
    }

    public String getStreamingName() {
        return streamingName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfoSearch() {
        return infoSearch;
    }

    public void setInfoSearch(String infoSearch) {
        this.infoSearch = infoSearch;
    }


    public static void upgradeWordTable(SupportSQLiteDatabase database) {

        ArrayList<String> columns = DButils.getColumns(database, MyValues.TABLE_NAME);

        // 1. Create new table
        final String TABLE_NAME_TEMP = "_temp" + MyValues.TABLE_NAME;
        database.execSQL("CREATE TABLE IF NOT EXISTS `" + TABLE_NAME_TEMP + "` (`" + Song + "` TEXT NOT NULL, `" + DBConstants.KEY_RANDOM_NUMBER + "` TEXT, PRIMARY KEY(`" + DBConstants.KEY_WORD + "`))");

        // 2. find common columns.
        columns.retainAll(DButils.getColumns(database, TABLE_NAME_TEMP));
        final String COLS = TextUtils.join(",", columns);

        // 3. Copy the data
        database.execSQL("INSERT INTO " + TABLE_NAME_TEMP + " (" + COLS + ") "
                + "SELECT " + COLS + " "
                + "FROM " + DBConstants.TABLE_WORD);

        // 4. Remove the old table
        database.execSQL("DROP TABLE " + DBConstants.TABLE_WORD);

        // 5. Change the table name to the correct one
        database.execSQL("ALTER TABLE " + TABLE_NAME_TEMP + " RENAME TO " + DBConstants.TABLE_WORD);
    }


    //public String getSong() {
    //    return this.name;
    //}


}