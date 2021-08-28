package com.jaime.addtracksspotifynowplaying.ui.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaime.addtracksspotifynowplaying.MyValues;
import com.jaime.addtracksspotifynowplaying.R;
import com.jaime.addtracksspotifynowplaying.db.database.SongViewModel;
import com.jaime.addtracksspotifynowplaying.roomwithpaging.PagingListAdapter;
import com.jaime.addtracksspotifynowplaying.sources.Spotify;

import static android.content.ContentValues.TAG;


public class Music extends Fragment {


    private SongViewModel mSongViewModel;
    Context context;

    public Music() {

    }

    public static Music newInstance() {
        return new Music();
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

        context = getContext();
        NotificationPermission notificationPermission = new NotificationPermission(context);


        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            //Intent intent = new Intent(getContext(), NewSongActivity.class);
            //startActivityForResult(intent, NEW_Song_ACTIVITY_REQUEST_CODE);
            //clearData();
            //Toast.makeText(getContext(), R.string.update, Toast.LENGTH_LONG).show();
            //Song Song = new Song("1");
            //mSongViewModel.deleteall();


            SharedPreferences pref = requireContext().getSharedPreferences(MyValues.PREFERENCES, Context.MODE_PRIVATE);
            boolean spotify_enabled = pref.getBoolean("SPOTIFY_ENABLED", false);


            if (spotify_enabled && notificationPermission.isNotificationServiceEnabled(context)) {

                mSongViewModel.deleteall();
                Spotify testSpotify = new Spotify(requireContext(), requireActivity().getApplication());
                testSpotify.initPlaylist();


                //AlertDialog enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
                //enableNotificationListenerAlertDialog.show();

            } else {
                Toast.makeText(context, "Please, first enable spotify and notification listener", Toast.LENGTH_SHORT).show();
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


        });


        // Initialize the RecyclerView.
        // Member variables.
        RecyclerView mRecyclerView = root.findViewById(R.id.my_recycler_view);

        //final SongListAdapter adapter = new SongListAdapter(getContext());
        //mRecyclerView.setAdapter(adapter);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(layoutManager);


        // Get a new or existing ViewModel from the ViewModelProvider.
        mSongViewModel = new ViewModelProvider(this).get(SongViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedSongs.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.


        final PagingListAdapter adapterPaging = new PagingListAdapter(getContext());
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSongViewModel.mFewWords.observe(getViewLifecycleOwner(), pagedList -> {
            Log.e(TAG, "Paged List Changed. New Size is: " + pagedList.size());
            adapterPaging.submitList(pagedList);
        });

        mRecyclerView.setAdapter(adapterPaging);



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private boolean isNotificationServiceEnabled() {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(),
                MyValues.ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}