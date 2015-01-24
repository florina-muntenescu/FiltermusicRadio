package filtermusic.net.ui.categories;

import android.content.Context;
import android.util.Log;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.common.data.DataProvider;
import filtermusic.net.common.model.Category;
import filtermusic.net.common.model.Radio;

/**
 * Controls the data that is displayed in the categories fragment
 * Retrieves data from the server or DB when needed and notifies the
 * data listeners when data is updated.
 */
public class CategoriesController implements DataProvider.RadioListRetrievedListener {

    private static final String LOG_TAG = CategoriesController.class.getSimpleName();

    @Inject
    DataProvider mDataProvider;

    public interface DataListener {
        void onCategoriesUpdated(List<Category> categories);
        void onError();
    }

    private static CategoriesController mInstance;

    private Context mContext;

    private List<Category> mCategories = new ArrayList<Category>();

    private DataListener mDataListener;

    private Radio mLastSelectedRadio;
    private Category mLastSelectedCategory;


    public static CategoriesController getInstance() {
        if (mInstance == null) {
            mInstance = new CategoriesController();
        }
        return mInstance;
    }

    private CategoriesController() {
        mContext = FiltermusicApplication.getInstance().getApplicationContext();

        FiltermusicApplication.getInstance().inject(this);
        syncRadios();
    }

    public List<Radio> selectCategory(Category category) {
        mLastSelectedCategory = category;
        return mLastSelectedCategory.getRadioList();

    }

    public Radio selectRadio(int radioIndex) {
        mLastSelectedRadio = mLastSelectedCategory.getRadioList().get(radioIndex);
        return mLastSelectedRadio;
    }

    public Category getLastSelectedCategory() {
        return mLastSelectedCategory;
    }

    private void updateCategories(ImmutableList<Radio> radioImmutableList) {
        mCategories.clear();
        Map<String, List<Radio>> categoryMap = new LinkedHashMap<>();
        for (Radio radio : radioImmutableList) {
            if (categoryMap.containsKey(radio.getGenre())) {
                categoryMap.get(radio.getGenre()).add(radio);
            } else {
                List<Radio> radios = new ArrayList<Radio>();
                radios.add(radio);
                categoryMap.put(radio.getGenre(), radios);
                Log.d(LOG_TAG, radio.getGenre());
            }
        }

        for (String key : categoryMap.keySet()) {
            Log.d(LOG_TAG, key);
            Category category = new Category(key, categoryMap.get(key));
            mCategories.add(category);
        }

        if (mDataListener != null) {
            mDataListener.onCategoriesUpdated(mCategories);
        }
    }

    public void syncRadios() {
        mDataProvider.provide(this);
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public void setDataListener(DataListener dataListener) {
        mDataListener = dataListener;
    }

    @Override
    public void onRadioListRetrieved(List<Radio> radios) {
        updateCategories(ImmutableList.copyOf(radios));
    }

    @Override
    public void onError() {
        mDataListener.onError();
    }
}
