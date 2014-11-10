package filtermusic.net.player;

/**
 * Created by android on 11/10/14.
 */

import android.media.AudioManager;
import android.util.Log;

import java.io.IOException;

import filtermusic.net.common.model.Radio;

/**
 * A subclass of android.media.MediaPlayer which provides methods for
 * state-management, data-source management, etc.
 */
public class StatefulMediaPlayer extends android.media.MediaPlayer {
    /**
     * Set of states for StatefulMediaPlayer:<br>
     * EMPTY, CREATED, PREPARED, STARTED, PAUSED, STOPPED, ERROR
     */
    public enum MPStates {
        EMPTY, CREATED, PREPARED, STARTED, PAUSED, STOPPED, ERROR
    }

    private MPStates mState;
    private Radio mRadio;

    /**
     * @return the mRadio
     */
    public Radio getRadio() {
        return mRadio;
    }

    /**
     * Sets a StatefulMediaPlayer's data source as the provided Radio
     *
     * @param Radio the Radio to set as the data source
     */
    public void setRadio(Radio Radio) {
        this.mRadio = Radio;
        try {
            setDataSource(mRadio.getURL());
            setState(MPStates.CREATED);
        } catch (Exception e) {
            Log.e("StatefulMediaPlayer", "");
            setState(MPStates.ERROR);
        }
    }

    /**
     * Instantiates a StatefulMediaPlayer object.
     */
    public StatefulMediaPlayer() {
        super();
        setState(MPStates.CREATED);
    }

    /**
     * Instantiates a StatefulMediaPlayer object with the Audio Stream Type
     * set to STREAM_MUSIC and the provided Radio's URL as the data source.
     *
     * @param Radio The Radio to use as the data source
     */
    public StatefulMediaPlayer(Radio Radio) {
        super();
        this.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mRadio = Radio;
        try {
            setDataSource(mRadio.getURL());
            setState(MPStates.CREATED);
        } catch (Exception e) {
            Log.e("statefulMediaPlayer", "");
            setState(MPStates.ERROR);
        }
    }

    /* (non-Javadoc)
     * @see android.media.MediaPlayer#reset()
     */
    @Override
    public void reset() {
        super.reset();
        this.mState = MPStates.EMPTY;
    }

    /* (non-Javadoc)
     * @see android.media.MediaPlayer#start()
     */
    @Override
    public void start() {
        super.start();
        setState(MPStates.STARTED);
    }

    /* (non-Javadoc)
     * @see android.media.MediaPlayer#pause()
     */
    @Override
    public void pause() {

        super.pause();
        setState(MPStates.PAUSED);

    }

    /* (non-Javadoc)
     * @see android.media.MediaPlayer#stop()
     */
    @Override
    public void stop() {
        super.stop();
        setState(MPStates.STOPPED);
    }

    /* (non-Javadoc)
     * @see android.media.MediaPlayer#release()
     */
    @Override
    public void release() {
        super.release();
        setState(MPStates.EMPTY);
    }

    /* (non-Javadoc)
     * @see android.media.MediaPlayer#prepare()
     */
    @Override
    public void prepare() throws IOException, IllegalStateException {
        super.prepare();
        setState(MPStates.PREPARED);
    }

    /* (non-Javadoc)
     * @see android.media.MediaPlayer#prepareAsync()
     */
    @Override
    public void prepareAsync() throws IllegalStateException {
        super.prepareAsync();
        setState(MPStates.PREPARED);
    }

    /**
     * @return
     */
    public MPStates getState() {
        return mState;
    }

    /**
     * @param state the state to set
     */
    public void setState(MPStates state) {
        this.mState = state;
    }

    /**
     * @return
     */
    public boolean isCreated() {
        return (mState == MPStates.CREATED);
    }

    public boolean isEmpty() {
        return (mState == MPStates.EMPTY);
    }

    public boolean isStopped() {
        return (mState == MPStates.STOPPED);
    }

    public boolean isStarted() {
        return (mState == MPStates.STARTED || this.isPlaying());
    }

    public boolean isPaused() {
        return (mState == MPStates.PAUSED);
    }

    public boolean isPrepared() {
        return (mState == MPStates.PREPARED);
    }
}