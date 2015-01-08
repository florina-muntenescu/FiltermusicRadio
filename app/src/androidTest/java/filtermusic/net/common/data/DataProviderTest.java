package filtermusic.net.common.data;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import filtermusic.net.common.model.Radio;

/**
 * Test the data provider
 */
public class DataProviderTest extends AndroidTestCase {

    private List<Radio> generateServerRadios() {
        List<Radio> radios = new ArrayList<Radio>();

        for (int i = 0; i < 5; i++) {
            Radio radio = new Radio(
                    -1, "title" + i, "URL" + i, "genre" + i, "description" + i, "category" + i,
                    "imageUrl" + i, false, null);
            radios.add(radio);
        }
        return radios;
    }

    private List<Radio> generateDBRadios() {
        List<Radio> radios = new ArrayList<Radio>();

        for (int i = 0; i < 4; i++) {
            Radio radio;
            Date now = new Date(System.currentTimeMillis());
            if (i % 2 == 0) {
                // existing radio - same title and category - but updated values
                radio = new Radio(
                        i, "title" + i, "URL" + i, "genre" + i, "description" + i,
                        "category" + i, "imageUrl" + i, true, now);
            } else {
                // radio not part of the server radio list - has to be removed
                radio = new Radio(
                        i, "db_title" + i, "URL" + i, "genre" + i, "description" + i,
                        "category" + i, "imageUrl" + i, true, now);
            }
            radios.add(radio);
        }
        return radios;
    }

    public void testGetNewUpdatedRemovedRadios() {
        DataProvider dataProvider = new DataProvider(getContext());

        final List<Radio> serverRadios = generateServerRadios();
        final List<Radio> dbRadios = generateDBRadios();
        List<Radio> newAndUpdatedRadios = dataProvider.getNewUpdatedRadios(
                serverRadios, dbRadios);

        assertNotNull(newAndUpdatedRadios);

        assertEquals(newAndUpdatedRadios.size(), 5);
    }

    public void testRemovedRadios() {
        DataProvider dataProvider = new DataProvider(getContext());

        final List<Radio> serverRadios = generateServerRadios();
        final List<Radio> dbRadios = generateDBRadios();
        List<Radio> removedRadios= dataProvider.getRemovedRadios(
                serverRadios, dbRadios);

        assertNotNull(removedRadios);
        assertEquals(removedRadios.size(), 2);
    }
}
