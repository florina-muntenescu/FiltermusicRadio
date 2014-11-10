package filtermusic.net;

import android.app.Application;

/**
 * Created by android on 10/19/14.
 */
public class FiltermusicApplication extends Application {

    private static FiltermusicApplication instance;

    public static FiltermusicApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
