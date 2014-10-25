package filtermusic.net.ui.adapters;

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
import filtermusic.net.model.Category;

/**
 * Adapter for the list of categories
 */
public class CategoriesAdapter extends ArrayAdapter<Category> {

    private LayoutInflater mLayoutInflater;

    public CategoriesAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CategoriesAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public CategoriesAdapter(Context context, int resource, Category[] objects) {
        super(context, resource, objects);
    }

    public CategoriesAdapter(Context context, int resource, int textViewResourceId, Category[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public CategoriesAdapter(Context context, int resource, List<Category> objects) {
        super(context, resource, objects);
    }

    public CategoriesAdapter(Context context, int resource, int textViewResourceId, List<Category> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mLayoutInflater.inflate(R.layout.whatever, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        holder.name.setText("John Doe");
        // etc...

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.title) TextView name;
        @InjectView(R.id.job_title)
        TextView jobTitle;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
