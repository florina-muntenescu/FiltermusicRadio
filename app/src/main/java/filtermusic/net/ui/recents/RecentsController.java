package filtermusic.net.ui.recents;

import java.util.List;

import javax.inject.Inject;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.common.data.DataProvider;
import filtermusic.net.common.model.Radio;

/**
 * Controls the list of recently played radios
 */
public class RecentsController implements DataProvider.LastPlayedRetrievedListener, DataProvider.DataUpdatedListener {

    public interface DataRetrievedListener {
        void onDataRetrieved(List<Radio> radios);
    }

    private DataRetrievedListener mListener;

    @Inject
    DataProvider mDataProvider;

    private Radio mLastSelectedRadio;
    private List<Radio> mLastPlayed;

    public RecentsController() {
        FiltermusicApplication.getInstance().inject(this);
        mDataProvider.registerDataListener(this);
    }

    public void retrieveRecents(DataRetrievedListener listener) {
        mListener = listener;
        mDataProvider.retrieveLastPlayed(this);
    }

    @Override
    public void onLastPlayedRetrieved(List<Radio> radios) {
        mLastPlayed = radios;
        mListener.onDataRetrieved(radios);
    }

    @Override
    public void onDataChanged() {
        mDataProvider.retrieveLastPlayed(this);
    }

    public Radio getLastSelectedRadio() {
        return mLastSelectedRadio;
    }

    public Radio selectRadio(int radioIndex) {
        mLastSelectedRadio = mLastPlayed.get(radioIndex);
        return mLastSelectedRadio;
    }
}
