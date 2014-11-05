package filtermusic.net.categories;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import filtermusic.net.R;

/**
 * View holding the details for a radio and that gives the user the possibility of playing that radio
 * and saving the radio as favorite
 */
public class RadioDetailView extends LinearLayout{
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

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.radio_detail_view, this, true);
    }
}
