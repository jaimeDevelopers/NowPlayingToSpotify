package com.jaime.addtracksspotifynowplaying;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import com.jaime.addtracksspotifynowplaying.db.database.Song;
import com.jaime.addtracksspotifynowplaying.sources.Spotify;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class GetNowPlayingNotifications extends NotificationListenerService {


    private SharedPreferences pref;
    String PackageName;

    public Spotify Source;
    //private SongViewModel mSongViewModel;

    public Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();


        PackageName = MyValues.PACKAGE_NAME_UNDER_30;
        if (Build.VERSION.SDK_INT >= 30) {
            PackageName = MyValues.PACKAGE_NAME_30;
        }


        this.pref = getApplicationContext().getSharedPreferences(MyValues.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = this.pref.edit();
        editor.putString("PackageName", PackageName);  // Saving string

        Map<String, String> language = new HashMap<String, String>();
        language.put("en", " by ");
        language.put("es", " de ");
        language.put("de", " von ");
        language.put("fr", " par ");
        language.put("it", " di ");
        language.put("nl", " van ");
        language.put("pl", " - ");
        language.put("pt", " de ");
        language.put("ru", " , ");

        editor.putString("split_util", language.get(Locale.getDefault().getLanguage()));  // Saving string
        editor.apply(); // commit changes


        if (isNetworkAvailable()) {
            Source = new Spotify(getApplicationContext(), getApplication());
        } else {
            Toast.makeText(context, "Connect to Internet, please", Toast.LENGTH_SHORT).show();
        }
        //
        //Source.initPlaylist(playlistName);

        //Source.checkAndRefreshSpotifyToken();
        //this.accountsPrefs = context.getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_ACCOUNT_FILE_NAME, Context.MODE_PRIVATE);
        //this.namePlaylistPrefs = getApplicationContext().getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_NAME_PLAYLIST, Context.MODE_PRIVATE);
        //this.Source.Check(Get_Now_Playing_Notifications.accountsPrefs);

        //try {
        //    this.Source.init_Cache(this.namePlaylistPrefs.getString("playlist_name", getResources().getString(R.string.PlayListName)));
        //} catch (Exception e){
        //    e.printStackTrace();
        //

        // Get a new or existing ViewModel from the ViewModelProvider.

    }


    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            matchNotificationCode(sbn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void matchNotificationCode(StatusBarNotification sbn) {
        String notification = sbn.getPackageName();

        if (isNetworkAvailable()) {

            if (notification.equalsIgnoreCase(PackageName)) {

                //if (notification.equals(PackageName)){
                //this.context = getApplicationContext();
                String title = sbn.getNotification().extras.getString("android.title");
                String text = sbn.getNotification().extras.getString("android.text");
                String package_name = sbn.getPackageName();
                assert title != null;
                Log.v("Notification title is", title);
                assert text != null;
                Log.v("Notification text is", text);
                Log.v("N. Package Name", package_name);

                this.pref = getApplicationContext().getSharedPreferences(MyValues.PREFERENCES, MODE_PRIVATE);
                String lastsong = pref.getString("last_song", null);         // getting String
                String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                //String playlistName = pref.getString("playlistName", date + " Now playing");         // getting String

                if (!title.equalsIgnoreCase(lastsong)) {
                    SharedPreferences.Editor editor = this.pref.edit();
                    editor.putString("last_song", title);  // Saving string
                    editor.apply(); // commit changes


                    if (Source == null) {
                        Source = new Spotify(getApplicationContext(), getApplication());
                    }


                    Source.recognizedSong(title);

                    //Source.searchsong(title);
                    //Source.Create_a_playlist();

                }
            }

            //mRepository = new SongRepository(getApplication());
            //jaime++;

//            prue = mRepository.getItemById(10000);
//            if (prue == null) {
//                System.out.println("El valor es nulo");
//            } else {
//                System.out.println("aaa" + prue.getCity());
//
//            }


            //Source.Check();


//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//
//                try {
//                    prue = mRepository.getItemById(79999);
//                } catch (Exception E) {
//                    System.out.println("NULOOOOOOOO" + E.getMessage());
//                }
//
//
//            }
//        };
//
//        thread.start();
//
//        thread.join();


//            //if(isNetworkAvailable()){
//            //    try {
//            //        System.out.println();
//                    //this.Source.CreatePlaylistCache(this.namePlaylistPrefs.getString("playlist_name", getResources().getString(R.string.PlayListName)), false, false, title);
//                }catch (Exception e) {
//                    e.printStackTrace();
//                    this.accountsPrefs = context.getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_ACCOUNT_FILE_NAME, Context.MODE_PRIVATE);
//                    this.namePlaylistPrefs = getApplicationContext().getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_NAME_PLAYLIST, Context.MODE_PRIVATE);
//                    Get_Now_Playing_Notifications.Source.Check(Get_Now_Playing_Notifications.accountsPrefs);
//                    matchNotificationCode(sbn);
//                }


        }

    }
    // }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


}
