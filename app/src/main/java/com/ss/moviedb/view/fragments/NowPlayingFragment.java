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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ss.moviedb.R;
import com.ss.moviedb.adapter.NowPlayingAdapter;
import com.ss.moviedb.model.NowPlaying;
import com.ss.moviedb.viewmodel.MovieViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NowPlayingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NowPlayingFragment extends Fragment {

    private View view;
    private ProgressBar nowPlaying_progressBar_fragment;
    private RecyclerView nowPlaying_recyclerView_fragment;
    private Dialog progressBarDialog;

    private NowPlayingAdapter adapter;
    private MovieViewModel viewModel;

    private int page, totalPage;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NowPlayingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NowPlayingFragment newInstance(String param1, String param2) {
        NowPlayingFragment fragment = new NowPlayingFragment();
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
        view = inflater.inflate(R.layout.fragment_now_playing, container, false);

        setProgressBarDialog();
        initialize();
        setRecyclerView();
        setListener();

        return view;
    }

    private void setListener() {
        nowPlaying_recyclerView_fragment.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                            viewModel.getNowPlaying(String.valueOf(page));
                            viewModel.getResultGetNowPlaying().observe(getActivity(), showResultNewNowPlaying);
                        }
                    }
                }
            }
        });
    }

    private void setRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        nowPlaying_recyclerView_fragment.setLayoutManager(manager);
    }

    private void initialize() {
        page = 1;

        nowPlaying_recyclerView_fragment = view.findViewById(R.id.nowPlaying_recyclerView_fragment);

        viewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        viewModel.getNowPlaying(String.valueOf(page));
        viewModel.getResultGetNowPlaying().observe(getActivity(), showResultNowPlaying);
    }

    private void setProgressBarDialog() {
        progressBarDialog = new Dialog(getActivity());
        progressBarDialog.setContentView(R.layout.circular_progress_bar);
        progressBarDialog.setCancelable(false);
        progressBarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        progressBarDialog.show();
    }

    private Observer<NowPlaying> showResultNowPlaying = new Observer<NowPlaying>() {
        @Override
        public void onChanged(NowPlaying nowPlaying) {
            totalPage = nowPlaying.getTotal_pages();

            adapter = new NowPlayingAdapter(getActivity());
            adapter.setNowPlayingList((ArrayList<NowPlaying.Results>) nowPlaying.getResults());
            nowPlaying_recyclerView_fragment.setAdapter(adapter);

            progressBarDialog.hide();

//            ItemClickSupport.addTo(nowPlaying_recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
//                    return false;
//                }
//            });

            // Alternative of onClick on adapter - seems harder to maintain(?)
//            ItemClickSupport.addTo(nowPlaying_recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//                @Override
//                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("movieId", String.valueOf(nowPlaying.getResults().get(position).getId()));
//                    Navigation.findNavController(view).navigate(R.id.action_nowPlayingFragment_to_movieDetailFragment, bundle);
//                }
//            });
        }
    };

    private Observer<NowPlaying> showResultNewNowPlaying = new Observer<NowPlaying>() {
        @Override
        public void onChanged(NowPlaying nowPlaying) {
            adapter.addNowPlayingList((ArrayList<NowPlaying.Results>) nowPlaying.getResults());
            adapter.notifyItemRangeChanged(adapter.getItemCount() + 1, nowPlaying.getResults().size());
        }
    };
}