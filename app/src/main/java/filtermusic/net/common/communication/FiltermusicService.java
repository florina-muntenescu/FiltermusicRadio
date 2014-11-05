package filtermusic.net.common.communication;

import java.util.List;

import filtermusic.net.common.model.Radio;
import retrofit.http.*;
import rx.Observable;

public interface FiltermusicService {
    @GET("/ios-feed")
    Observable<List<Radio>> getRadios();
}
