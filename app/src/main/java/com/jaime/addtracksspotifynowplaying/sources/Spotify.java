package com.jaime.addtracksspotifynowplaying.sources;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jaime.addtracksspotifynowplaying.MyValues;
import com.jaime.addtracksspotifynowplaying.db.database.Song;
import com.jaime.addtracksspotifynowplaying.db.database.SongRepository;

import org.apache.commons.text.similarity.JaroWinklerDistance;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.RetrofitError;

import static android.content.Context.MODE_PRIVATE;


public class Spotify {
    private SongRepository mRepository;
    public final SpotifyApi spotifyApi = new SpotifyApi();
    private Context context;
    public UserPrivate mePrivate;


    public String info_search;
    public StringBuilder info_search_builder;


    public Spotify(Context context, Application app) {
        this.context = context;
        this.mRepository = new SongRepository(app);
        Check(context);
        //this.Check(context);
        //this.spotifyApi= new SpotifyApi();
    }

    public void initPlaylist() {

        SharedPreferences pref = context.getSharedPreferences(MyValues.PREFERENCES, Context.MODE_PRIVATE);
        boolean spotify_enabled = pref.getBoolean("SPOTIFY_ENABLED", false);

        if (spotify_enabled) {
            String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
            String playlistName = pref.getString("playlistName", date + " Now Playing");         // getting String

            new Thread() {
                public void run() {
                    startPlaylist(playlistName);
                }
            }.start();

        }
        //SharedPreferences pref = context.getSharedPreferences(MyValues.PREFERENCES_NOTIFICATION, MODE_PRIVATE);
        //SharedPreferences.Editor editor = pref.edit();
        //editor.putString("userPlaylistId", userPlaylistId);  // Saving string
        //editor.apply(); // commit changes
        //createCache()
    }


    public void startPlaylist(String playlistName) {
        playlistHandler(playlistName);
        loadSongToDb();

    }

    public void loadSongToDb() {

        try {

            mRepository.deleteall();
            SharedPreferences pref = context.getSharedPreferences(MyValues.PREFERENCES, MODE_PRIVATE);
            String ownerId = pref.getString("userPlaylistOwner", null);         // getting String
            String playlistId = pref.getString("userPlaylistId", null);         // getting String
            Pager<PlaylistTrack> tracks = null;


            HashMap<String, Object> map = new HashMap<>();
            int poffset = 0;
            int trackNbr = pref.getInt("userPlaylistTrackTotal", 0);
            while (trackNbr > 0) {
                map.put("offset", poffset);
                try {
                    tracks = spotifyApi.getService().getPlaylistTracks(ownerId, playlistId, map);

                } catch (RetrofitError e) {
                    if (e.getResponse() != null && e.getResponse().getStatus() == 401) {
                        refreshSpotifyToken();
                        mePrivate = spotifyApi.getService().getMe();
                        tracks = spotifyApi.getService().getPlaylistTracks(ownerId, playlistId, map);
                    }
                }
                if (tracks != null) {
                    for (PlaylistTrack pt : tracks.items) {

                        if (pt.added_at == null) {
                            pt.added_at = "";
                        }

                        StringBuilder misArtistas = new StringBuilder().append(" by:");
                        for (ArtistSimple artist : pt.track.artists) {
                            misArtistas.append(" ").append(artist.name);
                        }

                        mRepository.insert(new Song("Not detected", pt.track.name + misArtistas.toString(), pt.added_at, "No searched yet by your music app"));
                    }
                }


                poffset += 100;
                trackNbr -= 100;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void playlistHandler(String playlistName) {
        PlaylistSimple userPlaylist = getPlaylist(playlistName);

        if (userPlaylist == null) {
            createPlaylist(playlistName);
        }
    }

    public PlaylistSimple getPlaylist(String playlistName) {
        Pager<PlaylistSimple> userPlaylists = null;

        try {
            userPlaylists = spotifyApi.getService().getMyPlaylists();

        } catch (RetrofitError e) {
            if (e.getResponse() != null && e.getResponse().getStatus() == 401) {
                refreshSpotifyToken();
                mePrivate = spotifyApi.getService().getMe();
                userPlaylists = spotifyApi.getService().getMyPlaylists();
            }
        }


        int offset = 0;
        assert userPlaylists != null;
        int count = userPlaylists.total;
        HashMap<String, Object> params = new HashMap<>();
        params.put("limit", 20);
        params.put("offset", 0);
        while (true) {
            for (PlaylistSimple playlistBase : userPlaylists.items) {
                //System.out.println("" + playlistBase.name);
                if (playlistBase.name.equals(playlistName)) {
                    SharedPreferences pref = context.getSharedPreferences(MyValues.PREFERENCES, MODE_PRIVATE);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userPlaylistId", playlistBase.id);  // Saving string
                    editor.putString("userPlaylistName", playlistBase.name);  // Saving string
                    editor.putString("userPlaylistOwner", playlistBase.owner.id);  // Saving string
                    editor.putInt("userPlaylistTrackTotal", playlistBase.tracks.total);  // Saving string

                    editor.apply(); // commit changes
                    return playlistBase;
                }
            }
            count -= 20;
            if (count <= 0) {
                break;

            } else {
                offset += 20;
                params.put("offset", offset);

                try {
                    userPlaylists = spotifyApi.getService().getMyPlaylists(params);
                } catch (RetrofitError e) {
                    if (e.getResponse() != null && e.getResponse().getStatus() == 401) {
                        refreshSpotifyToken();
                        mePrivate = spotifyApi.getService().getMe();
                        userPlaylists = spotifyApi.getService().getMyPlaylists(params);
                    }
                }
            }
        }

        return null;
    }

    private void createPlaylist(String playlistName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", playlistName);
        map.put("public", true);
        map.put("collaborative", false);
        Playlist playlist = null;
        try {
            playlist = spotifyApi.getService().createPlaylist(mePrivate.id, map);
        } catch (RetrofitError e) {
            if (e.getResponse() != null && e.getResponse().getStatus() == 401) {
                refreshSpotifyToken();
                mePrivate = spotifyApi.getService().getMe();
                playlist = spotifyApi.getService().createPlaylist(mePrivate.id, map);
            }
        }

        SharedPreferences pref = context.getSharedPreferences(MyValues.PREFERENCES, MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        assert playlist != null;
        editor.putString("userPlaylistId", playlist.id);  // Saving string
        editor.putString("userPlaylistName", playlist.name);  // Saving string
        editor.putString("userPlaylistOwner", playlist.owner.id);  // Saving string
        editor.putInt("userPlaylistTrackTotal", playlist.tracks.total);  // Saving string

        editor.apply(); // commit changes
    }

    public boolean playlistExist(String playlistName) {
        Pager<PlaylistSimple> userPlaylists = null;

        try {
            userPlaylists = spotifyApi.getService().getMyPlaylists();
        } catch (RetrofitError e) {
            if (e.getResponse() != null && e.getResponse().getStatus() == 401) {
                refreshSpotifyToken();
                mePrivate = spotifyApi.getService().getMe();
                userPlaylists = spotifyApi.getService().getMyPlaylists();
            }
        }


        int offset = 0;
        assert userPlaylists != null;
        int count = userPlaylists.total;
        HashMap<String, Object> params = new HashMap<>();
        params.put("limit", 20);
        params.put("offset", 0);
        while (true) {
            for (PlaylistSimple playlistBase : userPlaylists.items) {
                //System.out.println("" + playlistBase.name);
                if (playlistBase.name.equals(playlistName)) {
                    return true;
                }
            }
            count -= 20;
            if (count <= 0) {
                break;

            } else {
                offset += 20;
                params.put("offset", offset);

                try {
                    userPlaylists = spotifyApi.getService().getMyPlaylists(params);
                } catch (RetrofitError e) {
                    if (e.getResponse() != null && e.getResponse().getStatus() == 401) {
                        refreshSpotifyToken();
                        mePrivate = spotifyApi.getService().getMe();
                        userPlaylists = spotifyApi.getService().getMyPlaylists(params);
                    }
                }
            }
        }

        return false;
    }


    public void recognizedSong(String nowPlayingSong) {
        new Thread() {
            public void run() {

                try {

                    Song new_nowPlayingSong = mRepository.getItemnowPlayingSong(nowPlayingSong);

                    info_search_builder = new StringBuilder().append("");

                    /*The song was not recognize yet*/
                    if (new_nowPlayingSong == null) {

                        try {
                            Track track_searched = searchSong(nowPlayingSong);


                            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                            if (track_searched != null) {

                                StringBuilder misArtistas = new StringBuilder().append(" by:");
                                for (ArtistSimple artist : track_searched.artists) {
                                    misArtistas.append(" ").append(artist.name);
                                }


                                String puede = track_searched.name + misArtistas.toString();

                                Song new_spotifySong = mRepository.getItemstreamingSong(puede);


                                /*now playing was not detected yet*/
                                if (new_spotifySong == null) {
                                    addSongToSpotify(track_searched);
                                    mRepository.insert(new Song(nowPlayingSong, track_searched.name + misArtistas.toString(), date, getInfo_search()));
                                } else if (!new_spotifySong.getNowPlayingSong().equals("Not detected")) {
                                    addSongToSpotify(track_searched);
                                    mRepository.insert(new Song(nowPlayingSong, track_searched.name + misArtistas.toString(), date, getInfo_search()));

                                } else {


                                    mRepository.updateSong(nowPlayingSong, new_spotifySong.getStreamingName(), getInfo_search());
                                }


                            } else {
                                mRepository.insert(new Song(nowPlayingSong, "Fail to search this song", date, getInfo_search()));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }.start();

    }


    private Track searchSong(String songname) {
        Track track_searched = null;


        SharedPreferences pref = context.getSharedPreferences(MyValues.PREFERENCES, MODE_PRIVATE);
        String splitutil = pref.getString("split_util", null);         // getting String


        if (splitutil != null) {
            track_searched = supportedSearch(splitutil, songname);
        } else {
            track_searched = unsupportedSearch(songname);
        }


        return track_searched;
    }

    private Track supportedSearch(String splitutil, @NonNull String Notification) {
        Track track_searched = null;

        String[] song_name_split = Notification.split(splitutil);
        String Init_songsearched = "";


        if (song_name_split.length > 1) {
            Init_songsearched = TextUtils.join(" ", song_name_split);

            track_searched = searchSongSpotify(Init_songsearched, Init_songsearched, 50);

            if (track_searched == null) {
                track_searched = unsupportedSearch(Init_songsearched);
            }


        } else {
            track_searched = unsupportedSearch(Init_songsearched);
        }


        return track_searched;
    }

    private Track unsupportedSearch(String Notification) {
        Track track_searched = null;
        String process_song;

        process_song = Notification.replaceAll("\\p{P}", " ");
        process_song = process_song.replaceAll("(?i)feat", " ");
        process_song = process_song.replaceAll("(?i)ft", " ");
        process_song = process_song.replaceAll("(?i)remix", " ");
        process_song = process_song.replaceAll("  +", " ").trim();

        track_searched = searchSongSpotify(process_song, process_song, 50);
        if (track_searched == null) {
            String[] songsearched_splited = process_song.split(" ");

            StringBuilder buffer = new StringBuilder();
            for (String s : songsearched_splited) {
                buffer.append(s);
                track_searched = searchSongSpotify(process_song, buffer.toString(), 50);
                if (track_searched != null) {
                    break;
                }
                buffer.append(" ");
            }

        }


        return track_searched;
    }


    private Track searchSongSpotify(String notification, String songname, int limit) {

        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.OFFSET, 0);
        options.put(SpotifyService.LIMIT, limit);

        Double JaroWinklerDistance;
        TracksPager trackspager = null;
        Track mSongSpotify = null;
        int popularity;
        String Report = "My search " + limit + ": " + songname + ".";
        if (info_search_builder.length() == 0) {
            info_search_builder.append(Report);
        } else {
            info_search_builder.append("\n").append(Report);
        }
        DecimalFormat numberFormat = new DecimalFormat("#.0000");
        StringBuilder misArtistas = null;

        try {
            trackspager = spotifyApi.getService().searchTracks(songname, options);

        } catch (RetrofitError e) {
            if (e.getResponse() != null && e.getResponse().getStatus() == 401) {
                refreshSpotifyToken();
                mePrivate = spotifyApi.getService().getMe();
                mSongSpotify = searchSongSpotify(notification, songname, limit);
            }
        }

        if (trackspager != null && !trackspager.tracks.items.isEmpty()) {
            //Track track = trackspager.tracks.items.get(0);
            //System.out.println("HE ENCONTRADO ESTO   ---> en spotify --> " + track.name + " con esta búsqueda " + songname);
            JaroWinklerDistance distance = new JaroWinklerDistance();
            boolean perfect_mode = false;

            Double maxJaroWinklerDistance = 0.60;
            int maxPopularity = 10;

            int count = trackspager.tracks.total;

            if (count >= 200) {
                count = 200;
            }
            int offset = 0;

            while (true) {
                for (Track mTrackSearched : trackspager.tracks.items) {
                    misArtistas = new StringBuilder();
                    for (ArtistSimple artist : mTrackSearched.artists) {
                        misArtistas.append(" ").append(artist.name);
                    }
                    String searched = mTrackSearched.name + " " + misArtistas.toString().trim();
                    JaroWinklerDistance = distance.apply(searched, notification);

                    popularity = mTrackSearched.popularity;

                    if (JaroWinklerDistance >= maxJaroWinklerDistance) {
                        maxJaroWinklerDistance = JaroWinklerDistance;


                        if (JaroWinklerDistance >= 0.98) {
                            perfect_mode = true;

                            if (popularity >= maxPopularity || mSongSpotify == null) {
                                maxPopularity = popularity;
                                mSongSpotify = mTrackSearched;

                                Report = "*The JaroWinklerDistance: " + numberFormat.format(JaroWinklerDistance) + "\nStream: " + searched + " with a popularity " + mTrackSearched.popularity + "\nGoogle: " + notification;

                            } else if (popularity > 20) {
                                Report = "The JaroWinklerDistance: " + numberFormat.format(JaroWinklerDistance) + "\nStream: " + searched + " with a popularity " + mTrackSearched.popularity + "\nGoogle: " + notification;

                            }

                            info_search_builder.append("\n\n").append(Report);


                            //break outerloop;
                        } else if (popularity >= maxPopularity && !perfect_mode) {
                            maxPopularity = popularity;
                            mSongSpotify = mTrackSearched;
                            Report = "*The JaroWinklerDistance: " + numberFormat.format(JaroWinklerDistance) + "\nStream: " + searched + " with a popularity " + mTrackSearched.popularity + "\nGoogle: " + notification;

                            info_search_builder.append("\n\n").append(Report);


                        }
                    }
                }

                count -= 50;
                if (count <= 0) {
                    //System.out.println("La playlist " + Now_Playing_Playlist_name + " no ya está añadida");
                    break;

                } else {
                    offset += 50;
                    options.put(SpotifyService.OFFSET, offset);

                    try {
                        trackspager = spotifyApi.getService().searchTracks(songname, options);
                    } catch (RetrofitError e) {
                        if (e.getResponse() != null && e.getResponse().getStatus() == 401) {
                            refreshSpotifyToken();
                            mePrivate = spotifyApi.getService().getMe();
                            trackspager = spotifyApi.getService().searchTracks(songname, options);
                        }
                    }
                }
            }


            if (mSongSpotify != null && (mSongSpotify.name.toLowerCase().contains("tribute") || misArtistas.toString().toLowerCase().contains("tribute")) && !songname.toLowerCase().contains("tribute")) {
                mSongSpotify = null;
            }


        }

        if (info_search_builder.length() > 0) {
            info_search = info_search_builder.toString();

        }


        return mSongSpotify;
    }


    private void addSongToSpotify(Track Result_search) {

        SharedPreferences pref = context.getSharedPreferences(MyValues.PREFERENCES, MODE_PRIVATE);
        String playlistId = pref.getString("userPlaylistId", null);         // getting String
        String playlistOwner = pref.getString("userPlaylistOwner", null);         // getting String


        try {
            Map<String, Object> tracks = new HashMap<>();
            tracks.put("uris", "spotify:track:" + Result_search.id);
            spotifyApi.getService().addTracksToPlaylist(playlistOwner, playlistId, tracks, tracks);
            //spotifyApi.getService().addTracksToPlaylist(CurrentPlaylist.owner.display_name, "asssss", tracks, tracks);

        } catch (RetrofitError e) {
            if (e.getResponse() != null && e.getResponse().getStatus() == 401) {
                refreshSpotifyToken();
                mePrivate = spotifyApi.getService().getMe();
            }
        }
    }


    public void Check(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MyValues.PREFERENCES, MODE_PRIVATE);
        String SPOTIFY_USER_TOKEN = pref.getString("spotify_token", null);
        String SPOTIFY_REFRESH_TOKEN = pref.getString("spotify_refresh_token", null);

        if (SPOTIFY_USER_TOKEN != null) {

            //check for token validity
            Thread thread = new Thread() {
                public void run() {
                    Looper.prepare();
                    try {
                        spotifyApi.setAccessToken(SPOTIFY_USER_TOKEN);
                        mePrivate = spotifyApi.getService().getMe();

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("SPOTIFY_ENABLED", true);
                        editor.apply();
                        Toast.makeText(context, "Login on Spotify OK", Toast.LENGTH_SHORT).show();
                    } catch (RetrofitError e) {
                        if (e.getResponse() != null && e.getResponse().getStatus() == 401) {
                            // Log.println(Log.INFO, "[JAIME-SPOTIFY]", "Actualizing token.");
                            refreshSpotifyToken();
                            mePrivate = spotifyApi.getService().getMe();
                        }
                    }
                }
            };
            thread.start();
            try {
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void refreshSpotifyToken() {
        try {

            SharedPreferences pref = context.getSharedPreferences(MyValues.PREFERENCES, MODE_PRIVATE);
            String SPOTIFY_REFRESH_TOKEN = pref.getString("spotify_refresh_token", null);         // getting String


            URL apiUrl = new URL("https://accounts.spotify.com/api/token");
            HttpsURLConnection urlConnection = (HttpsURLConnection) apiUrl.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");

            //write POST parameters
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
            writer.write("grant_type=refresh_token&");
            writer.write("refresh_token=" + SPOTIFY_REFRESH_TOKEN + "&");
            writer.write("client_id=" + MyValues.SPOTIFY_CLIENT_ID + "&");
            writer.write("client_secret=" + "765e6769347547bfb48aa13a67f0e97a");
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();

            //System.out.println("[NpS] [AUTH-REFRESH] Result : " + urlConnection.getResponseCode() + " " + urlConnection.getResponseMessage());

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String result = reader.readLine();
            reader.close();
            result = result.substring(1);
            result = result.substring(0, result.length() - 1);
            //System.out.println("RESULT" + result);

            String[] results = result.split(",");
            for (String param : results) {
                if (param.startsWith("\"access_token\":\"")) {
                    param = param.replaceFirst("\"access_token\":\"", "");
                    param = param.replaceFirst("\"", "");
                    spotifyApi.setAccessToken(param);
                    pref = context.getSharedPreferences(MyValues.PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("spotify_token", param);
                    editor.apply();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getInfo_search() {
        return info_search;
    }
}
