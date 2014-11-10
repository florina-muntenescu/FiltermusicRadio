package filtermusic.net.common.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import filtermusic.net.common.communication.FiltermusicApi;
import filtermusic.net.common.model.Radio;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Provides the data either from the server or from database
 * Updates the database when needed
 */
public class DataProvider {
    private static final String LOG_TAG = DataProvider.class.getSimpleName();

    public interface DataUpdatedListener {
        public void onRadioListUpdated(final List<Radio> radios);

        public void onFavoritesUpdated(final List<Radio> radios);

        public void onLastPlayedUpdated(final List<Radio> radios);
    }

    private Context mContext;

    public DataProvider(Context context) {
        mContext = context;
    }

    public void getDataFromServer(final @NonNull DataUpdatedListener listener) {
        FiltermusicApi api = new FiltermusicApi(mContext);
        Observable<List<Radio>> apiObservable = api.createFromRestAdapter().getRadios();
        apiObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Radio>>() {
                    @Override
                    public void onCompleted() {
                        // nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<Radio> radios) {
                        Log.d(LOG_TAG, "radios: " + radios.size());
                        listener.onRadioListUpdated(ImmutableList.copyOf(radios));
                    }
                });

    }

    public void retrieveFavorites(final @NonNull DataUpdatedListener listener) {
        listener.onFavoritesUpdated(new ArrayList<Radio>());
    }

    public void retrieveLastPlayed(final @NonNull DataUpdatedListener listener) {
        listener.onLastPlayedUpdated(new ArrayList<Radio>());
    }

}
