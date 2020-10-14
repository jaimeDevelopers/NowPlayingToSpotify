package com.jaime.addtracksspotifynowplaying.db.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("SELECT * from word_table")
    LiveData<List<Word>> getAlphabetizedWords();

    @Update
    void updateWord(Word word);

    @Query("SELECT * FROM word_table WHERE id LIKE :itemId LIMIT 1")
    LiveData<Word> getItemById(int itemId);


}
