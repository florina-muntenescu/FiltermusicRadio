package filtermusic.net.player;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.common.model.Radio;

/**
 * Controller for the player
 */
public class PlayerController {

    public interface PlayerListener{
        void onPlay(Radio radio);
        void onStop();
    }

    private Radio mSelectedRadio;
    private MediaPlayerService mService;
    private boolean mBound;

    private Context mContext;

    private List<IMediaPlayerServiceListener> mServiceListeners;
    private static PlayerController mInstance;


    public static PlayerController getInstance() {
        if (null == mInstance) {
            mInstance = new PlayerController();
        }
        return mInstance;
    }

    private PlayerController() {
        mContext = FiltermusicApplication.getInstance().getApplicationContext();
        mServiceListeners = new ArrayList<IMediaPlayerServiceListener>();
        bindToService();
    }

    public boolean isRadioPlaying(@NonNull final Radio radio){
        if(mSelectedRadio != null && mSelectedRadio.equals(radio)
                && mService.isPlaying()){
            return true;
        }
        return false;
    }
    public Radio getSelectedRadio() {
        return mSelectedRadio;
    }

    /**
     * Binds to the instance of MediaPlayerService. If no instance of MediaPlayerService exists, it first starts
     * a new instance of the service.
     */
    private void bindToService() {
        Intent intent = new Intent(mContext, MediaPlayerService.class);

        if (mediaPlayerServiceRunning()) {
            // Bind to Service
            mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
        //no instance of service
        else {
            //start service and bind to it
            mContext.startService(intent);
            mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        }

    }

    public void play(Radio radio){
        if(mSelectedRadio != null && radio.equals(mSelectedRadio) && !mService.isPlaying()){
            mService.startMediaPlayer();
        }else{
            newRadioSelected(radio);
        }
    }

    private void newRadioSelected(Radio radio) {
        mSelectedRadio = radio;
        mService.initializePlayer(mSelectedRadio);
    }

    public void pause() {
        if (mService.getMediaPlayer().isStarted()) {
            mService.pauseMediaPlayer();
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
            Log.d("MainActivity", "service connected");

            //bound with Service. get Service instance
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) serviceBinder;
            mService = binder.getService();

            //send this instance to the service, so it can make callbacks on this instance as a client
            mBound = true;
            for(IMediaPlayerServiceListener listener : mServiceListeners){
                mService.addListener(listener);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            mService = null;
        }
    };

    /**
     * Determines if the MediaPlayerService is already running.
     *
     * @return true if the service is running, false otherwise.
     */
    private boolean mediaPlayerServiceRunning() {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);

        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MediaPlayerService.MEDIA_PLAYER_SERVICE.equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Add a listener of this service.
     *
     * @param listener The listener of this service, which implements the  {@link IMediaPlayerServiceListener}  interface
     */
    public void addListener(IMediaPlayerServiceListener listener) {
        if(mService != null) {
            mService.addListener(listener);
        }else{
            mServiceListeners.add(listener);
        }

    }

    /**
     * Removes a listener of this service
     *
     * @param listener - The listener of this service, which implements the {@link IMediaPlayerServiceListener} interface
     */
    public void removeListener(IMediaPlayerServiceListener listener) {
        if(mService != null) {
            mService.removeListener(listener);
        }else{
            mServiceListeners.add(listener);
        }
    }

    /**
     * Closes unbinds from service, stops the service, and calls finish()
     */
    public void shutDown() {

        if (mBound) {
            mService.stopMediaPlayer();
            // Detach existing connection.
            mContext.unbindService(mConnection);
            mBound = false;
        }

        Intent intent = new Intent(mContext, MediaPlayerService.class);
        mContext.stopService(intent);

    }

    public void disconnect() {
        if (mBound) {
            mService.unRegister();
            mContext.unbindService(mConnection);
            mBound = false;
        }
    }

}
