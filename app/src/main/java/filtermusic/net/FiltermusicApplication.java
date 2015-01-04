package filtermusic.net;

import android.app.Application;

import dagger.ObjectGraph;
import filtermusic.net.common.modules.FiltermusicModule;

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
