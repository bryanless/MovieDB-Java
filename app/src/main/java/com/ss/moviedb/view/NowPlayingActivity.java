package com.ss.moviedb.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.ss.moviedb.R;
import com.ss.moviedb.adapter.NowPlayingAdapter;
import com.ss.moviedb.model.NowPlaying;
import com.ss.moviedb.viewmodel.MovieViewModel;

import java.util.ArrayList;

public class NowPlayingActivity extends AppCompatActivity {

    private Toolbar nowPlaying_toolbar;
    private RecyclerView nowPlaying_recyclerView;

    private NowPlayingAdapter adapter;
    private MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        initialize();
        setRecyclerView();
//        When main activity is ready
//        setListener();
    }

    private Observer<NowPlaying> showResultNowPlaying = new Observer<NowPlaying>() {
        @Override
        public void onChanged(NowPlaying nowPlaying) {
            NowPlayingAdapter adapter = new NowPlayingAdapter(NowPlayingActivity.this);
            adapter.setNowPlayingList((ArrayList<NowPlaying.Results>) nowPlaying.getResults());
            nowPlaying_recyclerView.setAdapter(adapter);
        }
    };

    private void setListener() {
        nowPlaying_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Should be back to main activity
            }
        });
    }

    private void setRecyclerView() {
//        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext());
        FlexboxLayoutManager manager = new FlexboxLayoutManager(this, FlexDirection.ROW, FlexWrap.WRAP);
        manager.setJustifyContent(JustifyContent.CENTER);
        nowPlaying_recyclerView.setLayoutManager(manager);
    }

    private void initialize() {
        nowPlaying_toolbar = findViewById(R.id.nowPlaying_toolbar);
        nowPlaying_recyclerView = findViewById(R.id.nowPlaying_recyclerView);

//        When main activity is ready
//        setSupportActionBar(nowPlaying_toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = new ViewModelProvider(NowPlayingActivity.this).get(MovieViewModel.class);
        viewModel.getNowPlaying();
        viewModel.getResultGetNowPlaying().observe(NowPlayingActivity.this, showResultNowPlaying);
    }
}