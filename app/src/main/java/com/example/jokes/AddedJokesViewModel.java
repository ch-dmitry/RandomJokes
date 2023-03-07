package com.example.jokes;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddedJokesViewModel extends AndroidViewModel {
    private static final String LOG_TAG = "AddedJokesViewModel";
    private AddedJokesDatabase addedJokesDatabase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public AddedJokesViewModel(@NonNull Application application) {
        super(application);
        addedJokesDatabase = AddedJokesDatabase.getInstance(getApplication());
    }

    public void remove(Joke joke) {
        Disposable disposable = addedJokesDatabase.jokesDoa().remove(joke.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        addedJokesDatabase.jokesDoa().remove(joke.getId());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(LOG_TAG, "Error: " + throwable.getMessage());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public LiveData<List<Joke>> getJokes() {
        return addedJokesDatabase.jokesDoa().getJokes();
    }
}