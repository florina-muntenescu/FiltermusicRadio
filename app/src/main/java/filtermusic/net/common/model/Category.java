package filtermusic.net.common.model;

import java.util.List;

/**
 * List of categories based on genre
 */
public class Category {
    // genre of this category
    private String mGenre;

    private List<Radio> mRadioList;


    public Category(String genre, List<Radio> radioList) {
        mGenre = genre;
        mRadioList = radioList;
    }

    public List<Radio> getRadioList() {
        return mRadioList;
    }

    public String getGenre() {
        return mGenre;
    }

}
