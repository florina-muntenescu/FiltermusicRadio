package filtermusic.net.common.data;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import filtermusic.net.common.communication.FiltermusicService;
import filtermusic.net.common.database.RadioDbReadAdapter;
import filtermusic.net.common.database.RadioDbWriteAdapter;
import filtermusic.net.common.model.Radio;

/**
 *  Provides methods to synchronize the database
 */
public class RadioSynchronizer {
    
    private final RadioDbReadAdapter mRadioDbReadAdapter;
    private final RadioDbWriteAdapter mRadioDbWriteAdapter;
    
    private final FiltermusicService mFiltermusicService;

    public RadioSynchronizer(RadioDbReadAdapter radioDbReadAdapter, 
                             RadioDbWriteAdapter radioDbWriteAdapter, 
                             FiltermusicService filtermusicService) {
        mRadioDbReadAdapter = radioDbReadAdapter;
        mRadioDbWriteAdapter = radioDbWriteAdapter;
        mFiltermusicService = filtermusicService;
    }

    /**
     * Synchronizes the database by creating or updating a list of radios and deleting another list
     * or radios
     * @return the entire list of radios from the database
     */
    public List<Radio> sync(){
        List<Radio> serverRadios = mFiltermusicService.getRadiosSync();
        List<Radio> dbRadios = mRadioDbReadAdapter.queryRadioList();

        final List<Radio> dbRadiosRemoved = getRemovedRadios(serverRadios, dbRadios);
        final List<Radio> newOrUpdatedRadios = getNewUpdatedRadios(
                serverRadios, dbRadios);
        
        mRadioDbWriteAdapter.deleteCollection(dbRadiosRemoved);
        mRadioDbWriteAdapter.createOrUpdateCollection(newOrUpdatedRadios);
        
       return mRadioDbReadAdapter.queryRadioList();
    }

    /**
     * Get the radios that were removed from the server radios 
     * @param radios
     * @param dbRadios
     * @return
     */
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
        List<Radio> updatedRadios = new ArrayList<Radio>();
        for (Radio dbRadio : dbRadios) {
            Radio existingRadio = null;
            for (Radio radio : radios) {
                // if title and category are equal, we consider the radios equal
                if (radio.getTitle().equals(dbRadio.getTitle()) && radio.getGenre().equals
                        (dbRadio.getGenre())) {
                    existingRadio = radio;
                    if(!(radio.getDescription().equals(dbRadio.getDescription()) &&
                            radio.getCategory().equals(dbRadio.getCategory()) &&
                            radio.getImageUrl().equals(dbRadio.getImageUrl()) &&
                            radio.getURL().equals(dbRadio.getURL()))){
                        updatedRadios.add(radio);
                    }
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
        for (Radio radio : updatedRadios) {
            Radio dbRadio = existingRadiosMap.get(radio);
            radio.setId(dbRadio.getId());
            radio.setFavorite(dbRadio.isFavorite());
            radio.setPlayedDate(dbRadio.getPlayedDate());
            newOrUpdatedRadios.add(radio);
        }

        return newOrUpdatedRadios;
    }
    
}
