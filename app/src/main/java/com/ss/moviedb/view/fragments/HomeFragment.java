package com.ss.moviedb.view.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.ss.moviedb.R;
import com.ss.moviedb.adapter.GenreAdapter;
import com.ss.moviedb.adapter.MovieDetailGenreAdapter;
import com.ss.moviedb.adapter.MovieDetailProductionCompanyAdapter;
import com.ss.moviedb.adapter.PopularAdapter;
import com.ss.moviedb.adapter.TopRatedAdapter;
import com.ss.moviedb.adapter.TrendingVPAdapter;
import com.ss.moviedb.adapter.UpcomingAdapter;
import com.ss.moviedb.helper.Const;
import com.ss.moviedb.model.Genre;
import com.ss.moviedb.model.Movies;
import com.ss.moviedb.model.Popular;
import com.ss.moviedb.model.TopRated;
import com.ss.moviedb.model.Trending;
import com.ss.moviedb.model.Upcoming;
import com.ss.moviedb.viewmodel.GenreViewModel;
import com.ss.moviedb.viewmodel.MovieViewModel;
import com.ss.moviedb.viewmodel.TrendingViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private View view;
    private ViewPager2 home_viewPager_trending_fragment;
    private RecyclerView home_recyclerView_genre_fragment, home_recyclerView_popular_fragment, home_recyclerView_topRated_fragment;
    private Dialog progressBarDialog;

    private TrendingVPAdapter trendingAdapter;
    private TrendingViewModel trendingViewModel;
    private GenreAdapter genreAdapter;
    private GenreViewModel genreViewModel;
    private PopularAdapter popularAdapter;
    private TopRatedAdapter topRatedAdapter;
    private MovieViewModel movieViewModel;

    private Handler handler;

    private int delay, page, totalPage;
    private String mediaType, timeWindow;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        setProgressBarDialog();
        initialize();
        setViewPager();
        setRecyclerView();

        return view;
    }

    // * Exit fragment
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    // * Back on fragment
    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay * 1000);
    }

    private void setRecyclerView() {
        //==Start of genre rv
        RecyclerView.LayoutManager managerGenre = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        home_recyclerView_genre_fragment.setLayoutManager(managerGenre);
        //==End of genre rv

        //==Start of popular rv
        RecyclerView.LayoutManager managerPopular = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        home_recyclerView_popular_fragment.setLayoutManager(managerPopular);
        //==End of popular rv

        //==Start of top rated rv
        RecyclerView.LayoutManager managerTopRated = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        home_recyclerView_topRated_fragment.setLayoutManager(managerTopRated);
        //==End of top rated rv
    }

    private void setViewPager() {
        home_viewPager_trending_fragment.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, delay * 1000);
            }
        });

    }

    // * What happens when the handler countdown done
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (home_viewPager_trending_fragment.getCurrentItem() == home_viewPager_trending_fragment.getAdapter().getItemCount() - 1) {
                home_viewPager_trending_fragment.setCurrentItem(0);
            } else {
                home_viewPager_trending_fragment.setCurrentItem(home_viewPager_trending_fragment.getCurrentItem() + 1);
            }
        }
    };

    private void initialize() {
        delay = 3;
        mediaType = "movie";
        timeWindow = "week";

        handler = new Handler();

        home_viewPager_trending_fragment = view.findViewById(R.id.home_viewPager_trending_fragment);
        home_recyclerView_genre_fragment = view.findViewById(R.id.home_recyclerView_genre_fragment);
        home_recyclerView_popular_fragment = view.findViewById(R.id.home_recyclerView_popular_fragment);
        home_recyclerView_topRated_fragment = view.findViewById(R.id.home_recyclerView_topRated_fragment);

        //==Start of view model trending movie
        trendingViewModel = new ViewModelProvider(getActivity()).get(TrendingViewModel.class);
        trendingViewModel.getTrending(mediaType, timeWindow);
        trendingViewModel.getResultGetTrending().observe(getActivity(), showResultTrending);
        //==End of view model trending movie

        //==Start of view model genre movie
        genreViewModel = new ViewModelProvider(getActivity()).get(GenreViewModel.class);
        genreViewModel.getGenreMovie();
        genreViewModel.getResultGetGenreMovie().observe(getActivity(), showResultGenreMovie);
        //==End of view model genre movie

        //==Start of view model popular movie
        movieViewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        movieViewModel.getPopular();
        movieViewModel.getResultGetPopular().observe(getActivity(), showResultPopular);
        //==End of view model popular movie

        //==Start of view model top rated movie
        movieViewModel.getTopRated();
        movieViewModel.getResultGetTopRated().observe(getActivity(), showResultTopRated);
        //==End of view model top rated movie
    }

    private void setProgressBarDialog() {
        progressBarDialog = new Dialog(getActivity());
        progressBarDialog.setContentView(R.layout.circular_progress_bar);
        progressBarDialog.setCancelable(false);
        progressBarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        progressBarDialog.show();
    }

    // TODO: Check network connection
//    public boolean isInternetConnected() {
//        try {
//            String command = "ping -c 1 google.com";
//            return (Runtime.getRuntime().exec(command).waitFor() == 0);
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private boolean isNetworkConnected() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(getActivity().CONNECTIVITY_SERVICE);
//        return connectivityManager.getActiveNetworkInfo() != null;
//    }

    private Observer<Trending> showResultTrending = new Observer<Trending>() {
        @Override
        public void onChanged(Trending results) {
            trendingAdapter = new TrendingVPAdapter(getActivity());
            trendingAdapter.setTrendingList((ArrayList<Trending.Results>) results.getResults());
            home_viewPager_trending_fragment.setAdapter(trendingAdapter);
        }
    };

    private Observer<Genre> showResultGenreMovie = new Observer<Genre>() {
        @Override
        public void onChanged(Genre results) {
            genreAdapter = new GenreAdapter(getActivity());
            genreAdapter.setGenreList((ArrayList<Genre.Genres>) results.getGenres());
            home_recyclerView_genre_fragment.setAdapter(genreAdapter);
        }
    };

    private Observer<Popular> showResultPopular = new Observer<Popular>() {
        @Override
        public void onChanged(Popular popular) {
            popularAdapter = new PopularAdapter(getActivity());
            popularAdapter.setPopularList((ArrayList<Popular.Results>) popular.getResults());
            home_recyclerView_popular_fragment.setAdapter(popularAdapter);
        }
    };

    private Observer<TopRated> showResultTopRated = new Observer<TopRated>() {
        @Override
        public void onChanged(TopRated topRated) {
            topRatedAdapter = new TopRatedAdapter(getActivity());
            topRatedAdapter.setTopRatedList((ArrayList<TopRated.Results>) topRated.getResults());
            home_recyclerView_topRated_fragment.setAdapter(topRatedAdapter);

            progressBarDialog.hide();
        }
    };
}