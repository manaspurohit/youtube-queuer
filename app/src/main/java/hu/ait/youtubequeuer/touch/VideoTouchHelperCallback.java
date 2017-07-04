package hu.ait.youtubequeuer.touch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.END;
import static android.support.v7.widget.helper.ItemTouchHelper.START;

public class VideoTouchHelperCallback extends ItemTouchHelper.Callback {

    private VideoTouchHelperAdapter videoTouchHelperAdapter;

    public VideoTouchHelperCallback(VideoTouchHelperAdapter videoTouchHelperAdapter) {
        this.videoTouchHelperAdapter = videoTouchHelperAdapter;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeFlag(ACTION_STATE_SWIPE, START | END);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        videoTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
