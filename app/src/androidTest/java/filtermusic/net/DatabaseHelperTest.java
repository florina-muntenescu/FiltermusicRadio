package filtermusic.net;

import android.test.AndroidTestCase;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.Date;
import java.util.List;

import filtermusic.net.common.database.DatabaseHelper;
import filtermusic.net.common.database.DbRadio;
import filtermusic.net.common.model.Radio;

/**
 * Created by android on 12/15/14.
 */
public class DatabaseHelperTest extends AndroidTestCase {

    public void testCRUD() throws Exception {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

        Radio radio = new Radio(
                -1, "title", "url", "genre", "description", "category", null, false, null);
        DbRadio dbRadio = new DbRadio(radio);
        int insertedRows = dao.create(dbRadio);

        assertEquals(insertedRows, 1);

        List<DbRadio> radios = dao.queryForAll();

        assertEquals(radios.size(), 1);
        assertEquals(radios.get(0).getDescription(), radio.getDescription());

        int deletedRows = dao.delete(radios);
        assertEquals(deletedRows, 1);

        List<DbRadio> radiosAfterDelete = dao.queryForAll();
        assertEquals(radiosAfterDelete.size(), 0);
    }


    public void testStar() throws Exception {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

        Radio radio = new Radio(
                -1, "title", "url", "genre", "description", "category", null, false, null);
        DbRadio dbRadio = new DbRadio(radio);
        int insertedRows = dao.create(dbRadio);

        assertEquals(insertedRows, 1);

        dbRadio.setFavorite(true);
        dao.update(dbRadio);

        List<DbRadio> radios = dao.queryForAll();

        assertEquals(radios.size(), 1);
        assertEquals(radios.get(0).isFavorite(), true);
    }

    public void testLastPlayed() throws Exception {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

        Radio radio = new Radio(
                -1, "title", "url", "genre", "description", "category", null, false, null);
        DbRadio dbRadio = new DbRadio(radio);
        int insertedRows = dao.create(dbRadio);

        assertEquals(insertedRows, 1);

        Date date = new Date(System.currentTimeMillis());

        dbRadio.setPlayedDate(date);
        dao.update(dbRadio);

        List<DbRadio> radios = dao.queryForAll();

        assertEquals(radios.size(), 1);
        assertEquals(radios.get(0).getPlayedDate(), date);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();
        List<DbRadio> radios = dao.queryForAll();
        dao.delete(radios);
    }
}
