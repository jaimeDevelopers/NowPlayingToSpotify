package com.jaime.addtracksspotifynowplaying.db.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class SongViewModel extends AndroidViewModel {

    private SongRepository mRepository;

    private LiveData<List<Song>> mAllSongs;

    public SongViewModel(Application application) {
        super(application);
        mRepository = new SongRepository(application);
        mAllSongs = mRepository.getAllSongs();
    }

    public LiveData<List<Song>> getAllSongs() {
        return mAllSongs;
    }

    public void insert(Song Song) {
        mRepository.insert(Song);
    }

    public void deleteall() {
        mRepository.deleteall();
    }

    public Song getItemById(int id) {
        return mRepository.getItemById(id);
    }


}