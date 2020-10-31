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

    //public String getSong() {
    //    return this.name;
    //}


}