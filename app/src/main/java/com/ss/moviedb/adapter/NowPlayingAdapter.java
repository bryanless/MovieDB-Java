package com.ss.moviedb.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ss.moviedb.R;
import com.ss.moviedb.helper.Const;
import com.ss.moviedb.model.NowPlaying;
import com.ss.moviedb.view.MovieDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.CardViewViewHolder> {

    private Context context;
    private ArrayList<NowPlaying.Results> nowPlayingList;

    private ArrayList<NowPlaying.Results> getNowPlayingList() {
        return nowPlayingList;
    }

    public void setNowPlayingList(ArrayList<NowPlaying.Results> nowPlayingList) {
        this.nowPlayingList = nowPlayingList;
    }

    public NowPlayingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.card_now_playing, parent, false);
        View view = layoutInflater.inflate(R.layout.card_now_playing_2, parent, false);

        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
        final NowPlaying.Results results = getNowPlayingList().get(position);

//        holder.nowPlaying_textView_title.setText(results.getTitle());
//        holder.nowPlaying_textView_overview.setText(results.getOverview());
//        holder.nowPlaying_textView_releaseDate.setText(results.getRelease_date());
//        Glide.with(context).load(Const.IMG_URL + results.getPoster_path()).into(holder.nowPlaying_imageView_poster);
        Glide.with(context).load(Const.IMG_URL + results.getPoster_path()).into(holder.nowPlaying_imageView_poster_2);

        holder.nowPlaying_cardView_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("movieId", String.valueOf(results.getId()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getNowPlayingList().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {

        CardView nowPlaying_cardView_movie;
        ImageView nowPlaying_imageView_poster,nowPlaying_imageView_poster_2;
        TextView nowPlaying_textView_title, nowPlaying_textView_overview, nowPlaying_textView_releaseDate;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);

            nowPlaying_cardView_movie = itemView.findViewById(R.id.nowPlaying_cardView_movie);
//            nowPlaying_imageView_poster = itemView.findViewById(R.id.nowPlaying_imageView_poster);
//            nowPlaying_textView_title = itemView.findViewById(R.id.nowPlaying_textView_title);
//            nowPlaying_textView_overview = itemView.findViewById(R.id.nowPlaying_textView_overview);
//            nowPlaying_textView_releaseDate = itemView.findViewById(R.id.nowPlaying_textView_releaseDate);
            nowPlaying_imageView_poster_2 = itemView.findViewById(R.id.nowPlaying_imageView_poster_2);
        }
    }
}
