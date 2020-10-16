package com.jaime.addtracksspotifynowplaying.db.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Song_table")
public class Song {


    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "city")
    private String city;

    //Song = PERSON
    public Song(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }


    //public String getSong() {
    //    return this.name;
    //}


}