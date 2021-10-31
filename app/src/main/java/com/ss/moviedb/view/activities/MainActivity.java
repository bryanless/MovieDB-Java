package com.ss.moviedb.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.ss.moviedb.R;
import com.ss.moviedb.helper.Const;
import com.ss.moviedb.model.Movies;
import com.ss.moviedb.viewmodel.MovieViewModel;

public class MainActivity extends AppCompatActivity {

    private Button main_button_hit;
    private TextView main_textView_title;
    private TextInputLayout main_TIL_id;
    private ImageView main_imageView_poster;

    private MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        setListener();
    }

    private boolean validateMovieId(String movieId) {
        if (movieId.isEmpty()) {
            main_TIL_id.setError("Please fill a movie ID");
        } else {
            main_TIL_id.setError(null);
        }

        return !movieId.isEmpty();
    }

    private Observer<Movies> showResultMovie = new Observer<Movies>() {
        @Override
        public void onChanged(Movies movies) {
            if (movies == null) {
                main_textView_title.setText("Movie Not Found");
            } else {
                String title = movies.getTitle();
                String posterPath = movies.getPoster_path().toString();
                main_textView_title.setText(title);
                main_textView_title.setVisibility(View.VISIBLE);
                Glide.with(MainActivity.this).load(Const.IMG_URL_500 + posterPath).into(main_imageView_poster);
            }
        }
    };

    private void setListener() {
        main_button_hit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movieId = main_TIL_id.getEditText().getText().toString().trim();

                if (!validateMovieId(movieId)) {
                    return;
                }

                viewModel.getMovieById(movieId);
                viewModel.getResultGetMovieById().observe(MainActivity.this, showResultMovie);
            }
        });
    }

    private void initialize() {
        main_button_hit = findViewById(R.id.main_button_hit);
        main_textView_title = findViewById(R.id.main_textView_title);
        main_TIL_id = findViewById(R.id.main_TIL_id);
        main_imageView_poster = findViewById(R.id.main_imageView_poster);

        main_textView_title.setVisibility(View.GONE);

        viewModel = new ViewModelProvider(MainActivity.this).get(MovieViewModel.class);
    }
}