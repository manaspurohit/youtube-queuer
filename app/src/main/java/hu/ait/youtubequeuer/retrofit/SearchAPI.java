package hu.ait.youtubequeuer.retrofit;

import hu.ait.youtubequeuer.data.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchAPI {
    @GET("youtube/v3/search")
    Call<SearchResult> getSearchResult(@Query("part") String part,
                                       @Query("maxResults") String maxResults,
                                       @Query("q") String keywords,
                                       @Query("type") String type
                                       );
}
