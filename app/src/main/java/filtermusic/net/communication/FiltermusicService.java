package filtermusic.net.communication;

import java.util.List;

import filtermusic.net.model.Radio;
import retrofit.http.*;
import rx.Observable;

/**
 * Created by android on 10/18/14.
 */
public interface FiltermusicService {
    @GET("/ios-feed")
    Observable<List<Radio>> getRadios();
}
