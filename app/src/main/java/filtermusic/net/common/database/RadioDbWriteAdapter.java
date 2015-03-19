package filtermusic.net.common.database;

import android.support.annotation.NonNull;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import filtermusic.net.common.model.Radio;

/**
 * Provides interface for writing data in db
 */
public class RadioDbWriteAdapter {

    private DbRadioController mDbRadioController;

    @Inject
    public RadioDbWriteAdapter(DbRadioController dbRadioController) {
        mDbRadioController = dbRadioController;
    }

    /**
     * Creates or updates a collection of radios syncronous
     *
     * @param radios the radios that are updated or created in the database
     */
    public void createOrUpdateCollection(@NonNull final List<Radio> radios) {
        mDbRadioController.runInTransaction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (Radio radio : radios) {
                    createOrUpdateRadio(radio);
                }
                return null;
            }
        });
    }

    /**
     * Deletes from the database all the elements of the collection
     *
     * @param radios the collection that is removed
     */
    public void deleteCollection(@NonNull final List<Radio> radios) {
        mDbRadioController.runInTransaction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                List<DbRadio> dbRadios = toDbRadios(radios);
                mDbRadioController.deleteRadios(dbRadios);
                return null;
            }
        });
    }


    public void createOrUpdateRadio(@NonNull final Radio radio) {
        DbRadio dbRadio = toDbRadio(radio);
        mDbRadioController.addOrUpdateRadio(dbRadio);
    }

    private List<DbRadio> toDbRadios(List<Radio> radios) {
        return Lists.transform(radios, new Function<Radio, DbRadio>() {
            @Override
            public DbRadio apply(Radio radio) {
                return toDbRadio(radio);
            }
        });

    }


    /**
     * Converts a {@link Radio} to a {@link DbRadio}
     */
    private DbRadio toDbRadio(Radio radio) {
        return new DbRadio(radio.getId(), radio.getTitle(), radio.getURL(), radio.getGenre(),
                radio.getDescription(), radio.getCategory(), radio.getImageUrl(),
                radio.isFavorite(), radio.getPlayedDate());
    }

}
