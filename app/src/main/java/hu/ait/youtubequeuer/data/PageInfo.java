package hu.ait.youtubequeuer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageInfo {

    @SerializedName("totalResults")
    @Expose
    public Integer totalResults;
    @SerializedName("resultsPerPage")
    @Expose
    public Integer resultsPerPage;

    public Integer getTotalResults() {
        return totalResults;
    }

    public Integer getResultsPerPage() {
        return resultsPerPage;
    }
}