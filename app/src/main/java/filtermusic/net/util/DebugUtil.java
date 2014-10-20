package filtermusic.net.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by android on 10/18/14.
 */
public class DebugUtil {

    public static boolean isDebugMode(Context context) {
        boolean debug = false;
        PackageInfo packageInfo = null;

        if (context == null) {
            return false;
        }

        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            final int flags = packageInfo.applicationInfo.flags;
            debug = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
        return debug;
    }
}
