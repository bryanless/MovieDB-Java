package com.ss.moviedb.repositories;

import androidx.lifecycle.MutableLiveData;

import com.ss.moviedb.helper.Const;
import com.ss.moviedb.model.Genre;
import com.ss.moviedb.retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreRepository {
    private static GenreRepository repository;

    private GenreRepository() {

    }

    public static GenreRepository getInstance() {
        if (repository == null) {
            repository = new GenreRepository();
        }

        return repository;
    }

    public MutableLiveData<Genre> getGenreMovieData() {
        final MutableLiveData<Genre> result = new MutableLiveData<>();

        ApiService.endPoint().getGenreMovie(Const.API_KEY).enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(Call<Genre> call, Response<Genre> response) {
                result.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Genre> call, Throwable t) {

            }
        });

        return result;
    }
}
