package filtermusic.net.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import filtermusic.net.R;
import filtermusic.net.common.model.Category;

/**
 * Adapter for the list of categories
 */
/*package*/ class CategoriesAdapter extends ArrayAdapter<Category> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public CategoriesAdapter(Context context, int resource) {
        super(context, resource);
        init(context);
    }

    public CategoriesAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        init(context);
    }

    public CategoriesAdapter(Context context, int resource, Category[] objects) {
        super(context, resource, objects);
        init(context);
    }

    public CategoriesAdapter(Context context, int resource, int textViewResourceId, Category[] objects) {
        super(context, resource, textViewResourceId, objects);
        init(context);
    }

    public CategoriesAdapter(Context context, int resource, List<Category> objects) {
        super(context, resource, objects);
        init(context);
    }

    public CategoriesAdapter(Context context, int resource, int textViewResourceId, List<Category> objects) {
        super(context, resource, textViewResourceId, objects);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mLayoutInflater.inflate(R.layout.category_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        final Category category = getItem(position);
        holder.name.setText(category.getGenre());
        final String noStations = mContext.getString(R.string.no_stations, category.getRadioList().size());
        holder.stations.setText(noStations);
        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.title) TextView name;
        @InjectView(R.id.stations) TextView stations;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
