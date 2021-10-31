package com.ss.moviedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.moviedb.R;
import com.ss.moviedb.model.Genre;
import com.ss.moviedb.model.Movies;
import com.ss.moviedb.model.Upcoming;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.CardViewViewHolder> {

    private Context context;
    private ArrayList<Genre.Genres> genreList;

    private ArrayList<Genre.Genres> getGenreList() {
        return genreList;
    }

    public void setGenreList(ArrayList<Genre.Genres> genreList) {
        this.genreList = genreList;
    }

    public GenreAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_movie_detail_genre, parent, false);

        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
        final Genre.Genres results = getGenreList().get(position);

        holder.movieDetail_textView_genre.setText(results.getName());
    }

    @Override
    public int getItemCount() {
        return getGenreList().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {

        TextView movieDetail_textView_genre;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);

            movieDetail_textView_genre = itemView.findViewById(R.id.movieDetail_textView_genre);
        }
    }
}
