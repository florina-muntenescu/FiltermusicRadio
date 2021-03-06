package filtermusic.net.common.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import filtermusic.net.common.model.Radio;

/**
 * Holds the representation of a radio table
 */
@DatabaseTable(tableName = "radios")
public class DbRadio {

    public static final String TITLE_FILED_NAME = "title";
    public static final String URL_FILED_NAME = "url";
    public static final String GENRE_FILED_NAME = "genre";
    public static final String DESCRIPTION_FILED_NAME = "description";
    public static final String CATEGORY_FILED_NAME = "category";
    public static final String IMAGE_URL_FILED_NAME = "image_url";
    public static final String IS_FAVORITE_FILED_NAME = "is_favorite";
    public static final String PLAYED_DATE_FILED_NAME = "played_date";

    // id is generated by the database and set on the object automatically
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = TITLE_FILED_NAME)
    private String mTitle;
    @DatabaseField(columnName = URL_FILED_NAME)
    private String mURL;
    @DatabaseField(columnName = GENRE_FILED_NAME)
    private String mGenre;
    @DatabaseField(columnName = DESCRIPTION_FILED_NAME)
    private String mDescription;
    @DatabaseField(columnName = CATEGORY_FILED_NAME)
    private String mCategory;
    @DatabaseField(columnName = IMAGE_URL_FILED_NAME)
    private String mImageUrl;
    @DatabaseField(columnName = IS_FAVORITE_FILED_NAME, index = true)
    private boolean mIsFavorite;
    @DatabaseField(columnName = PLAYED_DATE_FILED_NAME)
    private Date mPlayedDate;


    public DbRadio() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public DbRadio(Radio radio) {
        id = radio.getId();
        mTitle = radio.getTitle();
        mURL = radio.getURL();
        mGenre = radio.getGenre();
        mDescription = radio.getDescription();
        mCategory = radio.getCategory();
        mImageUrl = radio.getImageUrl();
        mIsFavorite = radio.isFavorite();
        mPlayedDate = radio.getPlayedDate();
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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + " " + mTitle + " " + mCategory + " " + mGenre + " " + mDescription + " "
                + mImageUrl + " " + mURL + " " + mIsFavorite + " " + mPlayedDate;
    }
}
