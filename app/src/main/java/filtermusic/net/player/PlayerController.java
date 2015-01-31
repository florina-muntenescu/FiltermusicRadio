package filtermusic.net.player;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.magic.stream.player.MetadataListener;
import com.android.magic.stream.player.StreamPlayer;
import com.android.magic.stream.player.StreamPlayerError;
import com.android.magic.stream.player.StreamPlayerFactory;
import com.android.magic.stream.player.StreamPlayerListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.R;
import filtermusic.net.common.data.DataProvider;
import filtermusic.net.common.model.Radio;

/**
 * Controller for the player
 */
public class PlayerController implements StreamPlayerListener, MetadataListener {

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

    private boolean mPlayingBuffering = false;

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
        mPlayingBuffering = true;
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
        mPlayingBuffering = false;
    }

    @Override
    public void onError(StreamPlayerError error) {
        onPlayerStop();
        Toast.makeText(mContext, R.string.unable_to_play, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlayerStop() {
        mPlayingBuffering = false;
        for (PlayerListener listener : mPlayerListeners) {
            listener.onPlayerStopped();
        }
    }

    public boolean isPlayingBuffering() {
        return mPlayingBuffering;
    }

    public String getLastPlayingTrack() {
        return mLastPlayingTrack;
    }

    @Override
    public void onMetadataChanged(@Nullable String metadata) {
        mLastPlayingTrack = metadata;
        for (PlayerListener listener : mPlayerListeners) {
            listener.onTrackChanged(metadata);
        }

    }

}
