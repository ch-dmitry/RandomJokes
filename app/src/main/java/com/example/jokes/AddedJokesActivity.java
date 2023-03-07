package com.example.jokes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddedJokesActivity extends AppCompatActivity {
    private JokesAdapter jokesAdapter;
    private AddedJokesViewModel addedJokesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_jokes);

        RecyclerView rvAddedJokes = findViewById(R.id.rvAddedJokes);

        jokesAdapter = new JokesAdapter();
        rvAddedJokes.setAdapter(jokesAdapter);

        addedJokesViewModel = new ViewModelProvider(this).get(AddedJokesViewModel.class);
        addedJokesViewModel.getJokes().observe(this, new Observer<List<Joke>>() {
            @Override
            public void onChanged(List<Joke> jokes) {
                 jokesAdapter.setJokes(jokes);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int viewPosition = viewHolder.getAdapterPosition();
                Joke joke = jokesAdapter.getJokes().get(viewPosition);
                addedJokesViewModel.remove(joke);
            }
        });

        itemTouchHelper.attachToRecyclerView(rvAddedJokes);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddedJokesActivity.class);
    }
}