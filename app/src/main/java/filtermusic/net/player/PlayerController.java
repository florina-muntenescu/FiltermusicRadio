package filtermusic.net.player;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.magic.stream.player.StreamPlayer;
import com.android.magic.stream.player.StreamPlayerError;
import com.android.magic.stream.player.StreamPlayerFactory;
import com.android.magic.stream.player.StreamPlayerListener;
import com.android.magic.stream.player.TrackListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TooManyListenersException;

import javax.inject.Inject;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.R;
import filtermusic.net.common.data.DataProvider;
import filtermusic.net.common.model.Radio;

/**
 * Controller for the player
 */
public class PlayerController implements StreamPlayerListener, TrackListener {

    private static final String LOG_TAG = PlayerController.class.getSimpleName();

    public interface PlayerListener {
        void onPlayerStartedPlaying(Radio radio);

        void onPlayerStopped();

        void onTrackChanged(final String track);

        void onError();
    }

    @Inject
    DataProvider mDataProvider;

    private Radio mRadioPlaying;
    private Radio mSelectedRadio;
    private String mLastPlayingTrack;

    private StreamPlayer mPlayer;

    private Context mContext;

    private static PlayerController mInstance;

    private List<PlayerListener> mPlayerListeners;

    public static PlayerController getInstance() {
        if (null == mInstance) {
            mInstance = new PlayerController();
        }
        return mInstance;
    }

    private PlayerController() {
        FiltermusicApplication.getInstance().inject(this);

        mContext = FiltermusicApplication.getInstance().getApplicationContext();
        mPlayer = StreamPlayerFactory.getStreamPlayerInstance(mContext);

        mPlayer.registerStreamPlayerListener(this);
        mPlayer.registerTrackListener(this);

        mPlayerListeners = new ArrayList<>();
    }

    /**
     * Check if a radio was played in this session of the app
     *
     * @return true if a radio was selected
     */
    public boolean radioWasPlayedInThisSession() {
        return mSelectedRadio != null;
    }

    public boolean isRadioPlaying(@NonNull final Radio radio) {
        if (mRadioPlaying != null && mRadioPlaying.equals(radio)
                && mPlayer.getPlayingUrl() != null) {
            return true;
        }
        return false;
    }

    public Radio getSelectedRadio() {
        return mSelectedRadio;
    }

    public void play(Radio radio) {
        mPlayer.play(radio.getURL());
        mSelectedRadio = radio;
        final Date now = new Date(System.currentTimeMillis());
        radio.setPlayedDate(now);
        mDataProvider.updateRadio(radio);
    }

    public void stop() {
        mPlayer.stop();
    }


    public void pause() {
        mPlayer.pause();
    }


    /**
     * Add a listener of this service.
     *
     * @param listener The listener of player, which implements the  {@link
     *                 PlayerListener}  interface
     */
    public void addListener(PlayerListener listener) {
        mPlayerListeners.add(listener);

    }

    /**
     * Removes a listener of this service
     *
     * @param listener - The listener of this service, which implements the {@link
     *                 PlayerListener} interface
     */
    public void removeListener(PlayerListener listener) {
        mPlayerListeners.remove(listener);
    }

    @Override
    public void onPlaying(String radioUrl) {
        mRadioPlaying = mSelectedRadio;
        for (PlayerListener listener : mPlayerListeners) {
            listener.onPlayerStartedPlaying(mRadioPlaying);
        }
    }

    @Override
    public void onError(StreamPlayerError error) {
        onPlayerStop();
        Toast.makeText(mContext, R.string.unable_to_play, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlayerStop() {
        for (PlayerListener listener : mPlayerListeners) {
            listener.onPlayerStopped();
        }
    }

    public String getLastPlayingTrack() {
        return mLastPlayingTrack;
    }

    @Override
    public void onTrackChanged(@Nullable String track) {
        mLastPlayingTrack = track;
        for (PlayerListener listener : mPlayerListeners) {
            listener.onTrackChanged(track);
        }

    }

}
