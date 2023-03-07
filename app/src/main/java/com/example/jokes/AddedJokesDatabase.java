package com.example.jokes;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Joke.class}, version = 1, exportSchema = false)
public abstract class AddedJokesDatabase extends RoomDatabase {
    private static AddedJokesDatabase instance = null;
    private static final String DB_NAME = "added_jokes.db";

    public static AddedJokesDatabase getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    application,
                    AddedJokesDatabase.class,
                    DB_NAME
            ).build();
        }

        return instance;
    }

    public abstract JokesDao jokesDoa();
}
