package hu.ait.youtubequeuer.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnails {

    @SerializedName("default")
    @Expose
    public Default _default;
    @SerializedName("medium")
    @Expose
    public Medium medium;
    @SerializedName("high")
    @Expose
    public High high;

    public Default get_default() {
        return _default;
    }

    public Medium getMedium() {
        return medium;
    }

    public High getHigh() {
        return high;
    }
}