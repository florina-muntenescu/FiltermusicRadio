package filtermusic.net.common.data;

import android.content.Context;
import android.test.InstrumentationTestCase;

import filtermusic.net.common.database.DatabaseHelper;
import filtermusic.net.common.database.DbRadioController;

/**
 * Creates the database before each test and removes it afterwards
 */
public class DbRadioBaseTest extends InstrumentationTestCase {
    private static final String DB_NAME = "sample.db";
    protected DbRadioController mController;

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        getInstrumentation().getTargetContext().deleteDatabase(DB_NAME);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context targetContext = getInstrumentation().getTargetContext();

        DatabaseHelper dbHelper = new DatabaseHelper(targetContext, DB_NAME, 1);
        mController = new DbRadioController(dbHelper);
    }
}
