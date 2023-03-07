package com.example.jokes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
interface JokesDao {

    @Query("SELECT * FROM added_jokes")
    LiveData<List<Joke>> getJokes();

    @Insert
    Completable add(Joke joke);

    @Query("DELETE FROM added_jokes WHERE id = :jokeId")
    Completable remove(int jokeId);
}
