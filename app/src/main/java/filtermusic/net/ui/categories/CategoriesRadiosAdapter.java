package filtermusic.net.ui.categories;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import filtermusic.net.R;
import filtermusic.net.common.model.Radio;

/**
 * Adapter for the list of radios displayed in the categories view
 */
/*package*/ class CategoriesRadiosAdapter extends ArrayAdapter<Radio> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public CategoriesRadiosAdapter(Context context, int resource, List<Radio> objects) {
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
        holder.radioInfo.setText(Html.fromHtml(radio.getDescription()));

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.radio_title)
        TextView title;
        @InjectView(R.id.radio_info)
        TextView radioInfo;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
