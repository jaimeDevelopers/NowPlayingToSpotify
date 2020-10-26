package com.jaime.addtracksspotifynowplaying;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jaime.addtracksspotifynowplaying.ui.activities.NotificationPermission;
import com.jaime.addtracksspotifynowplaying.ui.activities.SettingsActivity;
import com.jaime.addtracksspotifynowplaying.ui.activities.adapter.ScreenSlidePagerAdapter;

import five.star.me.FiveStarMe;


public class MainActivity extends AppCompatActivity {



    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mTopToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(mTopToolbar);


        ViewPager2 viewPager2 = findViewById(R.id.viewPager2Id);
        TabLayout tabLayout = findViewById(R.id.tabs);

        FragmentStateAdapter ScreeSlide = new ScreenSlidePagerAdapter(this);
        viewPager2.setAdapter(ScreeSlide);

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(TAB_TITLES[position]);
                    }
                }).attach();


        //System.out.println("El resultado esssssssss " + Build.VERSION.SDK_INT);
        //System.out.println("El resultado es " + Build.VERSION.SDK_INT);

        try {
            FiveStarMe.with(getApplicationContext())
                    .setInstallDays(10) // default 10, 0 means install day.
                    .setLaunchTimes(10) // default 10
                    .setDebug(true) // default false
                    .monitor();

            // Show a dialog if meets conditions
            FiveStarMe.showRateDialogIfMeetsConditions(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        NotificationPermission notificationPermission = new NotificationPermission(getApplicationContext());

        if (!notificationPermission.isNotificationServiceEnabled(this)) {
            AlertDialog enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }


    }

    //
    private AlertDialog buildNotificationServiceAlertDialog() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Open settings");
        alertDialogBuilder.setMessage("Click on the gear and follow the steps:\n1º connect to spotify (The login API is slow, sorry)\n2º Enable the notification listener\n*Please, review in Google Play");

        alertDialogBuilder.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Toast.makeText(getApplicationContext(), "Remmenber, connect to spotify and enable the notificacion listener (go settings)", Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {

            Intent detailIntent = new Intent(this, SettingsActivity.class);
            this.startActivity(detailIntent);

            //fragment = com.jaime.addtracksspotifynowplaying.ui.main.Settings.newInstance();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//
//    private boolean isNotificationServiceEnabled() {
//        String pkgName = getPackageName();
//        final String flat = Settings.Secure.getString(getContentResolver(),
//                MyValues.ENABLED_NOTIFICATION_LISTENERS);
//        if (!TextUtils.isEmpty(flat)) {
//            final String[] names = flat.split(":");
//            for (String name : names) {
//                final ComponentName cn = ComponentName.unflattenFromString(name);
//                if (cn != null) {
//                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

}