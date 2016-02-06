package svce.ac.in.svcetransport;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.LocationListener;

/**
 * Created by tchinmai on 6/2/16.
 */
public class MonitorService extends Service {
SharedPreferences settings;
    SharedPreferences.Editor editor;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        addLocationListener();
        settings= PreferenceManager.getDefaultSharedPreferences(this);
        editor=settings.edit();
        editor.putBoolean("locationService", true);
        editor.apply();
        return  START_STICKY;
    }
    @Override
    public void onDestroy() {
        settings= PreferenceManager.getDefaultSharedPreferences(this);
        editor=settings.edit();
        editor.putBoolean("locationService", false);
        editor.apply();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    LocationManager lm;
    Thread triggerService;

    private void addLocationListener() {
        triggerService = new Thread(new Runnable() {
            public void run() {
                try {
                    Looper.prepare();//Initialise the current thread as a looper.
                    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    Criteria c = new Criteria();
                    c.setAccuracy(Criteria.ACCURACY_COARSE);

                    final String PROVIDER = lm.getBestProvider(c, true);

                    MyLocationListener myLocationListener = new MyLocationListener();
                    if (ActivityCompat.checkSelfPermission(BusApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BusApplication.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                   else lm.requestLocationUpdates(PROVIDER, 600000, 0, (android.location.LocationListener) myLocationListener);
                   Log.d("LOC_SERVICE", "Service RUNNING!");
                    Looper.loop();
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }, "LocationThread");
        triggerService.start();
    }

    public static void updateLocation(Location location)
    {
        Context appCtx = BusApplication.context;

        double latitude, longitude;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Intent filterRes = new Intent();
        filterRes.setAction("svce.ac.in.svcetransport.intent.action.LOCATION");
        filterRes.putExtra("latitude", latitude);
        filterRes.putExtra("longitude", longitude);
        appCtx.sendBroadcast(filterRes);
    }


    class MyLocationListener implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location)
        {
            updateLocation(location);
        }
    }
}
