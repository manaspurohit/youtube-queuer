package hu.ait.youtubequeuer;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import hu.ait.youtubequeuer.data.Video;

import static hu.ait.youtubequeuer.MainActivity.MAX_RESULTS;

public class SearchResultsRecyclerAdapter extends RecyclerView.Adapter<SearchResultsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Video> searchVideos;

    SearchResultsRecyclerAdapter(Context context) {
        this.context = context;

        searchVideos = new ArrayList<Video>(MAX_RESULTS);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View videoRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row, parent, false);

        return new ViewHolder(videoRow);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.layoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).addVideoToQueue(searchVideos.get(holder.getAdapterPosition()));
                clearSearch();
            }
        });

        Glide.with(context).load(searchVideos.get(position).getImgURL()).into(holder.ivThumbnail);
        holder.tvTitle.setText(searchVideos.get(position).getTitle());
        holder.tvChannel.setText(searchVideos.get(position).getChannel());
    }

    @Override
    public int getItemCount() {
        return searchVideos.size();
    }

    public void addVideo(Video video) {
        searchVideos.add(video);
        notifyDataSetChanged();
    }

    public void clearSearch() {
        searchVideos.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutRow;
        ImageView ivThumbnail;
        TextView tvTitle;
        TextView tvChannel;

        public ViewHolder(View itemView) {
            super(itemView);

            layoutRow = (LinearLayout) itemView.findViewById(R.id.layoutRow);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvChannel = (TextView) itemView.findViewById(R.id.tvChannel);
        }
    }
}
