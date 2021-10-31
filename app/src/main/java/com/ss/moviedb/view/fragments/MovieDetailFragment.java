package com.ss.moviedb.view.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.snackbar.Snackbar;
import com.ss.moviedb.R;
import com.ss.moviedb.adapter.MovieDetailGenreAdapter;
import com.ss.moviedb.adapter.MovieDetailProductionCompanyAdapter;
import com.ss.moviedb.helper.Const;
import com.ss.moviedb.model.Genre;
import com.ss.moviedb.model.Movies;
import com.ss.moviedb.model.Videos;
import com.ss.moviedb.viewmodel.MovieViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {

    private View view;
    private ImageView movieDetail_imageView_backdrop_fragment, movieDetail_imageView_poster_fragment;
    private TextView movieDetail_textView_titlePlaceholder_fragment, movieDetail_textView_caption_fragment, movieDetail_textView_title_fragment, movieDetail_textView_rating_fragment, movieDetail_textView_popularity_fragment, movieDetail_textView_productionCompanyStatic_fragment, movieDetail_textView_description_fragment;
    private Button movieDetail_button_watchTrailer_fragment;
    private RecyclerView movieDetail_recyclerView_productionCompany_fragment, movieDetail_recyclerView_genre_fragment;
    private Dialog progressBarDialog;

    private MovieViewModel viewModel;

    private String movieId, videoSite, videoKey;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailFragment newInstance(String param1, String param2) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailFragment() {
        // Required empty public constructor
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
        view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        initialize();
        setRecyclerView();
        setListener();

        return view;
    }

    private void setListener() {
        movieDetail_button_watchTrailer_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ? YouTube
                if (TextUtils.equals(videoSite, "YouTube")) {
                    // * Set intent to launch YouTube
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(Const.YOUTUBE_WATCH_URL + videoKey));

                    // * Check if YouTube installed
                    PackageManager manager = getActivity().getPackageManager();
                    ArrayList<ResolveInfo> info = (ArrayList<ResolveInfo>) manager.queryIntentActivities(intent, 0);
                    if (info.size() > 0) {
                        // * YouTube installed
                        intent.setPackage("com.google.android.youtube");
                    } else {
                        // * Youtube not installed
                    }

                    // * Launch YouTube
                    startActivity(intent);
                } else {
                    // * Make snackbar
                    Snackbar snackbar = Snackbar.make(view, getString(R.string.text_videoFailed), Snackbar.LENGTH_SHORT);
                    snackbar.setAnchorView(getActivity().findViewById(R.id.main_bottomNavView));

                    // * Set action
                    snackbar.setAction(getString(R.string.text_dismiss), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });

                    snackbar.show();
                }
            }
        });
    }

    private void setRecyclerView() {
        //==Start of genre RV
        FlexboxLayoutManager managerGenre = new FlexboxLayoutManager(getActivity(), FlexDirection.ROW, FlexWrap.WRAP);
        managerGenre.setJustifyContent(JustifyContent.FLEX_START);
        movieDetail_recyclerView_genre_fragment.setLayoutManager(managerGenre);
        //==End of genre RV

        //==Start of production company RV
        RecyclerView.LayoutManager managerProductionCompany = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        movieDetail_recyclerView_productionCompany_fragment.setLayoutManager(managerProductionCompany);
        //==End of production company RV
    }

    private void initialize() {
        videoSite = "";
        videoKey = "";

        movieId = getArguments().getString("movieId");

        setProgressBarDialog();

        movieDetail_imageView_backdrop_fragment = view.findViewById(R.id.movieDetail_imageView_backdrop_fragment);
        movieDetail_imageView_poster_fragment = view.findViewById(R.id.movieDetail_imageView_poster_fragment);
        movieDetail_textView_titlePlaceholder_fragment = view.findViewById(R.id.movieDetail_textView_titlePlaceholder_fragment);
        movieDetail_textView_caption_fragment = view.findViewById(R.id.movieDetail_textView_caption_fragment);
        movieDetail_button_watchTrailer_fragment = view.findViewById(R.id.movieDetail_button_watchTrailer_fragment);
        movieDetail_textView_title_fragment = view.findViewById(R.id.movieDetail_textView_title_fragment);
        movieDetail_textView_rating_fragment = view.findViewById(R.id.movieDetail_textView_rating_fragment);
        movieDetail_textView_popularity_fragment = view.findViewById(R.id.movieDetail_textView_popularity_fragment);
        movieDetail_textView_description_fragment = view.findViewById(R.id.movieDetail_textView_description_fragment);
        movieDetail_textView_productionCompanyStatic_fragment = view.findViewById(R.id.movieDetail_textView_productionCompanyStatic_fragment);
        movieDetail_recyclerView_productionCompany_fragment = view.findViewById(R.id.movieDetail_recyclerView_productionCompany_fragment);
        movieDetail_recyclerView_genre_fragment = view.findViewById(R.id.movieDetail_recyclerView_genre_fragment);

        viewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        viewModel.getMovieById(movieId);
        viewModel.getResultGetMovieById().observe(getActivity(), showResultMovieDetail);

        viewModel.getVideoById(movieId);
        viewModel.getResultGetVideoById().observe(getActivity(), showResultVideo);
    }

    private void setProgressBarDialog() {
        progressBarDialog = new Dialog(getActivity());
        progressBarDialog.setContentView(R.layout.circular_progress_bar);
        progressBarDialog.setCancelable(false);
        progressBarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        progressBarDialog.show();
    }

    private Observer<Movies> showResultMovieDetail = new Observer<Movies>() {
        @Override
        public void onChanged(Movies movies) {
            if (movies != null) {
                String year = movies.getRelease_date().substring(0, 4);
                int runtime = movies.getRuntime();
                String title = movies.getTitle();
                String voteAverage = String.valueOf(movies.getVote_average());
                String voteCount = String.valueOf(movies.getVote_count());
                String popularity = String.valueOf(movies.getPopularity());
                ArrayList<Movies.Genres> genreList = (ArrayList<Movies.Genres>) movies.getGenres();
                ArrayList<Movies.ProductionCompanies> productionCompaniesList = (ArrayList<Movies.ProductionCompanies>) movies.getProduction_companies();
                String tagline = movies.getTagline();
                String synopsis = movies.getOverview();

                String hour = String.valueOf(runtime / 60);
                String minute = String.valueOf(runtime % 60);

                String caption = getString(R.string.text_movieCaption, year, hour, minute);
                String rating = getString(R.string.text_movieRating, voteAverage, voteCount);
                String description = getString(R.string.text_movieDescription, tagline, synopsis);

                if (TextUtils.isEmpty(tagline)) {
                    description = synopsis;
                }

                movieDetail_textView_titlePlaceholder_fragment.setText(title);
                movieDetail_textView_caption_fragment.setText(caption);
                movieDetail_textView_title_fragment.setText(title);
                movieDetail_textView_rating_fragment.setText(rating);
                movieDetail_textView_popularity_fragment.setText(popularity);
                movieDetail_textView_description_fragment.setText(description);
                Glide.with(getActivity()).load(Const.IMG_URL_780 + movies.getBackdrop_path()).into(movieDetail_imageView_backdrop_fragment);
                Glide.with(getActivity()).load(Const.IMG_URL_500 + movies.getPoster_path()).into(movieDetail_imageView_poster_fragment);

                emptyHandler(movies, productionCompaniesList);

                //==Start of genre adapter
                MovieDetailGenreAdapter adapterGenre = new MovieDetailGenreAdapter(getActivity());
                adapterGenre.setMovieDetailGenreList(genreList);
                movieDetail_recyclerView_genre_fragment.setAdapter(adapterGenre);
                //==End of genre adapter

                //==Start of production company adapter
                MovieDetailProductionCompanyAdapter adapterProductionCompany = new MovieDetailProductionCompanyAdapter(getActivity());
                adapterProductionCompany.setProductionCompaniesList(productionCompaniesList);
                movieDetail_recyclerView_productionCompany_fragment.setAdapter(adapterProductionCompany);
                //==End of production company adapter
            }
        }
    };

    private Observer<Videos> showResultVideo = new Observer<Videos>() {
        @Override
        public void onChanged(Videos videos) {
            ArrayList<Videos.Results> videoList = (ArrayList<Videos.Results>) videos.getResults();
            if (videoList.size() != 0) {
                videoSite = videoList.get(0).getSite();
                videoKey = videoList.get(0).getKey();
            }

            progressBarDialog.hide();
        }
    };

    // * Handle view for empty data
    private void emptyHandler(Movies movies, ArrayList<Movies.ProductionCompanies> productionCompaniesList) {
        // ? Backdrop
        // Nothing

        // ? Poster
        if (movies.getPoster_path() != null) {
            movieDetail_textView_titlePlaceholder_fragment.setVisibility(View.INVISIBLE);
        }

        // ? Genre
        // Nothing

        // ? Production company
//        if (productionCompaniesList.size() < 1) {
//            movieDetail_textView_productionCompanyStatic_fragment.setVisibility(View.INVISIBLE);
//        }
    }
}