package filtermusic.net;

/**
 * A tab class defines the fragment that is contained by the tab and the title of the tab
 */
public class Tab {

    private String mFragmentClass;
    private String mTabTitle;
    private String mFragmentTag;

    public Tab(String fragmentClass, String tabTitle) {
        mFragmentClass = fragmentClass;
        mTabTitle = tabTitle;
    }

    public String getFragmentClass() {
        return mFragmentClass;
    }

    public String getTabTitle() {
        return mTabTitle;
    }

    public String getFragmentTag() {
        return mFragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        mFragmentTag = fragmentTag;
    }
}
