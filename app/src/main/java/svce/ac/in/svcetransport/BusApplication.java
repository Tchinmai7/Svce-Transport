package svce.ac.in.svcetransport;

import android.app.Application;
import android.content.Context;

/**
 * Created by tchinmai on 6/2/16.
 */
public class BusApplication extends Application {
    public static Context context;
    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public Context getAppContext()
    {
        return getApplicationContext();
    }
}
