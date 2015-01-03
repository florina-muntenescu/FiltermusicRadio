package filtermusic.net.common.data;

import android.content.Context;
import android.support.annotation.NonNull;

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

    private static final String LOG_TAG = DataProvider.class.getSimpleName();

    private Context mContext;

    public DataProvider(Context context) {
        mContext = context;
    }

    /**
     * Requests the list of data from the server
     *
     * @param listener listener that is notified data has been updated.
     */
    public void provide(final @NonNull RadioListRetrievedListener listener) {
        ServerDataProvider dataProvider = new ServerDataProvider(mContext);
        dataProvider.provideRadioList(listener);
    }

    public void retrieveFavorites(final @NonNull FavoritesRetrievedListener listener) {
        DatabaseDataProvider dataProvider = new DatabaseDataProvider(mContext);
        dataProvider.provideFavoritesList(listener);
    }

    public void retrieveLastPlayed(final @NonNull LastPlayedRetrievedListener listener) {
        DatabaseDataProvider dataProvider = new DatabaseDataProvider(mContext);
        dataProvider.provideLastPlayedList(listener);
    }

    public void setRadioAsFavorite(Radio radio) {
        radio.setFavorite(true);
        updateRadio(radio);
    }

    public void updatePlayDate(Radio radio) {
        Date date = new Date(System.currentTimeMillis());
        radio.setPlayedDate(date);
        updateRadio(radio);
    }

    public void updateRadio(Radio radio) {
        DatabaseDataProvider dataProvider = new DatabaseDataProvider(mContext);
        dataProvider.updateRadio(radio);
    }
}
