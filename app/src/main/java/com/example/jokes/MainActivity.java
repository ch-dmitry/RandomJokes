package com.example.jokes;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private TextView tvMainScreenSetup;
    private TextView tvMainScreenPunchline;
    private Button btnNextJoke;
    private Button btnAddJoke;
    private ProgressBar pbJokeLoading;

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        if (savedInstanceState == null) {
            mainViewModel.loadJoke();
        }

        mainViewModel.getJoke().observe(this, new Observer<Joke>() {
            @Override
            public void onChanged(Joke joke) {
                tvMainScreenSetup.setText(joke.getSetup());
                tvMainScreenPunchline.setText(joke.getPunchline());
            }
        });

        mainViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                if (loading) {
                    pbJokeLoading.setVisibility(View.VISIBLE);
                } else {
                    pbJokeLoading.setVisibility(View.GONE);
                }
            }
        });

        mainViewModel.getIsError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                if (isError) {
                    Toast.makeText(MainActivity.this, R.string.error_loading, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNextJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.loadJoke();
            }
        });

        btnAddJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.addJoke();
                Toast.makeText(MainActivity.this, R.string.joke_added, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        tvMainScreenSetup = findViewById(R.id.tvMainScreenSetup);
        tvMainScreenPunchline = findViewById(R.id.tvMainScreenPunchline);
        btnNextJoke = findViewById(R.id.btnNextJoke);
        btnAddJoke = findViewById(R.id.btnAddJoke);
        pbJokeLoading = findViewById(R.id.pbJokeLoading);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addedJokes) {
            Intent intent = AddedJokesActivity.newIntent(this);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("setup", mainViewModel.getJoke().getValue().getSetup());
        outState.putString("punchline", mainViewModel.getJoke().getValue().getPunchline());
    }
}