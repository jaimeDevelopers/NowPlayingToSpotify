package com.jaime.addtracksspotifynowplaying.db.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Song.class}, version = 1, exportSchema = false)
public abstract class SongRoomDatabase extends RoomDatabase {

    public abstract SongDao SongDao();

    private static volatile SongRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static SongRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SongRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SongRoomDatabase.class, "Song_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }



}