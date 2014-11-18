package filtermusic.net.player;

import filtermusic.net.common.model.Radio;

/**
 * Listener that reacts on different states of the player service
 */
public interface IMediaPlayerServiceListener {

    /**
     * A callback made by a MediaPlayerService onto its listeners to indicate that a player is initializing.
     */
    public void onInitializePlayerStart(Radio radio);

    /**
     * A callback made by a MediaPlayerService onto its listeners to indicate that a player was successfully initialized.
     */
    public void onInitializePlayerSuccess();

    /**
     *  A callback made by a MediaPlayerService onto its listeners to indicate that a player encountered an error.
     */
    public void onError();
}