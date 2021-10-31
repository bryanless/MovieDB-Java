package com.ss.moviedb.retrofit;

import com.ss.moviedb.model.Genre;
import com.ss.moviedb.model.Movies;
import com.ss.moviedb.model.NowPlaying;
import com.ss.moviedb.model.Popular;
import com.ss.moviedb.model.TopRated;
import com.ss.moviedb.model.Trending;
import com.ss.moviedb.model.Upcoming;
import com.ss.moviedb.model.Videos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndPoint {

    //==Start of movie API
    @GET("movie/{movie_id}")
    Call<Movies> getMovieById(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey
    );

    @GET("movie/now_playing")
    Call<NowPlaying> getNowPlaying(
            @Query("page") String page,
            @Query("api_key") String apiKey
    );

    @GET("movie/upcoming")
    Call<Upcoming> getUpcoming(
            @Query("page") String page,
            @Query("api_key") String apiKey
    );

    @GET("movie/popular")
    Call<Popular> getPopular(
            @Query("api_key") String apiKey
    );

    @GET("movie/top_rated")
    Call<TopRated> getTopRated(
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/videos")
    Call<Videos> getVideo(
            @Path("movie_id") String movieId,
            @Query("api_key") String apiKey
    );
    //==End of movie API

    //==Start of genre API
    @GET("genre/movie/list")
    Call<Genre> getGenreMovie(
            @Query("api_key") String apiKey
    );
    //==End of genre API

    //==Start of trending API
    @GET("trending/{media_type}/{time_window}")
    Call<Trending> getTrending(
            @Path("media_type") String mediaType,
            @Path("time_window") String timeWindow,
            @Query("api_key") String apiKey
    );
    //==End of trending API
}
