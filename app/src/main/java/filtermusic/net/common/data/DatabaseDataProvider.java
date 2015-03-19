package filtermusic.net.common.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import filtermusic.net.common.database.DatabaseHelper;
import filtermusic.net.common.database.DbRadio;
import filtermusic.net.common.model.Radio;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Provides data from the database
 */
/*package*/ class DatabaseDataProvider {

    private static final String LOG_TAG = DatabaseDataProvider.class.getSimpleName();

    private Context mContext;

    DatabaseDataProvider(Context context) {
        this.mContext = context;
    }

    public List<Radio> provideRadioList() {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

        List<DbRadio> dbRadios = dao.queryForAll();
        List<Radio> radios = toRadios(dbRadios);
        return radios;
    }

    /**
     * Converts a list of {@link DbRadio} to {@link Radio}
     */
    private List<Radio> toRadios(final List<DbRadio> dbRadios) {
        return Lists.transform(dbRadios, new Function<DbRadio, Radio>() {
            @Override
            public Radio apply(DbRadio dbRadio) {
                return new Radio(
                        dbRadio.getId(), dbRadio.getTitle(), dbRadio.getURL(), dbRadio.getGenre(),
                        dbRadio.getDescription(), dbRadio.getCategory(), dbRadio.getImageUrl(),
                        dbRadio.isFavorite(), dbRadio.getPlayedDate());
            }
        });
    }

    /**
     * Retrieve from the database the fields marked as favorite asynchronously
     *
     * @param listener notifies the requestor object that the list of favorites was retrieved
     */
    public void getFavoritesList(@NonNull final DataProvider.FavoritesRetrievedListener
                                         listener) {
        Observable<List<DbRadio>> favoritesObservable = getFavorites();
        favoritesObservable.subscribeOn(Schedulers.newThread()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(
                new Subscriber<List<DbRadio>>() {
                    @Override
                    public void onCompleted() {
                        // nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<DbRadio> dbRadios) {
                        List<Radio> radios = toRadios(dbRadios);
                        listener.onFavoritesRetrieved(radios);
                    }
                });
    }

    public Observable<List<DbRadio>> getFavorites() {
        return Observable.create(
                new Observable.OnSubscribe<List<DbRadio>>() {
                    @Override
                    public void call(Subscriber<? super List<DbRadio>> observer) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
                        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

                        List<DbRadio> dbRadios = dao.queryForEq(
                                DbRadio.IS_FAVORITE_FILED_NAME, true);
                        observer.onNext(dbRadios);
                        observer.onCompleted();
                    }
                });
    }

    public Observable<Radio> getRadioById(final int radioId) {
        return Observable.create(
                new Observable.OnSubscribe<Radio>() {
                    @Override
                    public void call(Subscriber<? super Radio> observer) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
                        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

                        DbRadio dbRadio = dao.queryForId(radioId);
                        Radio radio = new Radio(
                                dbRadio.getId(), dbRadio.getTitle(), dbRadio.getURL(), dbRadio.getGenre(),
                                dbRadio.getDescription(), dbRadio.getCategory(), dbRadio.getImageUrl(),
                                dbRadio.isFavorite(), dbRadio.getPlayedDate());
                        observer.onNext(radio);
                        observer.onCompleted();
                    }
                });

    }

    /**
     * Synchronizes the database by creating or updating a list of radios and deleting another list
     * or radios
     *
     * @param radiosToCreateOrUpdate radios that are created or updated in case they are already in
     *                               the database
     * @param radiosToDelete         radios that are removed from the database
     * @return the entire list of radios from the database
     */
    public List<Radio> syncDatabase(@NonNull final List<Radio> radiosToCreateOrUpdate,
                                    @NonNull final List<Radio> radiosToDelete) {
        Log.d(LOG_TAG, "to create/update " + radiosToCreateOrUpdate.size()
                + " to delete " + radiosToDelete.size());
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();
        // create or update radios
        for (Radio radio : radiosToCreateOrUpdate) {
            dao.createOrUpdate(new DbRadio(radio));
        }
        // delete radios
        List<DbRadio> dbRadios = new ArrayList<DbRadio>();
        for (Radio radio : radiosToDelete) {
            dbRadios.add(new DbRadio(radio));
        }
        dao.delete(dbRadios);

        final List<DbRadio> allDBRadios = dao.queryForAll();
        return toRadios(allDBRadios);
    }

    /**
     * Creates or updates a collection of radios syncronous
     *
     * @param radios the radios that are updated or created in the database
     */
    public void createOrUpdateCollection(@NonNull final List<Radio> radios) {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();
        for (Radio radio : radios) {
            dao.createOrUpdate(new DbRadio(radio));
        }
    }

    /**
     * Deletes from the database all the elements of the collection
     *
     * @param radios the collection that is removed
     */
    public void deleteCollection(@NonNull final List<Radio> radios) {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

        List<DbRadio> dbRadios = new ArrayList<DbRadio>();
        for (Radio radio : radios) {
            dbRadios.add(new DbRadio(radio));
        }
        dao.delete(dbRadios);
    }

    /**
     * Retrieve from the database all the fields ordered by their play date
     *
     * @param listener notifies the requestor object that the list of last played was retrieved
     */
    public void provideLastPlayedList(final DataProvider.LastPlayedRetrievedListener listener) {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

        QueryBuilder<DbRadio, Integer> queryBuilder = dao.queryBuilder();
        queryBuilder.orderBy(DbRadio.PLAYED_DATE_FILED_NAME, false);

        try {
            Where<DbRadio, Integer> where = queryBuilder.where();
            where.isNotNull(DbRadio.PLAYED_DATE_FILED_NAME);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        List<DbRadio> dbRadios = Collections.emptyList();
        try {
            dbRadios = dao.query(queryBuilder.prepare());
            List<Radio> radios = toRadios(dbRadios);
            listener.onLastPlayedRetrieved(radios);
        } catch (SQLException e) {
            e.printStackTrace();

            listener.onLastPlayedRetrieved(new ArrayList<Radio>());
        }
    }

    public void updateRadio(@NonNull final Radio radio, @NonNull final DataProvider
            .DataUpdatedListener listener) {
        final DbRadio dbRadio = new DbRadio(radio);
        Observable<Integer> updateObservable = updateRadio(dbRadio);
        final Subscription updateSubscription = updateObservable.subscribeOn(
                Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Action1<Integer>() {
                    @Override
                    public void call(final Integer elapsedSeconds) {
                        // nothing to do
                        Log.d(LOG_TAG, "radio updated");
                        if (listener != null) {
                            listener.onDataChanged();
                        }
                    }
                });
    }

    /**
     * Creates or updated the radio in the database
     *
     * @param radio radio that is updated or created
     * @return observable containing the number of lines that were modified
     */
    private Observable<Integer> updateRadio(@NonNull final DbRadio radio) {
        return Observable.create(
                new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> observer) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
                        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

                        Dao.CreateOrUpdateStatus status = dao.createOrUpdate(radio);
                        observer.onNext(status.getNumLinesChanged());
                        observer.onCompleted();
                    }
                });
    }

}
