package filtermusic.net.recents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import filtermusic.net.R;

/**
 * Holds the list of recently played radios and manages the navigation between the list and the details view of the radio
 */
public class RecentsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recents_fragment, container, false);


        return rootView;
    }
}
