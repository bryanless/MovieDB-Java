package com.ss.moviedb.view.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.ss.moviedb.adapter.UpcomingAdapter;
import com.ss.moviedb.model.NowPlaying;
import com.ss.moviedb.model.Upcoming;
import com.ss.moviedb.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.Observable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingFragment extends Fragment {

    private View view;
    private ProgressBar upcoming_progressBar_fragment;
    private RecyclerView upcoming_recyclerView_fragment;
    private Dialog progressBarDialog;

    private UpcomingAdapter adapter;
    private MovieViewModel viewModel;

    private int page, totalPage;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingFragment newInstance(String param1, String param2) {
        UpcomingFragment fragment = new UpcomingFragment();
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

        setProgressBarDialog();
        initialize();
        setRecyclerView();
        setListener();

        return view;
    }

    private void setListener() {
        upcoming_recyclerView_fragment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // ! This seems stupid
                // TODO: Find alternative for endless scroll
                GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (manager.findLastVisibleItemPosition() > recyclerView.getAdapter().getItemCount() - 7) {
                    if ((page * 20) - recyclerView.getAdapter().getItemCount() < 3) {

                        if (page <= totalPage) {
                            page++;

                            viewModel.getUpcoming(String.valueOf(page));
                            viewModel.getResultGetUpcoming().observe(getActivity(), showResultNewUpcoming);
                        }
                    }
                }
            }
        });
    }

    private void setRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        upcoming_recyclerView_fragment.setLayoutManager(manager);
    }

    private void initialize() {
        page = 1;

        upcoming_recyclerView_fragment = view.findViewById(R.id.upcoming_recyclerView_fragment);

        viewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        viewModel.getUpcoming(String.valueOf(page));
        viewModel.getResultGetUpcoming().observe(getActivity(), showResultUpcoming);
    }

    private void setProgressBarDialog() {
        progressBarDialog = new Dialog(getActivity());
        progressBarDialog.setContentView(R.layout.circular_progress_bar);
        progressBarDialog.setCancelable(false);
        progressBarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        progressBarDialog.show();
    }

    private Observer<Upcoming> showResultUpcoming = new Observer<Upcoming>() {
        @Override
        public void onChanged(Upcoming upcoming) {
            totalPage = upcoming.getTotal_pages();

            adapter = new UpcomingAdapter(getActivity());
            adapter.setUpcomingList((ArrayList<Upcoming.Results>) upcoming.getResults());
            upcoming_recyclerView_fragment.setAdapter(adapter);

            progressBarDialog.hide();
        }
    };

    private Observer<Upcoming> showResultNewUpcoming = new Observer<Upcoming>() {
        @Override
        public void onChanged(Upcoming upcoming) {
            adapter.addUpcomingList((ArrayList<Upcoming.Results>) upcoming.getResults());
            adapter.notifyItemRangeChanged(adapter.getItemCount() + 1, upcoming.getResults().size());
        }
    };
}