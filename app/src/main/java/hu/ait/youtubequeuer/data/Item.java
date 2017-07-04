package hu.ait.youtubequeuer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("kind")
    @Expose
    public String kind;
    @SerializedName("etag")
    @Expose
    public String etag;
    @SerializedName("id")
    @Expose
    public Id id;
    @SerializedName("snippet")
    @Expose
    public Snippet snippet;

    public String getKind() {
        return kind;
    }

    public String getEtag() {
        return etag;
    }

    public Id getId() {
        return id;
    }

    public Snippet getSnippet() {
        return snippet;
    }
}