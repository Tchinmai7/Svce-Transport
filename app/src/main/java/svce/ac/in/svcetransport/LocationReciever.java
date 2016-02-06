package svce.ac.in.svcetransport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by tchinmai on 6/2/16.
 */
public class LocationReciever extends BroadcastReceiver {

    double latitude, longitude;

    @Override
    public void onReceive(final Context context, final Intent calledIntent)
    {
        Log.d("LOC_RECEIVER", "Location RECEIVED!");

        latitude = calledIntent.getDoubleExtra("latitude", -1);
        longitude = calledIntent.getDoubleExtra("longitude", -1);

        updateRemote(latitude, longitude);

    }

    private void updateRemote(final double latitude, final double longitude )
    {
            new Sender().execute();
    }
    class Sender extends AsyncTask<Void,Void,Void>
    {
        String url="http://requestb.in/1i5slpu1";
        String json;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                HttpGet httpget = new HttpGet(url);
                HttpClient httpclient = new DefaultHttpClient();
                httpget.setHeader("Accept", "application/json");
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entityres = response.getEntity();
                json = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
