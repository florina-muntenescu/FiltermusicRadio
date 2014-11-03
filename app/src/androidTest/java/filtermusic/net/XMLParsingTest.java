package filtermusic.net;

import android.test.InstrumentationTestCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import filtermusic.net.common.model.Radio;
import filtermusic.net.parser.FeedXmlParser;

/**
 * Created by android on 10/19/14.
 */
public class XMLParsingTest extends InstrumentationTestCase {

    private static final String XML_FEED = "<?xml version=\"1.0\" encoding=\"utf-8\" ?> <rss version=\"2.0\" xml:base=\"http://filtermusic.net/\" xmlns:atom=\"http://www.w3.org/2005/Atom\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"> <channel> <title>filtermusic</title>\n" +
            " <description>Internet radio stations</description>\n" +
            " <link>http://filtermusic.net/</link>\n" +
            " <atom:link rel=\"self\" href=\"http://filtermusic.net/ios-feed\" />\n" +
            " <item> <title>DI.fm - House</title>\n" +
            " <serverURL>http://pub8.di.fm:80/di_house</serverURL>\n" +
            " <enclosure url=\"http://filtermusic.net/sites/default/files/difm-house-music.jpg\" length=\"21593\" type=\"image/jpeg\" />\n" +
            " <musicgenre>House / Dance</musicgenre>\n" +
            " <description>Silky, sexy, deep house music, straight from the heart of New York City!</description>\n" +
            " <category>Icecast</category>\n" +
            "</item>\n" +
            " <item> <title>Insomnia fm</title>\n" +
            " <serverURL>http://208.43.9.96:8092</serverURL>\n" +
            " <enclosure url=\"http://filtermusic.net/sites/default/files/insomniafm_0.jpg\" length=\"36246\" type=\"image/jpeg\" />\n" +
            " <musicgenre>Techno / Trance</musicgenre>\n" +
            " <description>All sorts of electronic music as well as laid back tunes.&amp;nbsp;</description>\n" +
            " <category>Shoutcast MPEG</category>\n" +
            "</item></channel>\n" +
            "</rss>";

    public void testParsing() throws Exception {

        FeedXmlParser parser = new FeedXmlParser();

        InputStream stream = new ByteArrayInputStream(XML_FEED.getBytes(StandardCharsets.UTF_8));
        List<Radio> radioList = parser.parse(stream);

        assertNotNull(radioList);
        assertTrue(radioList.size() > 0);
    }

}


