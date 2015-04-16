package filtermusic.net.common.data;

import java.util.Date;
import java.util.List;

import filtermusic.net.common.database.DbRadio;

/**
 * Tests the {@link filtermusic.net.common.database.DbRadioController}
 */
public class DbRadioControllerTest extends DbRadioBaseTest {

    private DbRadio mDbRadio = new DbRadio(1, "title", "URL", "genre", "description",
            "category", "imageUrl", true, new Date());
    private DbRadio mDbRadioNotPlayedNotFave = new DbRadio(2, "title2", "URL2", "genre2", 
            "description2",
            "category2", "imageUrl2", true, null);


    public void testAddRadio(){
        mController.addOrUpdateRadio(mDbRadio);
        
        DbRadio radio = mController.getRadioById(mDbRadio.getId());
        assertEquals(radio, mDbRadio);
    }
    
    public void testGetFavorite(){
        mController.addOrUpdateRadio(mDbRadio);
        mController.addOrUpdateRadio(mDbRadioNotPlayedNotFave);
        
        List<DbRadio> fave = mController.getFavorites();
        assertEquals(fave.size(), 1);
        assertEquals(fave.get(0), mDbRadio);
        
    }
    
    public void testGetLastPlayed(){
        mController.addOrUpdateRadio(mDbRadio);
        mController.addOrUpdateRadio(mDbRadioNotPlayedNotFave);

        List<DbRadio> lastPlayed = mController.getFavorites();
        assertEquals(lastPlayed.size(), 1);
        assertEquals(lastPlayed.get(0), mDbRadio);

    }
}
