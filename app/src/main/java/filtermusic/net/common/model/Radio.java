package filtermusic.net.common.model;

import java.util.Date;

/**
 * Example of a Radio item in XML
 * <p/>
 * <item> <title>Insomnia fm</title>
 * <serverURL>http://208.43.9.96:8092</serverURL>
 * <enclosure url="http://filtermusic.net/sites/default/files/insomniafm_0.jpg" length="36246" type="image/jpeg" />
 * <musicgenre>Techno / Trance</musicgenre>
 * <description>All sorts of electronic music as well as laid back tunes.&amp;nbsp;</description>
 * <category>Shoutcast MPEG</category>
 * </item>
 */
public class Radio {

    public static final String TAG_ITEM = "item";
    public static final String TAG_TITLE = "title";
    public static final String TAG_URL = "serverURL";
    public static final String TAG_IMAGEURL = "enclosure";
    public static final String ATTRIBUTE_IMAGEURL = "url";
    public static final String TAG_GENRE = "musicgenre";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_CATEGORY = "category";


    private String mTitle;
    private String mURL;
    private String mGenre;
    private String mDescription;
    private String mCategory;
    private String mImageUrl;
    private boolean mIsFavorite;
    private Date mPlayedDate;


    public Radio() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public Radio(String title, String URL, String genre, String description, String category,
                 String imageUrl, boolean isFavorite, Date playedDate) {
        mTitle = title;
        mURL = URL;
        mGenre = genre;
        mDescription = description;
        mCategory = category;
        mImageUrl = imageUrl;
        mIsFavorite = isFavorite;
        mPlayedDate = playedDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getURL() {
        return mURL;
    }

    public String getGenre() {
        return mGenre;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public Date getPlayedDate() {
        return mPlayedDate;
    }

    public void setFavorite(boolean isFavorite) {
        mIsFavorite = isFavorite;
    }

    public void setPlayedDate(Date playedDate) {
        mPlayedDate = playedDate;
    }

    @Override
    public boolean equals(Object o) {
        Radio radio = (Radio)o;
        if(this.getTitle().equals(radio.getTitle())
                && this.getDescription().equals(radio.getDescription())
                && this.getCategory().equals(radio.getCategory())
                && this.getGenre().equals(radio.getGenre())
                && this.getImageUrl().equals(radio.getImageUrl())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
