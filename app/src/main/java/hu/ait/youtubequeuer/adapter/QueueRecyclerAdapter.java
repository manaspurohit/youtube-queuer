package hu.ait.youtubequeuer.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.ait.youtubequeuer.R;
import hu.ait.youtubequeuer.data.Video;
import hu.ait.youtubequeuer.touch.VideoTouchHelperAdapter;
import io.realm.Realm;
import io.realm.RealmResults;

public class QueueRecyclerAdapter extends RecyclerView.Adapter<QueueRecyclerAdapter.ViewHolder>
        implements VideoTouchHelperAdapter{

    private Realm realmVideoQueue;
    private Context context;

    private List<Video> videoQueue;

    public QueueRecyclerAdapter(Context context, Realm realmVideoQueue) {
        this.realmVideoQueue = realmVideoQueue;
        this.context = context;

        videoQueue = new ArrayList<Video>();

        RealmResults<Video> cityRealmResults =
                realmVideoQueue.where(Video.class).findAll();
        int resultsSize = cityRealmResults.size();
        for (int i = 0; i < resultsSize; i++) {
            videoQueue.add(cityRealmResults.get(i));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View videoRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row, parent, false);

        return new QueueRecyclerAdapter.ViewHolder(videoRow);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(videoQueue.get(position).getImgURL()).into(holder.ivThumbnail);
        holder.tvTitle.setText(videoQueue.get(position).getTitle());
        holder.tvChannel.setText(videoQueue.get(position).getChannel());
    }

    @Override
    public int getItemCount() {
        return videoQueue.size();
    }

    @Override
    public void onItemDismiss(int position) {
        realmVideoQueue.beginTransaction();
        videoQueue.get(position).deleteFromRealm();
        realmVideoQueue.commitTransaction();

        videoQueue.remove(position);
        notifyItemRemoved(position);
    }


    public void addVideo(Video video) {
        realmVideoQueue.beginTransaction();
        Video newVideo = realmVideoQueue.createObject(Video.class);
        newVideo.setVideoID(video.getVideoID());
        newVideo.setTitle(video.getTitle());
        newVideo.setChannel(video.getChannel());
        newVideo.setImgURL(video.getImgURL());
        realmVideoQueue.commitTransaction();

        videoQueue.add(newVideo);
        notifyDataSetChanged();
    }

    public Video getFirstVideo() {
        return videoQueue.get(0);
    }

    public void removeFirstVideo() {
        realmVideoQueue.beginTransaction();
        videoQueue.get(0).deleteFromRealm();
        realmVideoQueue.commitTransaction();

        videoQueue.remove(0);
        notifyItemRemoved(0);
    }

    public boolean isQueueEmpty() {
        return videoQueue.isEmpty();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvTitle;
        TextView tvChannel;

        public ViewHolder(View itemView) {
            super(itemView);

            ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvChannel = (TextView) itemView.findViewById(R.id.tvChannel);
        }
    }
}
