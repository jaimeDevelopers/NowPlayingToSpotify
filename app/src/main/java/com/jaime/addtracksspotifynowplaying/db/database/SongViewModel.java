package com.jaime.addtracksspotifynowplaying.db.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;


public class SongViewModel extends AndroidViewModel {

    private SongRepository mRepository;

    public LiveData<PagedList<Song>> mFewWords;


    public SongViewModel(Application application) {
        super(application);
        mRepository = new SongRepository(application);

        DataSource.Factory<Integer, Song> fewWords = mRepository.getFewWords();

        mFewWords = new LivePagedListBuilder<>(
                fewWords, /* page size */ 1).build();

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