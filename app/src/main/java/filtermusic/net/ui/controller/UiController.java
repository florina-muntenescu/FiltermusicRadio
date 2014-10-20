package filtermusic.net.ui.controller;

import android.content.Context;
import android.util.Log;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import filtermusic.net.FiltermusicApplication;
import filtermusic.net.communication.FiltermusicApi;
import filtermusic.net.model.Category;
import filtermusic.net.model.Radio;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by android on 10/18/14.
 */
public class UiController {
    private static final String LOG_TAG = UiController.class.getSimpleName();

    private static UiController mInstance;

    private Context mContext;

    private List<Category> mCategories = new ArrayList<Category>();

    public static UiController getInstance(){
        if(mInstance == null){
            mInstance = new UiController();
        }
        return mInstance;
    }

    private UiController() {
        mContext = FiltermusicApplication.getInstance().getApplicationContext();
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
    }

    private void syncRadios() {
        FiltermusicApi api = new FiltermusicApi(mContext);
        Observable<List<Radio>> apiObservable = api.createFromRestAdapter().getRadios();
        apiObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Radio>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<Radio> radios) {
                        Log.d(LOG_TAG, "radios: " + radios.size());
                        updateCategories(ImmutableList.copyOf(radios));
                    }
                });
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public List<Radio> getRadiosForFirstCatgory(){
        if(mCategories.size() > 0){
            return mCategories.get(0).getRadioList();
        }
        return null;
    }
}
