package com.jaime.addtracksspotifynowplaying.db.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Song_table")
public class Song {


    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = "nowPlayingSong")
    private String nowPlayingSong;

    @ColumnInfo(name = "streamingSong")
    private String streamingSong;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "infoSearch")
    private String infoSearch;


    //Song = PERSON
    public Song(String nowPlayingSong, String streamingSong, String date, String infoSearch) {
        this.nowPlayingSong = nowPlayingSong;
        this.streamingSong = streamingSong;
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

    public String getStreamingSong() {
        return streamingSong;
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