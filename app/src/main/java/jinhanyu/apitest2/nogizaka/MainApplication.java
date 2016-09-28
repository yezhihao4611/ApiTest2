package jinhanyu.apitest2.nogizaka;

import android.app.Application;
import android.content.Context;

/**
 * Created by anzhuo on 2016/9/27.
 */

public class MainApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
