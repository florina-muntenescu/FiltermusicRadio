package filtermusic.net.categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import filtermusic.net.R;
import filtermusic.net.common.model.Category;

/**
 * Created by android on 11/2/14.
 */
public class CategoriesFragment extends Fragment implements CategoriesController.DataListener {

    private static final String LOG_TAG = CategoriesFragment.class.getSimpleName();

    private static final String STATE_VIEW_INDEX = "VIEW_INDEX";
    private static final int PROGRESS_VIEW_INDEX = 0;
    private static final int CATEGORIES_VIEW_INDEX = 1;
    private static final int RADIOS_VIEW_INDEX = 2;

    private ListView mCategoriesList;
    private ListView mRadiosList;

    private ViewFlipper mViewFlipper;

    private CategoriesController mCategoriesController;

    private List<Category> mCategories = new ArrayList<Category>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categories_fragment, container, false);


        mCategoriesController = CategoriesController.getInstance();
        mCategoriesController.setDataListener(this);

        if (!mCategoriesController.getCategories().isEmpty()) {
            onCategoriesUpdated(mCategoriesController.getCategories());
        }
        return rootView;
    }

    private void setupUI(View rootView) {
        mViewFlipper = (ViewFlipper) rootView.findViewById(R.id.view_flipper);
        mCategoriesList = (ListView) rootView.findViewById(R.id.categories_list);
        mRadiosList = (ListView) rootView.findViewById(R.id.radio_list);

        mCategoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = mCategories.get(position);
                
            }
        });
    }

    @Override
    public void onCategoriesUpdated(List<Category> categories) {

        updateCategories(categories);
        if (PROGRESS_VIEW_INDEX == mViewFlipper.getDisplayedChild()) {
            flipToPage(CATEGORIES_VIEW_INDEX);
        }
    }

    private void updateCategories(List<Category> categories) {
        mCategories = categories;
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getActivity(), R.layout.category_item, mCategories);
        mCategoriesList.setAdapter(categoriesAdapter);
    }

    private void flipToPage(int pageIndex) {
        mViewFlipper.setDisplayedChild(pageIndex);
    }


}
