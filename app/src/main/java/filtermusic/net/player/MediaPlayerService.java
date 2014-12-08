package filtermusic.net.player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import filtermusic.net.common.model.Radio;


/**
 * An extension of android.app.Service class which provides management to a MediaPlayerThread.<br>
 */
public class MediaPlayerService
        extends Service
        implements IMediaPlayerThreadClient {

    public static final String MEDIA_PLAYER_SERVICE = "filtermusic.net.player.MediaPlayerService";

    private MediaPlayerThread mMediaPlayerThread = new MediaPlayerThread(this);
    private final Binder mBinder = new MediaPlayerBinder();
    private Radio mRadio;

    private List<IMediaPlayerServiceListener> mListeners = new ArrayList<IMediaPlayerServiceListener>();

    @Override
    public void onCreate() {
        mMediaPlayerThread.start();
    }

    /**
     * A class for clients binding to this service. The client will be passed an object of this class
     * via its onServiceConnected(ComponentName, IBinder) callback.
     */
    public class MediaPlayerBinder extends Binder {
        /**
         * Returns the instance of this service for a client to make method calls on it.
         *
         * @return the instance of this service.
         */
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }

    }

    /**
     * Returns the contained StatefulMediaPlayer
     *
     * @return
     */
    public StatefulMediaPlayer getMediaPlayer() {
        return mMediaPlayerThread.getMediaPlayer();
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }


    /**
     * Add a listener of this service.
     *
     * @param listener The listener of this service, which implements the  {@link IMediaPlayerServiceListener}  interface
     */
    public void addListener(IMediaPlayerServiceListener listener) {
        mListeners.add(listener);
    }

    /**
     * Removes a listener of this service
     *
     * @param listener - The listener of this service, which implements the {@link IMediaPlayerServiceListener} interface
     */
    public void removeListener(IMediaPlayerServiceListener listener) {
        mListeners.remove(listener);
    }


    public void initializePlayer(final Radio radio) {
        mRadio = radio;
        mMediaPlayerThread.initializePlayer(radio);
    }

    public void startMediaPlayer() {
        mMediaPlayerThread.startMediaPlayer();
    }

    public boolean isPlaying(){
        StatefulMediaPlayer player = mMediaPlayerThread.getMediaPlayer();
        return player!= null && player.isStarted();
    }

    /**
     * Pauses playback
     */
    public void pauseMediaPlayer() {
        Log.d("MediaPlayerService", "pauseMediaPlayer() called");
        mMediaPlayerThread.pauseMediaPlayer();
        stopForeground(true);

    }

    /**
     * Stops playback
     */
    public void stopMediaPlayer() {
        stopForeground(true);
        mMediaPlayerThread.stopMediaPlayer();
    }

    public void resetMediaPlayer() {
        stopForeground(true);
        mMediaPlayerThread.resetMediaPlayer();
    }


    @Override
    public void onError() {
        for (IMediaPlayerServiceListener client : mListeners) {
            client.onError();
        }
    }

    @Override
    public void onInitializePlayerStart() {
        startMediaPlayer();
    }

    @Override
    public void onPlaying() {
        for (IMediaPlayerServiceListener client : mListeners) {
            client.onPlaying(mRadio);
        }
    }

    @Override
    public void onStop() {
        for (IMediaPlayerServiceListener client : mListeners) {
            client.onStop();
        }
    }

    public void unRegister() {
        this.mListeners.clear();
    }

    /**
     * This is called if the service is currently running and the user has removed a
     * task that comes from the service's application.
     * Stop the service when the task is removed
     *
     * @param rootIntent
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(MediaPlayerService.class.getSimpleName(), "on task removed");
        stopMediaPlayer();
        this.unRegister();
    }
}