package svce.ac.in.svcetransport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings= PreferenceManager.getDefaultSharedPreferences(this);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, Login.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 3000);
    }

    private void checkLocationService()
    {
        Log.d("CHECK_SERVICE", "Service running: " + (settings.getBoolean("locationService", false) ? "YES" : "NO"));

        if(settings.getBoolean("locationService", false))
            return;

        Intent mServiceIntent = new Intent(this, MonitorService.class);
        startService(mServiceIntent);
    }
}
