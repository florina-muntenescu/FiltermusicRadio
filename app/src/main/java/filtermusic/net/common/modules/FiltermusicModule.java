package filtermusic.net.common.modules;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import filtermusic.net.FiltermusicApplication;
import filtermusic.net.common.data.DataProvider;
import filtermusic.net.common.data.RadioSynchronizer;
import filtermusic.net.common.database.RadioDbReadAdapter;
import filtermusic.net.common.database.RadioDbWriteAdapter;
import filtermusic.net.player.PlayerController;
import filtermusic.net.ui.RadioDetailView;
import filtermusic.net.ui.categories.CategoriesController;
import filtermusic.net.ui.details.RadioDetailController;
import filtermusic.net.ui.favorites.FavoritesController;
import filtermusic.net.ui.recents.RecentsController;


@Module(
        injects = {CategoriesController.class, FavoritesController.class, RadioDetailView.class,
                RecentsController.class, PlayerController.class, RadioDetailController.class})
public class FiltermusicModule {

    private final Application mApplication;

    public FiltermusicModule(final Application application) {
        this.mApplication = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext(final Application application) {
        return application;
    }

    @Provides
    @Singleton
    DataProvider provideDataProvider(Context context,
                                     RadioDbReadAdapter readAdapter,
                                     RadioDbWriteAdapter writeAdapter,
                                     RadioSynchronizer radioSynchronizer) {
        return new DataProvider(context, readAdapter, writeAdapter, radioSynchronizer);
    }
}