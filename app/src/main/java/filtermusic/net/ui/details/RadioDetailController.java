package filtermusic.net.ui.details;

import javax.inject.Inject;

import filtermusic.net.common.data.DataProvider;
import filtermusic.net.common.model.Radio;

/**
 * Controls the {@link filtermusic.net.ui.details.RadioDetailActivity}
 */
public class RadioDetailController {

    private Radio mRadio;

    @Inject
    DataProvider mDataProvider;

    public RadioDetailController(Radio radio) {
        mRadio = radio;
    }

    public Radio getRadio() {
        return mRadio;
    }

    public void setRadioAsFavorite() {
        mRadio.setFavorite(!mRadio.isFavorite());
        mDataProvider.updateRadio(mRadio);
    }
}
