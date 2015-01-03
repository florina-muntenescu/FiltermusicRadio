package filtermusic.net.common.data;

import android.content.Context;
import android.content.res.AssetManager;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import filtermusic.net.R;
import filtermusic.net.common.communication.FeedXmlParser;
import filtermusic.net.common.model.Radio;

/**
 * Provides the list of radios from the local xml file
 */
/*pacakge*/ class LocalDataProvider {


    public List<Radio> provideLocalData(Context context) {
        List<Radio> radios = new ArrayList<Radio>();

        try {
            InputStream is = context.getResources().openRawResource(R.raw.feed);

            FeedXmlParser parser = new FeedXmlParser();
            radios = parser.parse(is);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return radios;
    }

}
