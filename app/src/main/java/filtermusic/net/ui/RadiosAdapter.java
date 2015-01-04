package filtermusic.net.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import filtermusic.net.R;
import filtermusic.net.common.model.Radio;

/**
 * Adapter for the list of radios
 */
public class RadiosAdapter extends ArrayAdapter<Radio> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public RadiosAdapter(Context context, int resource, List<Radio> objects) {
        super(context, resource, objects);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mLayoutInflater.inflate(R.layout.radio_list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        Radio radio = getItem(position);
        holder.title.setText(radio.getTitle());

        if(radio.getPlayedDate() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            final String dateString = formatter.format(radio.getPlayedDate());
            holder.lastPlayed.setText(mContext.getString(R.string.last_played, dateString));
            holder.lastPlayed.setVisibility(View.VISIBLE);
        }else{
            holder.lastPlayed.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.radio_title)
        TextView title;
        @InjectView(R.id.last_played)
        TextView lastPlayed;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
