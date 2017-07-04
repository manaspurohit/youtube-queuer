package hu.ait.youtubequeuer.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Video extends RealmObject {
    @PrimaryKey
    private String videoID;

    private String title;
    private String channel;
    private String imgURL;

    public Video() {

    }

    public Video(String videoID, String title, String channel, String imgURL) {
        this.videoID = videoID;
        this.title = title;
        this.channel = channel;
        this.imgURL = imgURL;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }


}
