package filtermusic.net;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import dagger.ObjectGraph;
import filtermusic.net.common.modules.FiltermusicModule;
import io.fabric.sdk.android.Fabric;

/**
 * Created by android on 10/19/14.
 */
public class FiltermusicApplication extends Application {

    private ObjectGraph objectGraph;

    private static FiltermusicApplication instance;

    public static FiltermusicApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        instance = this;
        buildObjectGraphAndInject();
    }

    public void buildObjectGraphAndInject() {
        objectGraph = ObjectGraph.create(new FiltermusicModule());
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

}
