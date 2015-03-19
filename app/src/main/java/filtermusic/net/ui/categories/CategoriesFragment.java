package filtermusic.net.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import filtermusic.net.R;
import filtermusic.net.common.model.Category;
import filtermusic.net.common.model.Radio;
import filtermusic.net.player.PlayerController;
import filtermusic.net.ui.details.RadioDetailActivity;

/**
 * Holds a view flipper where the user can browse between categories and radios corresponding to
 * categories
 */
public class CategoriesFragment extends Fragment implements CategoriesController.DataListener {

    private static final String LOG_TAG = CategoriesFragment.class.getSimpleName();

    private static final String LAST_OPENED_VIEW = "last_opened_view";

    private static final int PROGRESS_VIEW_INDEX = 0;
    private static final int CATEGORIES_VIEW_INDEX = 1;
    private static final int RADIOS_VIEW_INDEX = 2;
    private static final int ERROR_VIEW_INDEX = 3;

    private ListView mCategoriesList;
    private ListView mRadiosList;

    private ViewFlipper mViewFlipper;

    private CategoriesController mCategoriesController;

    private List<Category> mCategories = new ArrayList<Category>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categories_fragment, container, false);

        setupUI(rootView);

        mCategoriesController = CategoriesController.getInstance();
        mCategoriesController.setDataListener(this);

        if (!mCategoriesController.getCategories().isEmpty()) {
            onCategoriesUpdated(mCategoriesController.getCategories());
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(LAST_OPENED_VIEW)) {
            int lastOpenedView = savedInstanceState.getInt(LAST_OPENED_VIEW);
            flipToPage(lastOpenedView);

        }
        return rootView;
    }

    private void setupUI(final View rootView) {
        mViewFlipper = (ViewFlipper) rootView.findViewById(R.id.view_flipper);
        mCategoriesList = (ListView) rootView.findViewById(R.id.categories_list);
        mRadiosList = (ListView) rootView.findViewById(R.id.radios_list);

        rootView.findViewById(R.id.retry_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoriesController.syncRadios();
                flipToPage(PROGRESS_VIEW_INDEX);
            }
        });
        mCategoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Radio> radios = mCategoriesController.selectCategory(
                        mCategories.get(position));
                updateRadioList(radios);

                flipToPage(RADIOS_VIEW_INDEX);
            }
        });

        mRadiosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Radio selectedRadio = mCategoriesController.selectRadio(position);
                if (!PlayerController.getInstance().radioWasPlayedInThisSession()) {
                    final String selectedRadioGson = new Gson().toJson(selectedRadio);
                    Intent radioDetailIntent = new Intent(
                            getActivity(), RadioDetailActivity.class);
                    radioDetailIntent.putExtra(
                            RadioDetailActivity.INTENT_RADIO_PLAYING, selectedRadioGson);
                    startActivity(radioDetailIntent);
                }
                PlayerController.getInstance().play(selectedRadio);
            }
        });
    }

    private void updateRadioList(List<Radio> radios) {
        CategoriesRadiosAdapter radiosAdapter = new CategoriesRadiosAdapter(getActivity(),
                R.layout.radio_list_item, radios);

        mRadiosList.setAdapter(radiosAdapter);
    }

    @Override
    public void onCategoriesUpdated(List<Category> categories) {
        updateCategories(categories);
        if (PROGRESS_VIEW_INDEX == mViewFlipper.getDisplayedChild()) {
            flipToPage(CATEGORIES_VIEW_INDEX);
        }
    }

    @Override
    public void onError() {
        flipToPage(ERROR_VIEW_INDEX);
    }

    private void updateCategories(List<Category> categories) {
        mCategories = categories;
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getActivity(),
                R.layout.category_item, mCategories);
        mCategoriesList.setAdapter(categoriesAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mViewFlipper != null) {
            outState.putInt(LAST_OPENED_VIEW, mViewFlipper.getDisplayedChild());
        }
    }

    private void flipToPage(int pageIndex) {
        mViewFlipper.setDisplayedChild(pageIndex);
        updateContent(pageIndex);
    }

    public void onBackPressed() {
        int currentPage = mViewFlipper.getDisplayedChild();
        int newPage = currentPage;
        switch (currentPage) {
            case RADIOS_VIEW_INDEX:
                newPage = CATEGORIES_VIEW_INDEX;
                break;
            case PROGRESS_VIEW_INDEX:
            case CATEGORIES_VIEW_INDEX:
            case ERROR_VIEW_INDEX:
                getActivity().finish();
        }
        flipToPage(newPage);
    }

    /**
     * Update the content displayed based on the page index of the view flipper that is currently
     * visible
     *
     * @param pageIndex the page index of the view flipper
     */
    private void updateContent(int pageIndex) {
        if(mCategoriesController == null){
            mCategoriesController = CategoriesController.getInstance();
        }
        switch (pageIndex) {
            case RADIOS_VIEW_INDEX:
                if(mCategoriesController.getLastSelectedCategory() != null) {
                    updateRadioList(mCategoriesController.getLastSelectedCategory().getRadioList());
                    break;
                }
                // else do default
            default:
                updateCategories(mCategoriesController.getCategories());
        }
    }
}
