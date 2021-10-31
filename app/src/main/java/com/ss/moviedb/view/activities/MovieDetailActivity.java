package com.ss.moviedb.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.ss.moviedb.R;
import com.ss.moviedb.adapter.MovieDetailGenreAdapter;
import com.ss.moviedb.helper.Const;
import com.ss.moviedb.model.Movies;
import com.ss.moviedb.viewmodel.MovieViewModel;

import java.util.ArrayList;

public class MovieDetailActivity extends AppCompatActivity {

    private Toolbar movieDetail_toolbar;
    private ImageView movieDetail_imageView_poster;
    private TextView movieDetail_textView_caption, movieDetail_textView_title, movieDetail_textView_rating, movieDetail_textView_synopsis;
    private RecyclerView movieDetail_recyclerView_genre;

    private MovieViewModel viewModel;

    private String movieId;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initialize();
        setRecyclerView();
    }

    private Observer<Movies> showResultMovieDetail = new Observer<Movies>() {
        @Override
        public void onChanged(Movies movies) {
            if (movies != null) {
                String year = movies.getRelease_date().substring(0, 4);
                int runtime = movies.getRuntime();
                String title = movies.getTitle();
                String rating = String.valueOf(movies.getVote_average());
                ArrayList<Movies.Genres> genreList = (ArrayList<Movies.Genres>) movies.getGenres();
                String synopsis = movies.getOverview();

                String hour = String.valueOf(runtime / 60);
                String minute = String.valueOf(runtime % 60);

                String caption = getString(R.string.text_movieCaption, year, hour, minute);

                movieDetail_textView_caption.setText(caption);
                movieDetail_textView_title.setText(title);
                movieDetail_textView_rating.setText(rating);
                movieDetail_textView_synopsis.setText(synopsis);
                Glide.with(MovieDetailActivity.this).load(Const.IMG_URL_500 + movies.getPoster_path()).into(movieDetail_imageView_poster);

                MovieDetailGenreAdapter adapter = new MovieDetailGenreAdapter(MovieDetailActivity.this);
                adapter.setMovieDetailGenreList(genreList);
                movieDetail_recyclerView_genre.setAdapter(adapter);
            }
        }
    };

    private void setRecyclerView() {
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP);
        manager.setJustifyContent(JustifyContent.FLEX_START);
        movieDetail_recyclerView_genre.setLayoutManager(manager);
    }

    private void initialize() {
        Intent intent = getIntent();
        movieId = intent.getStringExtra("movieId");

        movieDetail_toolbar = findViewById(R.id.movieDetail_toolbar);
        movieDetail_imageView_poster = findViewById(R.id.movieDetail_imageView_poster);
        movieDetail_textView_caption = findViewById(R.id.movieDetail_textView_caption);
        movieDetail_textView_title = findViewById(R.id.movieDetail_textView_title);
        movieDetail_textView_rating = findViewById(R.id.movieDetail_textView_rating);
        movieDetail_textView_synopsis = findViewById(R.id.movieDetail_textView_synopsis);
        movieDetail_recyclerView_genre = findViewById(R.id.movieDetail_recyclerView_genre);

        setSupportActionBar(movieDetail_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(MovieDetailActivity.this).get(MovieViewModel.class);
        viewModel.getMovieById(movieId);
        viewModel.getResultGetMovieById().observe(MovieDetailActivity.this, showResultMovieDetail);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}