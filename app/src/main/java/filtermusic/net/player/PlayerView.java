package filtermusic.net.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import filtermusic.net.R;
import filtermusic.net.common.model.Radio;

/**
 * View that contains the player
 */
public class PlayerView extends LinearLayout implements IMediaPlayerServiceListener {

    private PlayerController mPlayerController;
    private Context mContext;
    private Radio mRadio;

    private ImageView mRadioImage;
    private TextView mRadioTitle;
    private ImageView mPlayPauseButton;


    public PlayerView(Context context) {
        super(context);
        init(context);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public PlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context context) {
        mContext = context;

        mPlayerController = PlayerController.getInstance();
        mPlayerController.addListener(this);

        View rootView = LayoutInflater.from(context).inflate(R.layout.player_view, this, true);

        mRadioImage = ButterKnife.findById(rootView, R.id.radio_image);
        mRadioTitle = ButterKnife.findById(rootView, R.id.radio_title);
        mPlayPauseButton = ButterKnife.findById(rootView, R.id.play_pause_button);

        mPlayPauseButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if(mPlayerController.isRadioPlaying(mRadio)){
                           mPlayerController.pause();
                       }else{
                           mPlayerController.play(mRadio);
                       }
                    }
                });

        if (mPlayerController.getSelectedRadio() != null) {
            setRadio(mPlayerController.getSelectedRadio());
        }
    }

    public void setRadio(Radio radio) {
        mRadio = radio;
        mRadioTitle.setText(mRadio.getTitle());
        Picasso.with(mContext).load(mRadio.getImageUrl()).placeholder(R.drawable.station_image)
                .into(mRadioImage);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPlayerController.removeListener(this);
    }

    @Override
    public void onInitializePlayerStart(Radio radio) {
        setRadio(radio);

    }

    @Override
    public void onPlaying(Radio radio) {
        setRadio(radio);
        mPlayPauseButton.setImageResource(R.drawable.pause);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onStop() {
        mPlayPauseButton.setImageResource(R.drawable.play_arrow);
    }
}
