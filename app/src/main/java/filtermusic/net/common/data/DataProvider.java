package filtermusic.net.common.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import filtermusic.net.common.model.Radio;

/**
 * Provides the data either from the server or from database
 * Updates the database when needed
 */
public class DataProvider {

    /**
     * Listener that notifies when different radio lists was updated
     */
    public interface RadioListRetrievedListener {

        public void onRadioListRetrieved(final List<Radio> radios);
    }

    /**
     * Listener that notifies when different radio lists was updated
     */
    public interface FavoritesRetrievedListener {

        public void onFavoritesRetrieved(final List<Radio> radios);
    }

    /**
     * Listener that notifies when different radio lists was updated
     */
    public interface LastPlayedRetrievedListener {

        public void onLastPlayedRetrieved(final List<Radio> radios);
    }

    public interface DataUpdatedListener {
        /**
         * Notifies when the data from the database has changed, so then the listeners can trigger
         * an update
         */
        public void onDataChanged();
    }

    private static final String LOG_TAG = DataProvider.class.getSimpleName();

    private Context mContext;
    private List<DataUpdatedListener> mDataUpdatedListeners;

    public DataProvider(Context context) {
        mContext = context;
        mDataUpdatedListeners = new ArrayList<DataUpdatedListener>();
    }

    /**
     * Requests the list of data from the server
     *
     * @param listener listener that is notified data has been updated.
     */
    public void provide(@NonNull final RadioListRetrievedListener listener) {
        ServerDataProvider dataProvider = new ServerDataProvider(mContext);
        dataProvider.provideRadioList(
                new RadioListRetrievedListener() {
                    @Override
                    public void onRadioListRetrieved(List<Radio> radios) {
                        updateWithDatabase(radios, listener);
                    }
                });
    }

    /**
     * Update the list of radios with the radios from the database
     *
     * @param radios   list of radios
     * @param listener notifies when the data has been updated
     */
    public void updateWithDatabase(List<Radio> radios, @NonNull final RadioListRetrievedListener
            listener) {
        // merge the list of radios with the one from the db
        DatabaseDataProvider dbProvider = new DatabaseDataProvider(mContext);
        List<Radio> dbRadios = dbProvider.provideRadioList();
        for (Radio dbRadio : dbRadios) {
            for (Radio radio : radios) {
                if (dbRadio.equals(radio)) {
                    radio.setId(dbRadio.getId());
                    radio.setFavorite(dbRadio.isFavorite());
                    radio.setPlayedDate(dbRadio.getPlayedDate());
                }
            }
        }
        listener.onRadioListRetrieved(radios);
    }

    public void registerDataListener(@NonNull final DataUpdatedListener listener) {
        mDataUpdatedListeners.add(listener);
    }

    public void unregisterDataListener(@NonNull final DataUpdatedListener listener) {
        mDataUpdatedListeners.remove(listener);
    }

    public void retrieveFavorites(final @NonNull FavoritesRetrievedListener listener) {
        DatabaseDataProvider dataProvider = new DatabaseDataProvider(mContext);
        dataProvider.provideFavoritesList(listener);
    }

    public void retrieveLastPlayed(final @NonNull LastPlayedRetrievedListener listener) {
        DatabaseDataProvider dataProvider = new DatabaseDataProvider(mContext);
        dataProvider.provideLastPlayedList(listener);
    }

    public void updateRadio(Radio radio) {
        DatabaseDataProvider dataProvider = new DatabaseDataProvider(mContext);
        dataProvider.updateRadio(
                radio, new DataUpdatedListener() {
                    @Override
                    public void onDataChanged() {
                        notifyDataUpdated();
                    }
                });
    }

    private void notifyDataUpdated() {
        for (DataUpdatedListener listener : mDataUpdatedListeners) {
            listener.onDataChanged();
        }
    }

}
