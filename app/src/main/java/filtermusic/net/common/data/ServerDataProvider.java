package filtermusic.net.common.data;

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

    ServerDataProvider() {
        //nothing to do
    }

    public void provideRadioList(final DataProvider.RadioListRetrievedListener listener) {
        FiltermusicApi api = new FiltermusicApi();
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
                        listener.onError();
                    }

                    @Override
                    public void onNext(List<Radio> radios) {
                        Log.d(LOG_TAG, "radios: " + radios.size());
                        listener.onRadioListRetrieved(ImmutableList.copyOf(radios));
                    }
                });

    }

}
