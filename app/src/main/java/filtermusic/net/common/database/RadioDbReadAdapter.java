package filtermusic.net.common.database;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

import javax.inject.Inject;

import filtermusic.net.common.model.Radio;

/**
 * Provides interface to read radios from db
 */
public class RadioDbReadAdapter {
    private DbRadioController mDbRadioController;

    @Inject
    public RadioDbReadAdapter(DbRadioController dbRadioController) {
        mDbRadioController = dbRadioController;
    }

    public List<Radio> queryRadioList() {
        List<DbRadio> dbRadios = mDbRadioController.getRadioList();
        List<Radio> radios = toRadios(dbRadios);
        return radios;
    }


    /**
     * Retrieve from the database the fields marked as favorite asynchronously
     */
    public List<Radio> queryFavoriteRadios() {
        List<DbRadio> dbRadios = mDbRadioController.getFavorites();
        return toRadios(dbRadios);
    }

    public Radio queryRadioById(final int radioId) {
        DbRadio dbRadio = mDbRadioController.getRadioById(radioId);
        return toRadio(dbRadio);
    }

    /**
     * Retrieve from the database all the radios ordered by their play date
     */
    public List<Radio> queryLastPlayed() {
        List<DbRadio> dbRadios = mDbRadioController.getLastPlayedList();
        return toRadios(dbRadios);
    }

    /**
     * Converts a list of {@link DbRadio} to {@link Radio}
     */
    private List<Radio> toRadios(final List<DbRadio> dbRadios) {
        return Lists.transform(dbRadios, new Function<DbRadio, Radio>() {
            @Override
            public Radio apply(DbRadio dbRadio) {
                return toRadio(dbRadio);
            }
        });
    }

    /**
     * Converts a {@link DbRadio} to {@link Radio}
     *
     * @param dbRadio database radio that gets converted
     * @return the {@link filtermusic.net.common.model.Radio} object
     */
    private Radio toRadio(final DbRadio dbRadio) {
        return new Radio(
                dbRadio.getId(), dbRadio.getTitle(), dbRadio.getURL(), dbRadio.getGenre(),
                dbRadio.getDescription(), dbRadio.getCategory(), dbRadio.getImageUrl(),
                dbRadio.isFavorite(), dbRadio.getPlayedDate());
    }
}
