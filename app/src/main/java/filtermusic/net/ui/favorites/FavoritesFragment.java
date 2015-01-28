package filtermusic.net.ui.favorites;

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

import java.util.List;

import filtermusic.net.R;
import filtermusic.net.common.model.Radio;
import filtermusic.net.player.PlayerController;
import filtermusic.net.ui.RadioDetailView;
import filtermusic.net.ui.RadiosAdapter;
import filtermusic.net.ui.details.RadioDetailActivity;

/**
 * Holds the list of favorite radios and manages the navigation between the list and the details
 * view of the radio
 */
public class FavoritesFragment extends Fragment implements FavoritesController
        .DataRetrievedListener {

    private static final String LAST_OPENED_VIEW = "last_opened_view";

    private static final int PROGRESS_VIEW_INDEX = 0;
    private static final int RADIOS_VIEW_INDEX = 1;

    private ViewFlipper mViewFlipper;
    private ListView mRadiosList;

    private FavoritesController mController;

    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_fragment, container, false);

        mController = new FavoritesController();

        mViewFlipper = (ViewFlipper) rootView.findViewById(R.id.view_flipper);
        mRadiosList = (ListView) rootView.findViewById(R.id.favorites);
        mRadiosList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            AdapterView<?> parent, View view, int position, long id) {
                        final Radio selectedRadio = mController.selectRadio(position);
                        if(!PlayerController.getInstance().radioWasPlayedInThisSession()){
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

        mRadiosList.setEmptyView(rootView.findViewById(android.R.id.empty));

        mController.retrieveFavorites(this);

        return rootView;
    }

    public void updateRadioList(List<Radio> radios) {
        RadiosAdapter radiosAdapter = new RadiosAdapter(
                getActivity(), R.layout.radio_list_item, radios);

        mRadiosList.setAdapter(radiosAdapter);
    }

    @Override
    public void onDataRetrieved(List<Radio> radios) {
        updateRadioList(radios);
        if (PROGRESS_VIEW_INDEX == mViewFlipper.getDisplayedChild()) {
            flipToPage(RADIOS_VIEW_INDEX);
        }
    }

    private void flipToPage(int pageIndex) {
        mViewFlipper.setDisplayedChild(pageIndex);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mViewFlipper != null) {
            outState.putInt(LAST_OPENED_VIEW, mViewFlipper.getDisplayedChild());
        }
    }

}
