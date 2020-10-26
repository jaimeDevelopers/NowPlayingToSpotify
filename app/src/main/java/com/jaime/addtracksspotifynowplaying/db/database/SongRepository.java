package com.jaime.addtracksspotifynowplaying.db.database;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.List;


public class SongRepository {

    private SongDao mSongDao;
    private LiveData<List<Song>> mAllSongs;
    Song resultado;
    SongRoomDatabase db;

    //public final LiveData<PagedList<Song>> songPaging;
    private final DataSource.Factory<Integer, Song> mFewWords;

    // Note that in order to unit test the SongRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public SongRepository(Application application) {
        db = SongRoomDatabase.getDatabase(application);
        mSongDao = db.SongDao();
        //mAllSongs = mSongDao.getAlphabetizedSongs();

        mFewWords = mSongDao.getFewWords();

        //songPaging = new LivePagedListBuilder<>(
        //        mSongDao.getAllPaging(), 50).build();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Song>> getAllSongs() {
        return mAllSongs;
    }

    // public LiveData<PagedList<Song>> getallusingPaging() {
    //    return songPaging;
    //}

    public DataSource.Factory<Integer, Song> getFewWords() {
        return mFewWords;
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
            db.clearAllTables();

        });
    }

    public void updateSong(String nowPlayingSong, String streamingSong, String infoSearch) {
        SongRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSongDao.updateSong(nowPlayingSong, streamingSong, infoSearch);

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
                    E.printStackTrace();
                }
            }
        };
        thread.start();
//
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
        //return mSongDao.getItemById(itemId);
    }

    public Song getItemnowPlayingSong(String nowPlayingSong) {

        resultado = null;
        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    resultado = mSongDao.getItemnowPlayingSong(nowPlayingSong);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
//
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
        //return mSongDao.getItemById(itemId);
    }

    public Song getItemstreamingSong(String streamingSong) {

        resultado = null;
        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    resultado = mSongDao.getItemstreamingSong(streamingSong);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
//
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
        //return mSongDao.getItemById(itemId);
    }


}