package com.jaime.addtracksspotifynowplaying.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jaime.addtracksspotifynowplaying.MyValues;
import com.jaime.addtracksspotifynowplaying.R;


import com.jaime.addtracksspotifynowplaying.sources.Spotify;
//import com.spotify.sdk.android.authentication.AuthenticationClient;
//import com.spotify.sdk.android.authentication.AuthenticationRequest;
//import com.spotify.sdk.android.authentication.AuthenticationResponse;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;
//import com.spotify.sdk.android.authentication.AuthenticationClient;
//import com.spotify.sdk.android.authentication.AuthenticationRequest;
//import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import static android.view.KeyEvent.KEYCODE_BACK;


public class LoginSpotify extends AppCompatActivity {


    TextInputLayout playlist_name;
    Context context;

    private SharedPreferences pref;


    /**
     * Initializes the activity, filling in the data from the Intent.
     *
     * @param savedInstanceState Contains information about the saved state
     *                           of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);
        context = getApplicationContext();

        Toolbar mTopToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(mTopToolbar);

        playlist_name = findViewById(R.id.playlistname);

        this.pref = context.getSharedPreferences(MyValues.PREFERENCES, MODE_PRIVATE);
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String playlistName = pref.getString("playlistName", date + " Now Playing");         // getting String


        playlist_name.getEditText().setText(playlistName);
        EventoTeclado teclado = new EventoTeclado();
        playlist_name.getEditText().setOnEditorActionListener(teclado);

        hint_setOnFocusChangeListener();


        playlist_name.setEndIconOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                update(v);
                //***Do what you want with the click here***
            }
        });


        //runLogin();


        // Initialize the views.
        //TextView sportsTitle = findViewById(R.id.titleDetail);
        // Set the text from the Intent extra.
        //sportsTitle.setText(getIntent().getStringExtra("title"));

    }

    public void onRequestCodeClicked(View view) {
        runLogin();
    }


    private void runLogin() {

        SharedPreferences pref = getSharedPreferences(MyValues.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("SPOTIFY_ENABLED", false);
        editor.apply();

        //Get_Now_Playing_Notifications.accountsPrefs = getApplicationContext().getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_ACCOUNT_FILE_NAME, Context.MODE_PRIVATE);
        //Get_Now_Playing_Notifications.namePlaylistPrefs = getApplicationContext().getSharedPreferences(Get_Now_Playing_Notifications.PREFERENCES_NAME_PLAYLIST, Context.MODE_PRIVATE);

        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(MyValues.SPOTIFY_CLIENT_ID, AuthorizationResponse.Type.CODE, MyValues.SPOTIFY_REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "user-read-email", "user-follow-read",
                "playlist-read-private", "playlist-read-collaborative", "user-library-read", "user-library-modify",
                "playlist-modify-public", "playlist-modify-private"});
        builder.setShowDialog(false);
        builder.setCampaign("your-campaign-token");

        // builder.setScopes(new String[]{"user-read-private", "streaming", "user-read-email", "user-follow-read",
        //        "playlist-read-private", "playlist-read-collaborative", "user-library-read", "user-library-modify",
        //        "playlist-modify-public", "playlist-modify-private", "user-follow-modify"});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(this, MyValues.SPOTIFY_REQUEST_CODE, request);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == MyValues.SPOTIFY_REQUEST_CODE) {


            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthorizationResponse.Type.CODE) {

                final String code = response.getCode();
                Thread t = new Thread() {
                    public void run() {
                        Looper.prepare();
                        try {
                            SharedPreferences pref = getSharedPreferences(MyValues.PREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();


                            URL apiUrl = new URL("https://accounts.spotify.com/api/token");
                            HttpsURLConnection urlConnection = (HttpsURLConnection) apiUrl.openConnection();
                            urlConnection.setDoInput(true);
                            urlConnection.setDoOutput(true);
                            urlConnection.setRequestMethod("POST");

                            //write POST parameters
                            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
                            writer.write("grant_type=authorization_code&");
                            writer.write("code=" + code + "&");
                            writer.write("redirect_uri=" + MyValues.SPOTIFY_REDIRECT_URI + "&");
                            writer.write("client_id=" + MyValues.SPOTIFY_CLIENT_ID + "&");
                            writer.write("client_secret=" + "765e6769347547bfb48aa13a67f0e97a");
                            writer.flush();
                            writer.close();
                            out.close();
                            urlConnection.connect();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            String result = reader.readLine();
                            reader.close();
                            result = result.substring(1);
                            result = result.substring(0, result.length() - 1);

                            String[] results = result.split(",");

                            for (String param : results) {
                                if (param.startsWith("\"access_token\":\"")) {
                                    param = param.replaceFirst("\"access_token\":\"", "");
                                    param = param.replaceFirst("\"", "");
                                    editor = pref.edit();
                                    editor.putString("spotify_token", param);
                                    editor.apply();
                                } else if (param.startsWith("\"refresh_token\":\"")) {
                                    param = param.replaceFirst("\"refresh_token\":\"", "");
                                    param = param.replaceFirst("\"", "");
                                    //MyValues.SPOTIFY_REFRESH_TOKEN = param;
                                    editor = pref.edit();
                                    editor.putString("spotify_refresh_token", param);
                                    //editor.putString("SPOTIFY_USER_TOKEN", param);
                                    editor.apply();
                                }
                            }

                            editor.putBoolean("SPOTIFY_ENABLED", true);
                            editor.apply();


                            Spotify testSpotify = new Spotify(getApplicationContext(), getApplication());


                            //Reset playlist playlist
                            testSpotify.initPlaylist();


                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                };


                // if (!isNotificationServiceEnabled()) {
                //    enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
                //     enableNotificationListenerAlertDialog.show();
                // }


                //testTheApp.setVisibility(View.VISIBLE);
                t.start();

            } else {
                //Snackbar.make(findViewById(android.R.id.content), "Wrong reponse received.", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                //testTheApp.setVisibility(View.GONE);

                Toast.makeText(context, "Wrong reponse received: " + response.getError(), Toast.LENGTH_SHORT).show();

            }


        }


    }

    public void onClearCredentialsClicked(View view) {
        SharedPreferences pref = getSharedPreferences(MyValues.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("SPOTIFY_ENABLED", false);
        editor.apply();
        AuthorizationClient.clearCookies(this);
    }


    void hint_setOnFocusChangeListener() {

        playlist_name.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                update(null);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String playlistName = pref.getString("playlistName", date + " Now Playing");         // getting String
        playlist_name.getEditText().setText(playlistName);
    }

    public void onRequestsynchronize(View view) {

        boolean spotify_enabled = pref.getBoolean("SPOTIFY_ENABLED", false);

        if (spotify_enabled) {
            update(view);
        } else {
            Toast.makeText(context, "Please, first enable spotify", Toast.LENGTH_SHORT).show();

        }

    }

    class EventoTeclado implements TextView.OnEditorActionListener {


        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (playlist_name.getEditText().getText().toString().length() == 0) {
                    String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                    String playlistName = pref.getString("playlistName", date + " Now Playing");         // getting String

                    playlist_name.getEditText().setText(playlistName);
                }
                update(null);
            }
            return false;
        }
    }

    public void update(View v) {
        try {

            SharedPreferences.Editor editor = this.pref.edit();


            if (Objects.requireNonNull(playlist_name.getEditText().getText()).toString().length() == 0) {
                String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                String playlistName = pref.getString("playlistName", date + " Now Playing");         // getting String
                playlist_name.getEditText().setText(playlistName);
            }

            editor.putString("playlistName", playlist_name.getEditText().getText().toString());  // Saving string
            editor.apply();


            InputMethodManager mKeyboard = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
            mKeyboard.hideSoftInputFromWindow(playlist_name.getWindowToken(), 0);

            playlist_name.clearFocus();


            //playlist_name.clearFocus();

            try {

                Spotify testSpotify = new Spotify(getApplicationContext(), getApplication());


                //Reset playlist playlist

                testSpotify.initPlaylist();


            } catch (Exception e) {
                e.printStackTrace();


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}