package com.ss.moviedb.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ss.moviedb.model.Movies;
import com.ss.moviedb.model.Trending;
import com.ss.moviedb.repositories.TrendingRepository;

public class TrendingViewModel extends AndroidViewModel {

    private TrendingRepository repository;

    public TrendingViewModel(@NonNull Application application) {
        super(application);
        repository = TrendingRepository.getInstance();
    }

    //==Start of view model get trending
    private MutableLiveData<Trending> resultGetTrending = new MutableLiveData<>();
    public void getTrending(String mediaType, String timeWindow) {
        resultGetTrending = repository.getTrendingData(mediaType, timeWindow);
    }
    public LiveData<Trending> getResultGetTrending() {
        return resultGetTrending;
    }
    //==End of view model get trending
}
