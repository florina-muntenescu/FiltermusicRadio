package filtermusic.net;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.google.common.io.CharSource;

import net.hockeyapp.android.Constants;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;

import filtermusic.net.BuildConfig;
import filtermusic.net.R;

public class DebugUtil {

    public static boolean checkForCrashes(final Context context, final @Nullable String userEmailAddress) {
        // only check for crashes when in release mode, not in debug mode
        if (!BuildConfig.HOCKEY_APP_ENABLED) {
            return false;
        }
        
        //beware this is the net.hockeyapp.android.Constants
        Constants.loadFromContext(context);
        final WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
        if (CrashManager.hasStackTraces(contextWeakReference) > 0) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(context);
            
            alert.setTitle(R.string.crashreporter_crashes_found_alertview_title);
            alert.setMessage(R.string.crashreporter_crashes_found_alertview_message);
            
            // Set an EditText view to get user input
            final EditText input = new EditText(context);
            input.setText(R.string.crashreporter_crashes_found_alert_input_default_text);
            input.setSelection(0, input.getText().length());
            alert.setView(input);
            
            alert.setPositiveButton(R.string.crashreporter_crashes_found_alertview_button_send, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    final String value = input.getText().toString();
                    registerForCrashes(context, userEmailAddress, value, true);
                }
            });
            alert.setNegativeButton(R.string.crashreporter_crashes_found_alertview_button_dont_send, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    CrashManager.deleteStackTraces(contextWeakReference);
                    registerForCrashes(context, userEmailAddress, null, false);
                }
            });
            
            alert.show();
            return true;
        } else {
            registerForCrashes(context, userEmailAddress, null, false);
            return false;
        }
    }

    public static boolean isDebugMode(Context context) {
        boolean debug = false;
        PackageInfo packageInfo = null;
        
        if (context == null) {
            return false;
        }
        
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (final NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            final int flags = packageInfo.applicationInfo.flags;
            debug = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
        return debug;
    }
    
    private static void registerForCrashes(Context context, final @Nullable String userEmailAddress, final @Nullable String userDescription, final boolean userConfirmed) {
        if (BuildConfig.HOCKEY_APP_ENABLED) {
            CrashManager.register(context, BuildConfig.HOCKEY_APP_TOKEN, new CrashManagerListener() {

                @Override
                public String getContact() {
                    return userEmailAddress;
                }

                @Override
                public String getDescription() {
                    final StringBuilder log = new StringBuilder();
                    log.append(R.string.crashreporter_crashes_found_alertview_title);
                    if (userDescription != null) {
                        log.append("The user provided the following input:\n");
                        log.append(userDescription);
                    }

                    try {
                        final CharSource logcatCharSource = new CharSource() {
                            @Override
                            public Reader openStream() throws IOException {
                                final Process logcatProcess = Runtime.getRuntime().exec("logcat -v time -t 200 ");
                                return new InputStreamReader(logcatProcess.getInputStream());
                            }
                        };

                        log.append(logcatCharSource.read());
                    } catch (final IOException iOException) {
                        log.append("--- I/O exception occurred while reading logcat output to add to Hockey crash description: ").append(iOException.getMessage());
                    }

                    return log.toString();
                }

                @Override
                public boolean ignoreDefaultHandler() {
                    return userConfirmed;
                }
            });

        }
    }
    
}
