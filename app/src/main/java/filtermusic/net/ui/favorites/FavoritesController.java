package filtermusic.net.ui.favorites;

import java.util.List;

import javax.inject.Inject;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.common.data.DataProvider;
import filtermusic.net.common.model.Radio;

/**
 * Controls the list of favorites
 */
public class FavoritesController implements DataProvider.FavoritesRetrievedListener,
        DataProvider.DataUpdatedListener {

    public interface DataRetrievedListener {
        void onDataRetrieved(List<Radio> radios);
    }

    private DataRetrievedListener mListener;

    @Inject
    DataProvider mDataProvider;

    private Radio mLastSelectedRadio;
    private List<Radio> mFavorites;

    public FavoritesController() {
        FiltermusicApplication.getInstance().inject(this);
        mDataProvider.registerDataListener(this);
    }

    public void retrieveFavorites(DataRetrievedListener listener) {
        mListener = listener;
        if (mFavorites == null) {
            mDataProvider.retrieveFavorites(this);
        } else {
            mListener.onDataRetrieved(mFavorites);
        }
    }

    @Override
    public void onFavoritesRetrieved(List<Radio> radios) {
        mFavorites = radios;
        mListener.onDataRetrieved(radios);
    }

    @Override
    public void onDataChanged() {
        mDataProvider.retrieveFavorites(this);
    }

    public Radio selectRadio(int radioIndex) {
        mLastSelectedRadio = mFavorites.get(radioIndex);
        return mLastSelectedRadio;
    }
    
    public void unregisterListener(){
        mDataProvider.unregisterDataListener(this);
    }
        
}
