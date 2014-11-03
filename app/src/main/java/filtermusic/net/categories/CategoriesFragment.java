package filtermusic.net.categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ViewFlipper;

import filtermusic.net.R;

/**
 * Created by android on 11/2/14.
 */
public class CategoriesFragment extends Fragment {

    private ListView mCategoriesList;
    private ListView mRadiosList;

    private ViewFlipper mViewFlipper;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categories_fragment, container, false);


        return rootView;
    }
}
