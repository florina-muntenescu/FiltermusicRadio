package filtermusic.net.ui.favorites;

import android.content.Context;

import java.util.List;

import filtermusic.net.common.data.DataProvider;
import filtermusic.net.common.model.Radio;

/**
 * Controls the list of favorites
 */
public class FavoritesController implements DataProvider.FavoritesRetrievedListener{

    public interface DataRetrievedListener {
        void onDataRetrieved(List<Radio> radios);
    }

    private Context mContext;
    private DataRetrievedListener mListener;
    private DataProvider mDataProvider;

    private Radio mLastSelectedRadio;
    private List<Radio> mFavorites;

    public FavoritesController(Context context) {
        mContext = context;
        mDataProvider = new DataProvider(mContext);
    }

    public void retrieveFavorites(DataRetrievedListener listener){
        mListener = listener;
        mDataProvider.retrieveFavorites(this);
    }

    @Override
    public void onFavoritesRetrieved(List<Radio> radios) {
        mFavorites = radios;
        mListener.onDataRetrieved(radios);
    }

    public Radio getLastSelectedRadio() {
        return mLastSelectedRadio;
    }

    public Radio selectRadio(int radioIndex) {
        mLastSelectedRadio = mFavorites.get(radioIndex);
        return mLastSelectedRadio;
    }
}
