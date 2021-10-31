package com.ss.moviedb.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ss.moviedb.model.Genre;
import com.ss.moviedb.repositories.GenreRepository;

public class GenreViewModel extends AndroidViewModel {

    private GenreRepository repository;

    public GenreViewModel(@NonNull Application application) {
        super(application);
        repository = GenreRepository.getInstance();
    }

    //==Start of view model get genre movie
    private MutableLiveData<Genre> resultGetGenreMovie = new MutableLiveData<>();
    public void getGenreMovie() {
        resultGetGenreMovie = repository.getGenreMovieData();
    }
    public LiveData<Genre> getResultGetGenreMovie() {
        return resultGetGenreMovie;
    }
    //==End of view model get genre movie
}
