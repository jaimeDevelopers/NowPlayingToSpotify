package com.jaime.addtracksspotifynowplaying.db.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;


public class SongRepository {

    private SongDao mSongDao;
    private LiveData<List<Song>> mAllSongs;
    Song resultado;


    // Note that in order to unit test the SongRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public SongRepository(Application application) {
        SongRoomDatabase db = SongRoomDatabase.getDatabase(application);
        mSongDao = db.SongDao();
        mAllSongs = mSongDao.getAlphabetizedSongs();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Song>> getAllSongs() {
        return mAllSongs;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Song Song) {
        SongRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSongDao.insert(Song);
        });
    }

    public void deleteall() {
        SongRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSongDao.deleteAll();
        });
    }

    public Song getItemById(int itemId) {


        resultado = null;
        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    resultado = mSongDao.getItemById(itemId);
                } catch (Exception E) {
                    System.out.println("NULOOOOOOOO" + E.getMessage());
                }
            }
        };
        thread.start();
//
        try {
            thread.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return resultado;
        //return mSongDao.getItemById(itemId);
    }


}