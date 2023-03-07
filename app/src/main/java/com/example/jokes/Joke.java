package com.example.jokes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "added_jokes")
public class Joke {
    private String type;
    private String setup;
    private String punchline;
    @PrimaryKey
    private int id;

    public Joke(String type, String setup, String punchline, int id) {
        this.type = type;
        this.setup = setup;
        this.punchline = punchline;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getSetup() {
        return setup;
    }

    public String getPunchline() {
        return punchline;
    }

    public int getId() {
        return id;
    }
}
