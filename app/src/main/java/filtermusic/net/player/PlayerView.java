package filtermusic.net.player;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import filtermusic.net.common.model.Radio;

/**
 * View that contains the player
 */
public class PlayerView extends LinearLayout implements IMediaPlayerServiceListener {

    private PlayerController mPlayerController;
    private Context mContext;
    private Radio mRadio;


    public PlayerView(Context context) {
        super(context);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init(final Context context){
        mContext = context;

        mPlayerController = PlayerController.getInstance();
        mPlayerController.addListener(this);


    }

    @Override
    public void onInitializePlayerStart() {

    }

    @Override
    public void onInitializePlayerSuccess() {

    }

    @Override
    public void onError() {

    }
}
