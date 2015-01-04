package filtermusic.net.ui.favorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import java.util.List;

import filtermusic.net.R;
import filtermusic.net.common.model.Radio;
import filtermusic.net.ui.RadioDetailView;
import filtermusic.net.ui.RadiosAdapter;

/**
 * Holds the list of favorite radios and manages the navigation between the list and the details
 * view of the radio
 */
public class FavoritesFragment extends Fragment implements FavoritesController
        .DataRetrievedListener {

    private static final String LAST_OPENED_VIEW = "last_opened_view";

    private static final int PROGRESS_VIEW_INDEX = 0;
    private static final int RADIOS_VIEW_INDEX = 1;
    private static final int RADIO_DETAIL_VIEW_INDEX = 2;


    private ViewFlipper mViewFlipper;
    private ListView mRadiosList;

    private RadioDetailView mRadioDetailView;

    private FavoritesController mController;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_fragment, container, false);

        mController = new FavoritesController();

        mViewFlipper = (ViewFlipper) rootView.findViewById(R.id.view_flipper);
        mRadiosList = (ListView) rootView.findViewById(R.id.favorites);
        mRadiosList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
                        Radio selectedRadio = mController.selectRadio(position);
                        updateRadio(selectedRadio);
                        flipToPage(RADIO_DETAIL_VIEW_INDEX);
                    }
                });

        mRadiosList.setEmptyView(rootView.findViewById(android.R.id.empty));
        mRadioDetailView = (RadioDetailView) rootView.findViewById(R.id.radio_detail_view);

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

    private void updateRadio(Radio radio) {
        mRadioDetailView.setRadio(radio);
    }


    private void flipToPage(int pageIndex) {
        mViewFlipper.setDisplayedChild(pageIndex);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LAST_OPENED_VIEW, mViewFlipper.getDisplayedChild());
    }

    public void onBackPressed() {
        int currentPage = mViewFlipper.getDisplayedChild();
        int newPage = currentPage;
        switch (currentPage) {
            case RADIO_DETAIL_VIEW_INDEX:
                newPage = RADIOS_VIEW_INDEX;
                break;
            case PROGRESS_VIEW_INDEX:
            case RADIOS_VIEW_INDEX:
                getActivity().finish();
        }

        updateContent(newPage);
        flipToPage(newPage);

    }

    /**
     * Update the content displayed based on the page index of the view flipper that is currently
     * visible
     *
     * @param pageIndex the page index of the view flipper
     */
    private void updateContent(int pageIndex) {
        switch (pageIndex) {
            case RADIOS_VIEW_INDEX:
                //                updateRadioList(mCategoriesController.getLastSelectedCategory()
                // .getRadioList());
                break;
            case RADIO_DETAIL_VIEW_INDEX:
                updateRadio(mController.getLastSelectedRadio());
                break;
        }
    }
}
