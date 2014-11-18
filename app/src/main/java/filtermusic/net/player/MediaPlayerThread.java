package filtermusic.net.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.wifi.WifiManager;
import android.util.Log;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.common.model.Radio;


/**
 */
public class MediaPlayerThread
        extends Thread
        implements
        OnBufferingUpdateListener,
        OnInfoListener,
        OnPreparedListener,
        OnErrorListener {

    private StatefulMediaPlayer mMediaPlayer = new StatefulMediaPlayer();
    private IMediaPlayerThreadClient mClient;
    private WifiManager.WifiLock mWifiLock;

    public MediaPlayerThread(IMediaPlayerThreadClient client) {
        mClient = client;
        Context appContext = FiltermusicApplication.getInstance().getApplicationContext();
        mWifiLock = ((WifiManager) appContext.getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "filtermusic.lock");
    }

    /**
     * Initializes a StatefulMediaPlayer for streaming playback of the provided Radio
     *
     * @param station The Radio representing the station to play
     */
    public void initializePlayer(final Radio station) {
        mClient.onInitializePlayerStart();

        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.reset();
        }
        mMediaPlayer = new StatefulMediaPlayer(station);

        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.prepareAsync();

        mWifiLock.acquire();

    }

    /**
     * Initializes a StatefulMediaPlayer for streaming playback of the provided stream url
     *
     * @param streamUrl The URL of the stream to play.
     */
    public void initializePlayer(String streamUrl) {

        mMediaPlayer = new StatefulMediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(streamUrl);
        } catch (Exception e) {
            Log.e("MediaPlayerThread", "error setting data source");
            mMediaPlayer.setState(StatefulMediaPlayer.MPStates.ERROR);
        }
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.prepareAsync();
    }

    /**
     * Starts the contained StatefulMediaPlayer and foregrounds the service to support
     * persisted background playback.
     */
    public void startMediaPlayer() {
        Log.d("MediaPlayerThread", "startMediaPlayer() called");

        mMediaPlayer.start();
        mWifiLock.acquire();
    }

    /**
     * Pauses the contained StatefulMediaPlayer
     */
    public void pauseMediaPlayer() {
        Log.d("MediaPlayerThread", "pauseMediaPlayer() called");
        mMediaPlayer.pause();
        mWifiLock.release();

    }

    /**
     * Stops the contained StatefulMediaPlayer.
     */
    public void stopMediaPlayer() {
//        if(mMediaPlayer.isPlaying()) {
//            mMediaPlayer.stop();
//        }
        mMediaPlayer.release();
        mWifiLock.release();
    }

    public void resetMediaPlayer() {
        mMediaPlayer.reset();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer player, int percent) {

    }

    @Override
    public boolean onError(MediaPlayer player, int what, int extra) {
        mMediaPlayer.reset();
        mClient.onError();
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        mClient.onInitializePlayerSuccess();

    }

    /**
     * @return
     */
    public StatefulMediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

}
