package com.jaime.addtracksspotifynowplaying.db.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")
public class Word {


    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "city")
    private String city;

    //WORD = PERSON
    public Word(int id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
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


    //public String getWord() {
    //    return this.name;
    //}


}