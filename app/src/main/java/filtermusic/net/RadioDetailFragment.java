package filtermusic.net;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

import filtermusic.net.common.model.Radio;
import filtermusic.net.categories.CategoriesController;


/**
 * A fragment representing a single Radio detail screen.
 * This fragment is either contained in a {@link CategoriesListActivity}
 * in two-pane mode (on tablets) or a {@link RadioDetailActivity}
 * on handsets.
 */
public class RadioDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The radio this fragment is presenting.
     */
    private Radio mItem;

    private Button.OnClickListener mOnClickListener;
    private MediaPlayer mMediaPlayer;
    private Context mContext;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RadioDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            if(!CategoriesController.getInstance().getRadiosForFirstCatgory().isEmpty()) {
                mItem = CategoriesController.getInstance().getRadiosForFirstCatgory().get(getArguments().getInt(ARG_ITEM_ID));
            }
        }
        mContext = getActivity().getBaseContext();

        mMediaPlayer = new MediaPlayer();
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                } else {
                    startPlaying();
                }
            }
        };
    }

    private void startPlaying() {
        try {
            mMediaPlayer.reset();


            mMediaPlayer.setDataSource(mItem.getURL());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mMediaPlayer.prepareAsync();

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                    MediaMetadataRetriever mMediaMetaDataRetriever = new MediaMetadataRetriever();
                    mMediaMetaDataRetriever.setDataSource(mItem.getURL(), new HashMap<String, String>());
                    String albumName = mMediaMetaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    String artistName = mMediaMetaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String titleName = mMediaMetaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                    Log.d(RadioDetailFragment.class.getSimpleName(), albumName + " " + artistName + " " + titleName);
                }
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("Buffering", "" + percent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_radio_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.radio_detail)).setText(mItem.getDescription());
            ((Button) rootView.findViewById(R.id.play)).setOnClickListener(mOnClickListener);
        }

        return rootView;
    }
}
