package filtermusic.net.common.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Provides;
import filtermusic.net.common.database.DatabaseHelper;
import filtermusic.net.common.database.DbRadioController;

/**
 * Connects the classes that need DB access
 */
public class DbModule {
    private static final String DATABASE_NAME = "filtermusic_radios.db";
    private static final int DATABASE_VERSION = 1;

    @Provides
    @Singleton
    public DbRadioController provideDbRadioController(DatabaseHelper helper) {
        return new DbRadioController(helper);
    }

    @Provides
    @Singleton
    public DatabaseHelper provideDbTransactionOpenHelper(Context context) {
        return new DatabaseHelper(context, DATABASE_NAME, DATABASE_VERSION);
    }
}
