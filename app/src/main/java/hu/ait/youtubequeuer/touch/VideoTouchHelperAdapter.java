package hu.ait.youtubequeuer.touch;

public interface VideoTouchHelperAdapter {

    void onItemDismiss(int position);

    void onItemMove(int fromPosition,
                           int toPosition);
}
