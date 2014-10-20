package filtermusic.net.communication;

import android.content.Context;
import android.support.annotation.NonNull;

import filtermusic.net.parser.FeedXmlParser;
import filtermusic.net.util.DebugUtil;
import retrofit.RestAdapter;

/**
 * Created by android on 10/18/14.
 */
public class FiltermusicApi {

    private static final RestAdapter.LogLevel DEBUG_DEFAULT_LOG_LEVEL = RestAdapter.LogLevel.FULL;

    private Context mApplicationContext;

    public FiltermusicApi(Context context) {
        mApplicationContext = context;

    }

    public FiltermusicService createFromRestAdapter() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://filtermusic.net")
                .setConverter(new FeedXmlParser())
                .build();

        setRestAdapterLogLevel(restAdapter);

        FiltermusicService service = restAdapter.create(FiltermusicService.class);
        return service;
    }

    private void setRestAdapterLogLevel(final @NonNull RestAdapter restAdapter) {
        if (DebugUtil.isDebugMode(mApplicationContext)) {
            // use a special log level only when in debug mode
            restAdapter.setLogLevel(DEBUG_DEFAULT_LOG_LEVEL);
        }
    }
}
