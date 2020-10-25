package com.jaime.addtracksspotifynowplaying.db.database;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;


public class SongViewModel extends AndroidViewModel {

    private SongRepository mRepository;

    private LiveData<List<Song>> mAllSongs;

    public final LiveData<PagedList<Song>> songPaging;


    public SongViewModel(Application application) {
        super(application);
        mRepository = new SongRepository(application);
        mAllSongs = mRepository.getAllSongs();


        songPaging = mRepository.getallusingPaging();

    }

    public LiveData<List<Song>> getAllSongs() {
        return mAllSongs;
    }

    public LiveData<PagedList<Song>> getAllSongsPaging() {
        return songPaging;
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

    public Song getItemnowPlayingSong(String nowPlayingSong) {
        return mRepository.getItemnowPlayingSong(nowPlayingSong);
    }

}