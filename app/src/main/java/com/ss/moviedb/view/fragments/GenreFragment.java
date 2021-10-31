package com.ss.moviedb.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.ss.moviedb.R;
import com.ss.moviedb.adapter.GenreAdapter;
import com.ss.moviedb.adapter.UpcomingAdapter;
import com.ss.moviedb.model.Genre;
import com.ss.moviedb.model.Upcoming;
import com.ss.moviedb.viewmodel.GenreViewModel;
import com.ss.moviedb.viewmodel.MovieViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenreFragment extends Fragment {

    private View view;
    private ProgressBar genre_progressBar_fragment;
    private RecyclerView genre_recyclerView_fragment;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenreFragment newInstance(String param1, String param2) {
        GenreFragment fragment = new GenreFragment();
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
        view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        initialize();
        setRecyclerView();

        return view;
    }

    private void setRecyclerView() {
        FlexboxLayoutManager manager = new FlexboxLayoutManager(getActivity(), FlexDirection.ROW, FlexWrap.WRAP);
        manager.setJustifyContent(JustifyContent.CENTER);
        genre_recyclerView_fragment.setLayoutManager(manager);
    }

    private void initialize() {
        genre_progressBar_fragment = view.findViewById(R.id.genre_progressBar_fragment);
        genre_recyclerView_fragment = view.findViewById(R.id.genre_recyclerView_fragment);

        genre_progressBar_fragment.setVisibility(View.VISIBLE);

        // TODO: Discover view model to get movie by genre
    }

    private Observer<Genre> showResultGenre = new Observer<Genre>() {
        @Override
        public void onChanged(Genre results) {
            genre_progressBar_fragment.setVisibility(View.GONE);

            // TODO: View model
        }
    };
}