package filtermusic.net.categories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import filtermusic.net.R;
import filtermusic.net.common.model.Radio;

/**
 * View holding the details for a radio and that gives the user the possibility of playing that radio
 * and saving the radio as favorite
 */
public class RadioDetailView extends LinearLayout{

    private ImageView mRadioImage;
    private TextView mRadioTitle;
    private TextView mRadioDescription;

    private Radio mRadio;

    private Context mContext;

    public RadioDetailView(Context context) {
        super(context);
        initUI(context);
    }

    public RadioDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI(context);
    }

    public RadioDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initUI(context);
    }


    private void initUI(final Context context){
        setOrientation(VERTICAL);

        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.radio_detail_view, this, true);

        mRadioImage = (ImageView) rootView.findViewById(R.id.radio_image);
        mRadioTitle = (TextView) rootView.findViewById(R.id.radio_title);
        mRadioDescription = (TextView) rootView.findViewById(R.id.radio_description);
    }

    public void setRadio(@NonNull Radio radio){
        mRadio = radio;
        updateUI();
    }

    private void updateUI(){

        Picasso.with(mContext)
                .load(mRadio.getImageUrl())
                .placeholder(R.drawable.station_image)
                .into(mRadioImage);

        mRadioTitle.setText(mRadio.getTitle());
        mRadioDescription.setText(mRadio.getDescription());
    }
}
