package filtermusic.net.categories;

import android.content.Context;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.common.data.DataProvider;
import filtermusic.net.common.model.Category;
import filtermusic.net.common.model.Radio;

/**
 * Controls the data that is displayed in the categories fragment
 * Retrieves data from the server or DB when needed and notifies the
 * data listeners when data is updated.
 */
public class CategoriesController implements DataProvider.DataUpdatedListener{

    public interface DataListener{
        void onCategoriesUpdated(List<Category> categories);
    }

    private static final String LOG_TAG = CategoriesController.class.getSimpleName();

    private static CategoriesController mInstance;

    private Context mContext;

    private DataProvider mDataProvider;
    private List<Category> mCategories = new ArrayList<Category>();

    private DataListener mDataListener;


    public static CategoriesController getInstance(){
        if(mInstance == null){
            mInstance = new CategoriesController();
        }
        return mInstance;
    }

    private CategoriesController() {
        mContext = FiltermusicApplication.getInstance().getApplicationContext();
        mDataProvider = new DataProvider(mContext);
        syncRadios();
    }

    private void updateCategories(ImmutableList<Radio> radioImmutableList){
        mCategories.clear();
        Map<String, List<Radio>> categoryMap = new HashMap<String, List<Radio>>();
        for(Radio radio : radioImmutableList){
            if(categoryMap.containsKey(radio.getGenre())){
                categoryMap.get(radio.getGenre()).add(radio);
            }else{
                List<Radio> radios = new ArrayList<Radio>();
                radios.add(radio);
                categoryMap.put(radio.getGenre(), radios);
            }
        }

        for(String key : categoryMap.keySet()){
            Category category = new Category(key, categoryMap.get(key));
            mCategories.add(category);
        }

        if(mDataListener != null){
            mDataListener.onCategoriesUpdated(mCategories);
        }
    }

    private void syncRadios() {
       mDataProvider.getDataFromServer(this);
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public List<Radio> getRadiosForFirstCatgory(){
        if(mCategories.size() > 0){
            return mCategories.get(0).getRadioList();
        }
        Radio radio = new Radio("a","b", "c", "d", "e", "f");
        List<Radio> radios = new ArrayList<Radio>();
        radios.add(radio);
        return radios;
    }

    public void setDataListener(DataListener dataListener) {
        mDataListener = dataListener;
    }

    @Override
    public void onRadioListUpdated(List<Radio> radios) {
        updateCategories(ImmutableList.copyOf(radios));
    }

    @Override
    public void onFavoritesUpdated(List<Radio> radios) {

    }

    @Override
    public void onLastPlayedUpdated(List<Radio> radios) {

    }
}
