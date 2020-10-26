package com.jaime.addtracksspotifynowplaying.db.database;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;


@Dao
public interface SongDao {

    // allowing the insert of the same Song multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Song Song);

    @Query("DELETE FROM Song_table")
    void deleteAll();

    @Query("SELECT * from Song_table")
    LiveData<List<Song>> getAlphabetizedSongs();


    @Query("UPDATE Song_table SET nowPlayingSong=:nowPlayingSong, infoSearch=:infoSearch WHERE streamingSong=:streamingSong")
    void updateSong(String nowPlayingSong, String streamingSong, String infoSearch);

    @Query("SELECT * FROM Song_table WHERE id LIKE :itemId LIMIT 1")
    Song getItemById(int itemId);

    @Query("SELECT * FROM Song_table WHERE nowPlayingSong LIKE :nowPlayingSong LIMIT 1")
    Song getItemnowPlayingSong(String nowPlayingSong);

    @Query("SELECT * FROM Song_table WHERE streamingSong LIKE :streamingSong LIMIT 1")
    Song getItemstreamingSong(String streamingSong);


    @Query("SELECT * from Song_table")
    DataSource.Factory<Integer, Song> getFewWords();

}
