package filtermusic.net.common.communication;

import android.support.annotation.NonNull;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import filtermusic.net.common.model.Radio;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * This class parses XML feeds from http://filtermusic.net/ios-feed
 * Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (radio station) in the XML feed.
 */
/*package*/ class FeedXmlParser implements Converter {
    // We don't use namespaces
    private static final String ns = null;


    public List<Radio> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Radio> readFeed(final @NonNull XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<Radio> entries = new ArrayList<Radio>();

        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("channel")) {
                return readChannel(parser);
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private List<Radio> readChannel(final @NonNull XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<Radio> entries = new ArrayList<Radio>();

        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals(Radio.TAG_ITEM)) {
                entries.add(readRadio(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    /**
     * Parses the contents of an item. If it encounters a title, summary, or link tag, hands them
     * off to their respective read methods for processing. Otherwise, skips the tag.
     */
    private Radio readRadio(final @NonNull XmlPullParser parser)
            throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");

        String title = null;
        String url = null;
        String imageUrl = null;
        String genre = null;
        String description = null;
        String category = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(Radio.TAG_TITLE)) {
                title = readTag(parser, Radio.TAG_TITLE);
            } else if (name.equals(Radio.TAG_URL)) {
                url = readTag(parser, Radio.TAG_URL);
            } else if (name.equals(Radio.TAG_IMAGEURL)) {
                imageUrl = readImageUrl(parser);
            } else if (name.equals(Radio.TAG_GENRE)) {
                genre = readTag(parser, Radio.TAG_GENRE);
            } else if (name.equals(Radio.TAG_DESCRIPTION)) {
                description = readTag(parser, Radio.TAG_DESCRIPTION);
            } else if (name.equals(Radio.TAG_CATEGORY)) {
                category = readTag(parser, Radio.TAG_CATEGORY);
            } else {
                skip(parser);
            }
        }

        return new Radio(title, url, genre, description, category, imageUrl, false, null);
    }

    // Processes tags in the feed.
    private String readTag(final @NonNull XmlPullParser parser, final @NonNull String tag)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return title;
    }

    // Processes image url tags in the feed.
    private String readImageUrl(final @NonNull XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, Radio.TAG_IMAGEURL);
        String tag = parser.getName();
        if (tag.equals(Radio.TAG_IMAGEURL)) {
            link = parser.getAttributeValue(null, Radio.ATTRIBUTE_IMAGEURL);
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, Radio.TAG_IMAGEURL);
        return link;
    }


    private String readText(final @NonNull XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(final @NonNull XmlPullParser parser)
            throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        try {
            return parse(body.in());
        } catch (IOException e) {
            return null;
        } catch (XmlPullParserException e) {
            return null;
        }
    }

    @Override
    public TypedOutput toBody(Object object) {
        return null;
    }
}
