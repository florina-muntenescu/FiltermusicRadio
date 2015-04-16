package filtermusic.net.common.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import filtermusic.net.common.communication.FiltermusicApi;
import filtermusic.net.common.data.RadioSynchronizer;
import filtermusic.net.common.database.DatabaseHelper;
import filtermusic.net.common.database.DbRadioController;
import filtermusic.net.common.database.RadioDbReadAdapter;
import filtermusic.net.common.database.RadioDbWriteAdapter;

/**
 * Connects the classes that need DB access
 */
@Module(includes = {
        FiltermusicModule.class
},
        library = true)
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

    @Provides
    @Singleton
    public RadioSynchronizer provideRadioSynchronizer(
            RadioDbReadAdapter readAdapter,
            RadioDbWriteAdapter writeAdapter) {
        FiltermusicApi api = new FiltermusicApi();
        return new RadioSynchronizer(readAdapter, writeAdapter, api.createFromRestAdapter());
    }
}
