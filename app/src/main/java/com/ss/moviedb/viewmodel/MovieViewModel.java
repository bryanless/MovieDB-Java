package com.ss.moviedb.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ss.moviedb.model.Movies;
import com.ss.moviedb.model.NowPlaying;
import com.ss.moviedb.repositories.MovieRepository;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository repository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = MovieRepository.getInstance();
    }

    //==Start of view model get movie by id
    private MutableLiveData<Movies> resultGetMovieById = new MutableLiveData<>();
    public void getMovieById(String movieId) {
        resultGetMovieById = repository.getMovieData(movieId);
    }
    public LiveData<Movies> getResultGetMovieById() {
        return resultGetMovieById;
    }
    //==End of view model get movie by id

    //==Start of view model get now playing
    private MutableLiveData<NowPlaying> resultGetNowPlaying = new MutableLiveData<>();
    public void getNowPlaying() {
        resultGetNowPlaying = repository.getNowPlayingData();
    }
    public LiveData<NowPlaying> getResultGetNowPlaying() {
        return resultGetNowPlaying;
    }
    //==End of view model get now playing
}
