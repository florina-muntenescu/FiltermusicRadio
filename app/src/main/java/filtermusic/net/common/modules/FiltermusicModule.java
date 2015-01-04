package filtermusic.net.common.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import filtermusic.net.FiltermusicApplication;
import filtermusic.net.common.data.DataProvider;
import filtermusic.net.player.PlayerController;
import filtermusic.net.ui.RadioDetailView;
import filtermusic.net.ui.categories.CategoriesController;
import filtermusic.net.ui.favorites.FavoritesController;
import filtermusic.net.ui.recents.RecentsController;


@Module(
        injects = {CategoriesController.class, FavoritesController.class, RadioDetailView.class,
                RecentsController.class,
                PlayerController.class})
public class FiltermusicModule {

    @Provides
    @Singleton
    DataProvider provideDataProvider() {
        Context context = FiltermusicApplication.getInstance().getApplicationContext();
        return new DataProvider(context);
    }
}