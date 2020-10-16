package com.jaime.addtracksspotifynowplaying.db.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    @Update
    void updateSong(Song Song);

    @Query("SELECT * FROM Song_table WHERE id LIKE :itemId LIMIT 1")
    Song getItemById(int itemId);


}
