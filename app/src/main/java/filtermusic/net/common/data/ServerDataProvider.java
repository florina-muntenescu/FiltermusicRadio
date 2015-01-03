package filtermusic.net.common.data;

import android.content.Context;
import android.util.Log;

import com.google.common.collect.ImmutableList;

import java.util.List;

import filtermusic.net.common.communication.FiltermusicApi;
import filtermusic.net.common.model.Radio;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Provides data from the Server
 */
/*package*/ class ServerDataProvider {

    private static final String LOG_TAG = ServerDataProvider.class.getSimpleName();
    private Context mContext;

    ServerDataProvider(Context context) {
        this.mContext = context;
    }

    public void provideRadioList(final DataProvider.RadioListRetrievedListener listener) {
        FiltermusicApi api = new FiltermusicApi(mContext);
        Observable<List<Radio>> apiObservable = api.createFromRestAdapter().getRadios();
        apiObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Radio>>() {
                               @Override
                               public void onCompleted() {
                                   // nothing
                               }

                               @Override
                               public void onError(Throwable e) {
                               }

                               @Override
                               public void onNext(List<Radio> radios) {
                                   Log.d(LOG_TAG, "radios: " + radios.size());
                                   listener.onRadioListRetrieved(ImmutableList.copyOf(radios));
                               }
                           });

    }

}
