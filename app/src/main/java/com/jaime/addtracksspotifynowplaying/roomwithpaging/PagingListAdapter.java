package com.jaime.addtracksspotifynowplaying.roomwithpaging;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jaime.addtracksspotifynowplaying.R;
import com.jaime.addtracksspotifynowplaying.db.database.Song;
import com.jaime.addtracksspotifynowplaying.ui.activities.SongDetails;

public class PagingListAdapter extends PagedListAdapter<Song, PagingListAdapter.SongViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;


    public PagingListAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

    }


    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView SongItemTitle;
        private final TextView SongItemSubtitle;
        //private final TextView SongItemSubTitle;


        private SongViewHolder(View itemView) {
            super(itemView);
            SongItemTitle = itemView.findViewById(R.id.Notification);
            SongItemSubtitle = itemView.findViewById(R.id.Search);
            //SongItemSubTitle = itemView.findViewById(R.id.date);


            itemView.setOnClickListener(this);
        }

        void bindTo(Song song) {
            this.SongItemTitle.setText(song.getNowPlayingSong());
            this.SongItemSubtitle.setText(song.getStreamingName());

            //SongItemTitle.setText(valueOf(current.getNowPlayingSong()));
            //holder.SongItemSubtitle.setText(current.getStreamingSong());
            //holder.SongItemSubTitle.setText(current.getNowPlayingSong());
        }

        void clear() {
            this.SongItemTitle.setText("No song");
            this.SongItemSubtitle.setText("");
        }

        @Override
        public void onClick(View view) {

            if (getAdapterPosition() > RecyclerView.NO_POSITION) {
                Song currentSong = getItem(getAdapterPosition());
                //if (repo != null && !TextUtils.isEmpty(repo.url)) {
                //    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(repo.url));
                //    view.getContext().startActivity(intent);
                //}
                //mSongs.get(getAdapterPosition());

                Intent detailIntent = new Intent(mContext, SongDetails.class);
                //public Song(String nowPlayingSong, String streamingSong, String date, String infoSearch) {

                assert currentSong != null;
                detailIntent.putExtra("nowPlayingSong", currentSong.getNowPlayingSong());
                detailIntent.putExtra("streamingSong", currentSong.getStreamingName());
                detailIntent.putExtra("date", currentSong.getDate());
                detailIntent.putExtra("infoSearch", currentSong.getInfoSearch());


                mContext.startActivity(detailIntent);
            }
        }

    }


    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.cards_layout, parent, false);
        return new SongViewHolder(itemView);
    }

    Song mSong;


    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {

        Song current = getItem(position);
        if (current != null) {
            holder.bindTo(current);
            mSong = current;


            //holder.SongItemTitle.setText(valueOf(current.getNowPlayingSong()));
            //holder.SongItemSubtitle.setText(current.getStreamingSong());
            //holder.SongItemSubTitle.setText(current.getNowPlayingSong());

        } else {
            // Covers the case of data not being ready yet.
            holder.clear();
        }
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    private static DiffUtil.ItemCallback<Song> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Song>() {
                // Concert details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(Song oldSong, Song newSong) {
                    return oldSong.getId() == (newSong.getId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Song oldSong,
                                                  @NonNull Song newSong) {
                    return oldSong.equals(newSong);
                }
            };

}
