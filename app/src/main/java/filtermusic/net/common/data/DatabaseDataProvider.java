package filtermusic.net.common.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
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
        List<Radio> radios = convert(dbRadios);

        return radios;
    }

    private List<Radio> convert(final List<DbRadio> dbRadios) {
        List<Radio> radios = new ArrayList<Radio>();
        for (DbRadio dbRadio : dbRadios) {
            final Radio radio = new Radio(
                    dbRadio.getId(), dbRadio.getTitle(), dbRadio.getURL(), dbRadio.getGenre(),
                    dbRadio.getDescription(), dbRadio.getCategory(), dbRadio.getImageUrl(),
                    dbRadio.isFavorite(), dbRadio.getPlayedDate());
            radios.add(radio);
        }

        return radios;
    }

    /**
     * Retrieve from the database the fields marked as favorite asynchronously
     *
     * @param listener notifies the requestor object that the list of favorites was retrieved
     */
    public void provideFavoritesList(@NonNull final DataProvider.FavoritesRetrievedListener
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
                        List<Radio> radios = convert(dbRadios);
                        listener.onFavoritesRetrieved(radios);
                    }
                });
    }

    private Observable<List<DbRadio>> getFavorites() {
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
        }catch (SQLException exception){
            exception.printStackTrace();
        }
        
        List<DbRadio> dbRadios = null;
        try {
            dbRadios = dao.query(queryBuilder.prepare());
            List<Radio> radios = convert(dbRadios);
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
                        listener.onDataChanged();
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
