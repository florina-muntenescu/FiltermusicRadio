package filtermusic.net.ui.details;

import javax.inject.Inject;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.common.data.DataProvider;
import filtermusic.net.common.model.Radio;

/**
 * Controls the {@link filtermusic.net.ui.details.RadioDetailActivity}
 */
public class RadioDetailController implements DataProvider.RadioRetrievedListener{

    public interface RadioListener{
        public void dataChanged();
        
    }
    private Radio mRadio;

    @Inject
    DataProvider mDataProvider;
    
    private RadioListener mRadioListener;

    public RadioDetailController(final Radio radio) {

        FiltermusicApplication.getInstance().inject(this);

        mRadio = radio;
        mDataProvider.getRadioById(mRadio.getId(), this);
    }

    public Radio getRadio() {
        return mRadio;
    }

    public void setRadioAsFavorite() {
        mRadio.setFavorite(!mRadio.isFavorite());
        mDataProvider.updateRadio(mRadio);
    }

    public void registerRadioListener(RadioListener radioListener){
        mRadioListener = radioListener;
    }
    
    public void unregisterRadioListener(){
        mRadioListener = null;
        
    }
    @Override
    public void onRadioRetrieved(Radio radio) {
        mRadio = radio;
        if(mRadioListener != null){
            mRadioListener.dataChanged();
        }
    }
}
