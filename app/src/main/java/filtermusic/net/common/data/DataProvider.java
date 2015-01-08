package filtermusic.net.common.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                        Observable<List<Radio>> radiosObservable = requestFromServerAndSync(radios);
                        radiosObservable.subscribeOn(Schedulers.newThread()).observeOn(
                                AndroidSchedulers.mainThread()).subscribe(
                                new Subscriber<List<Radio>>() {
                                    @Override
                                    public void onCompleted() {
                                        // nothing
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }

                                    @Override
                                    public void onNext(List<Radio> radios) {
                                        listener.onRadioListRetrieved(radios);
                                    }
                                });

                    }
                });

    }

    private Observable<List<Radio>> requestFromServerAndSync(final List<Radio> radios) {
        return Observable.create(
                new Observable.OnSubscribe<List<Radio>>() {
                    @Override
                    public void call(final Subscriber<? super List<Radio>> observer) {
                        List<Radio> radioList = syncDatabaseAndServerData(radios);
                        observer.onNext(radioList);
                        observer.onCompleted();
                    }
                });
    }

    private List<Radio> syncDatabaseAndServerData(@NonNull final List<Radio> serverRadios) {
        DatabaseDataProvider dbProvider = new DatabaseDataProvider(mContext);
        final List<Radio> dbRadios = dbProvider.provideRadioList();

        final List<Radio> dbRadiosRemoved = getRemovedRadios(serverRadios, dbRadios);
        final List<Radio> newOrUpdatedRadios = getNewUpdatedRadios(
                serverRadios, dbRadios);
        dbProvider.createOrUpdateCollection(newOrUpdatedRadios);
        dbProvider.deleteCollection(dbRadiosRemoved);

        return dbProvider.provideRadioList();
    }

    protected List<Radio> getRemovedRadios(@NonNull final List<Radio> radios,
            @NonNull final List<Radio> dbRadios) {
        List<Radio> dbRadiosRemoved = new ArrayList<Radio>();
        for (Radio dbRadio : dbRadios) {
            Radio existingRadio = null;
            for (Radio radio : radios) {
                // if title and category are equal, we consider the radios equal
                if (radio.getTitle().equals(dbRadio.getTitle()) && radio.getCategory().equals
                        (dbRadio.getCategory())) {
                    existingRadio = radio;
                    break;
                }
            }
            if (existingRadio == null) {
                // the radio has been removed
                dbRadiosRemoved.add(dbRadio);
            }
        }

        return dbRadiosRemoved;
    }

    /**
     * Returns the list of radios that are not in the database or that have been updated
     * Making the method protected to test it
     */
    protected List<Radio> getNewUpdatedRadios(@NonNull final List<Radio> radios,
            @NonNull final List<Radio> dbRadios) {
        List<Radio> newOrUpdatedRadios = new ArrayList<Radio>(radios);

        // radios that have been retrieved from the BE, exist in the database but we don't know
        // their ID and the radio from the database
        HashMap<Radio, Radio> existingRadiosMap = new HashMap<Radio, Radio>();
        for (Radio dbRadio : dbRadios) {
            Radio existingRadio = null;
            for (Radio radio : radios) {
                // if title and category are equal, we consider the radios equal
                if (radio.getTitle().equals(dbRadio.getTitle()) && radio.getCategory().equals
                        (dbRadio.getCategory())) {
                    existingRadio = radio;
                    break;
                }
            }
            if (existingRadio != null) {
                existingRadiosMap.put(existingRadio, dbRadio);
            }
        }

        // if from the list of radios we remove all the radios that exist already
        // we get the list of new radios
        newOrUpdatedRadios.removeAll(existingRadiosMap.keySet());

        //for every updated radio, update the id, is favorite and last play date
        // according to the value from the database
        // like this we don't loose the info that we saved in the DB
        // add the key set of the existing map to the list of new radios.
        for (Radio radio : existingRadiosMap.keySet()) {
            Radio dbRadio = existingRadiosMap.get(radio);
            radio.setId(dbRadio.getId());
            radio.setFavorite(dbRadio.isFavorite());
            radio.setPlayedDate(dbRadio.getPlayedDate());
            newOrUpdatedRadios.add(radio);
        }

        return newOrUpdatedRadios;
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
