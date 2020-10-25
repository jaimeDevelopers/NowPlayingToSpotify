package com.jaime.addtracksspotifynowplaying.ui.activities;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaime.addtracksspotifynowplaying.MainActivity;
import com.jaime.addtracksspotifynowplaying.MyValues;
import com.jaime.addtracksspotifynowplaying.R;
import com.jaime.addtracksspotifynowplaying.db.database.Song;
import com.jaime.addtracksspotifynowplaying.db.database.SongListAdapter;
import com.jaime.addtracksspotifynowplaying.db.database.SongViewModel;
import com.jaime.addtracksspotifynowplaying.sources.Spotify;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Music extends Fragment {


    // Member variables.
    private RecyclerView mRecyclerView;


    private SongViewModel mSongViewModel;

    public Music() {

    }

    public static Music newInstance(int index) {
        Music fragment = new Music();
        Bundle bundle = new Bundle();
        bundle.putInt("HOLAAAAAAAAAERAEAASDASDADADJSDKLJKLADJKLSKJLKSDJLKJLDSJKLDSKJJKLS", index);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);


        createNotificationChannel();


        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getContext(), NewSongActivity.class);
                //startActivityForResult(intent, NEW_Song_ACTIVITY_REQUEST_CODE);
                //clearData();
                //Toast.makeText(getContext(), R.string.update, Toast.LENGTH_LONG).show();
                //Song Song = new Song("1");
                //mSongViewModel.deleteall();


                SharedPreferences pref = requireContext().getSharedPreferences(MyValues.PREFERENCES, Context.MODE_PRIVATE);
                boolean spotify_enabled = pref.getBoolean("SPOTIFY_ENABLED", false);

                if (spotify_enabled) {


                    mSongViewModel.deleteall();
                    Spotify testSpotify = new Spotify(requireContext(), requireActivity().getApplication());
                    testSpotify.initPlaylist();


                    //AlertDialog enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
                    //enableNotificationListenerAlertDialog.show();


                } else {
                    Toast.makeText(getContext(), "Please, first enable spotify", Toast.LENGTH_SHORT).show();

                }


                // create


//                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "CHANNEL_ID")
//                        .setSmallIcon(R.mipmap.ic_launcher_round)
//                        .setContentTitle("Prueba")
//                        .setContentText("notificacion");
//
//                // Creates the intent needed to show the notification
//                Intent notificationIntent = new Intent(getContext(), MainActivity.class);
//                PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                builder.setContentIntent(contentIntent);
//
//
//                // Add as notification
//                NotificationManager manager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                manager.notify(0, builder.build());


                //Song Song = new Song( "TEO", "China");
                //mSongViewModel.insert(Song);


//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Song jaime = mSongViewModel.getItemById(jai);
//                        if (jaime == null) {
//                            System.out.println("es nulo!!!@@@@@@");
//                            return;
//                        }
//                        System.out.println("eeeeeeeafkjasfasklfa2@@@@@ -->" + jaime.getId());
//                        jai ++;
//
//                    }
//                }).start();


//                mSongViewModel.getItemById(6).observe(getViewLifecycleOwner(), Songs -> {
//                    // Update the cached copy of the Songs in the adapter.
//                    //adapter.setSongs(Songs);
//
//                    if (Songs == null) {
//                        System.out.println("es nulo!!!");
//                        return;
//                    }
//                    System.out.println("ESTO ES LO QUE TENGO " + Songs.getCity());
//                    Toast.makeText(getContext(), Songs.getCity(), Toast.LENGTH_LONG).show();
//                });


            }
        });


        // Initialize the RecyclerView.
        mRecyclerView = root.findViewById(R.id.my_recycler_view);

        final SongListAdapter adapter = new SongListAdapter(getContext());
        mRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        //layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        layoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(layoutManager);


        // Get a new or existing ViewModel from the ViewModelProvider.
        mSongViewModel = new ViewModelProvider(this).get(SongViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedSongs.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mSongViewModel.getAllSongs().observe(getViewLifecycleOwner(), Songs -> {
            // Update the cached copy of the Songs in the adapter.
            adapter.setSongs(Songs);
        });

        //mSongViewModel.deleteall();
//
//        Song Song = new Song("JAIME", "ESPAÑA");
//        mSongViewModel.insert(Song);
//
//       Song = new Song("Lucia", "Mexico");
//       mSongViewModel.insert(Song);


        //Song = new Song("TEO", "China");
        //mSongViewModel.insert(Song);

//        MainActivity.runLogin();


//        mSongViewModel.getItemById(2).observe(getViewLifecycleOwner(), Songs -> {
//             //Update the cached copy of the Songs in the adapter.
//            //adapter.setSongs(Songs);
//            if (Songs ==null){
//                System.out.println("Entro aquiiiiiiiiii " );
//
//                return;
//            }
//            System.out.println("Cris Uni ESTO ES LO QUE TENGO " + Songs.getCity());
//            Toast.makeText(getContext(), Songs.getCity(), Toast.LENGTH_SHORT).show();
//        });
        //LiveData<Song> jaime = mSongViewModel.getItemById(3);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                Song jaime = mSongViewModel.getItemById(1);
//                if (jaime == null) {
//                    System.out.println("es nulo!!! --->");
//                    return;
//                }
//                System.out.println("eeeeeeeafkjasfasklfa ----> " + jaime.getCity());
//
//
//            }
//        }).start();


//
//        mSongViewModel = new ViewModelProvider(this).get(SongViewModel.class);
//
//
//        // Add an observer on the LiveData returned by getAlphabetizedSongs.
//        // The onChanged() method fires when the observed data changes and the activity is
//        // in the foreground.
//        mSongViewModel.getAllSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
//            @Override
//            public void onChanged(@Nullable final List<Song> Songs) {
//                // Update the cached copy of the Songs in the adapter.
//                adapter.setSongs(Songs);
//            }
//        });

//
//        // Set the Layout Manager.
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // Initialize the ArrayList that will contain the data.
//        mSportsData = new ArrayList<>();
//
//        // Initialize the adapter and set it to the RecyclerView.
//        mAdapter = new SportsAdapter(getContext(), mSportsData);
//        mRecyclerView.setAdapter(mAdapter);
//
//        // Get the data.
//        initializeData();


        // Helper class for creating swipe to dismiss and drag and drop
        // functionality.
//        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper
//                .SimpleCallback(
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
//                        ItemTouchHelper.DOWN | ItemTouchHelper.UP,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            /**
//             * Defines the drag and drop functionality.
//             *
//             * @param recyclerView The RecyclerView that contains the list items
//             * @param viewHolder The SportsViewHolder that is being moved
//             * @param target The SportsViewHolder that you are switching the
//             *               original one with.
//             * @return true if the item was moved, false otherwise
//             */
//            @Override
//            public boolean onMove(RecyclerView recyclerView,
//                                  RecyclerView.ViewHolder viewHolder,
//                                  RecyclerView.ViewHolder target) {
//                // Get the from and to positions.
//                int from = viewHolder.getAdapterPosition();
//                int to = target.getAdapterPosition();
//
//                // Swap the items and notify the adapter.
//                Collections.swap(mSportsData, from, to);
//                mAdapter.notifyItemMoved(from, to);
//                return true;
//            }
//
//            /**
//             * Defines the swipe to dismiss functionality.
//             *
//             * @param viewHolder The viewholder being swiped.
//             * @param direction The direction it is swiped in.
//             */
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder,
//                                 int direction) {
//                // Remove the item from the dataset.
//                mSportsData.remove(viewHolder.getAdapterPosition());
//                // Notify the adapter.
//                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//            }
//        });
//
//        // Attach the helper to the RecyclerView.
//        helper.attachToRecyclerView(mRecyclerView);


        // Put initial data into the Song list.
        //  for (int i = 0; i < 20; i++) {
        //      mSongList.addLast("Song " + i);
        //  }


        //mRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);


// Get a handle to the RecyclerView.
// Create an adapter and supply the data to be displayed.
        // mAdapter = new SongListAdapter(getContext(), mSongList);
// Connect the adapter with the RecyclerView.
        //      mRecyclerView.setAdapter(mAdapter);
// Give the RecyclerView a default layout manager.
        //      mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "JAIME.PRUEBA";
            String description = "ESTADOS UNIDOS";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private AlertDialog buildNotificationServiceAlertDialog() {


        SharedPreferences pref = requireContext().getSharedPreferences(MyValues.PREFERENCES, Context.MODE_PRIVATE);
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String playlistName = pref.getString("playlistName", date + " Now playing");         // getting String

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation + "\"" + playlistName + "\"");


        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        alertDialogBuilder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });

        return (alertDialogBuilder.create());
    }


}