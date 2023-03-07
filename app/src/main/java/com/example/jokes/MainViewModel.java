package com.example.jokes;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
    private static final String LOG_TAG = "MainViewModel";
    private MutableLiveData<Joke> joke = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isError = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private AddedJokesDatabase addedJokesDatabase = AddedJokesDatabase.getInstance(getApplication());


    public LiveData<Joke> getJoke() {
        return joke;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsError() {
        return isError;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadJoke() {
        Disposable disposable = ApiFactory.getApiService().loadJoke()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isError.setValue(false);
                        isLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .subscribe(new Consumer<Joke>() {
                    @Override
                    public void accept(Joke jokeText) throws Throwable {
                        joke.setValue(jokeText);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        isError.setValue(true);
                        Log.d(LOG_TAG, "Error: " + throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void addJoke() {
        String setup = joke.getValue().getSetup();
        String punchline = joke.getValue().getPunchline();
        Joke addedJoke = new Joke(joke.getValue().getType(), setup, punchline, joke.getValue().getId());

        Disposable disposable = addedJokesDatabase.jokesDoa().add(addedJoke)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        addedJokesDatabase.jokesDoa().add(addedJoke);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(LOG_TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}