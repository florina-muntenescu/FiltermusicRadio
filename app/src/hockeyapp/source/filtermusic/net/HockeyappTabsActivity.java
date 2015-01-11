package filtermusic.net;

import android.os.Bundle;
import android.util.Log;

import net.hockeyapp.android.Tracking;
import net.hockeyapp.android.UpdateManager;

public class HockeyappTabsActivity extends TabsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Hockey", HockeyappTabsActivity.class.getSimpleName());
        handleHockeyApi();
    }

    @Override
    protected void onPause() {
        Tracking.stopUsage(this);
        UpdateManager.unregister();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DebugUtil.checkForCrashes(this, null);
        Tracking.startUsage(this);
    }

    private void checkForUpdates() {
        // only check for updates when in release mode, not in hockey mode
        if (BuildConfig.HOCKEY_APP_ENABLED) {
            UpdateManager.register(this, BuildConfig.HOCKEY_APP_TOKEN);
        }
    }

    private void handleHockeyApi() {
        checkForUpdates();
        DebugUtil.checkForCrashes(this, null);
    }
}
