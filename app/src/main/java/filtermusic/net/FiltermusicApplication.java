package filtermusic.net;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

import dagger.ObjectGraph;
import filtermusic.net.common.modules.FiltermusicModule;
import io.fabric.sdk.android.Fabric;

/**
 * Application class for this app
 */
public class FiltermusicApplication extends Application {

    public enum TrackerName {
        APP_TRACKER
    }

    private ObjectGraph mObjectGraph;

    private static FiltermusicApplication mInstance;

    Map<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public static FiltermusicApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mInstance = this;
        buildObjectGraphAndInject();
    }

    public void buildObjectGraphAndInject() {
        mObjectGraph = ObjectGraph.create(new FiltermusicModule());
    }

    public void inject(Object o) {
        mObjectGraph.inject(o);
    }

    synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(R.xml.app_tracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }
}
