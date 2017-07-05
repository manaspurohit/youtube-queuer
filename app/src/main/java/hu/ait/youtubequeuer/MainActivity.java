package hu.ait.youtubequeuer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.ait.youtubequeuer.adapter.QueueRecyclerAdapter;
import hu.ait.youtubequeuer.adapter.SearchResultsRecyclerAdapter;
import hu.ait.youtubequeuer.data.Item;
import hu.ait.youtubequeuer.data.SearchResult;
import hu.ait.youtubequeuer.data.Video;
import hu.ait.youtubequeuer.retrofit.SearchAPI;
import hu.ait.youtubequeuer.touch.VideoTouchHelperCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final int MAX_RESULTS = 15;
    public static final String URL = "https://www.googleapis.com";
    public static final String PART = "snippet";
    public static final String TYPE = "video";

    @BindView(R.id.recyclerQueue)
    RecyclerView recyclerQueue;
    @BindView(R.id.btnAddVideo)
    Button btnAddVideo;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.layoutButtons)
    LinearLayout layoutButtons;
    @BindView(R.id.recyclerSearch)
    RecyclerView recyclerSearch;

    private SearchResultsRecyclerAdapter searchRecyclerAdapter;
    private QueueRecyclerAdapter queueRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        ((MainApplication) getApplication()).openRealm();

        recyclerQueue.setHasFixedSize(true);
        recyclerQueue.setLayoutManager(new LinearLayoutManager(this));
        queueRecyclerAdapter = new QueueRecyclerAdapter(this,
                ((MainApplication) getApplication()).getRealmCity());
        recyclerQueue.setAdapter(queueRecyclerAdapter);

        ItemTouchHelper.Callback callback = new VideoTouchHelperCallback(queueRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerQueue);

        recyclerSearch.setHasFixedSize(true);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerAdapter = new SearchResultsRecyclerAdapter(this);
        recyclerSearch.setAdapter(searchRecyclerAdapter);

        if (!queueRecyclerAdapter.isQueueEmpty()) {
            initializeYouTubePlayer(queueRecyclerAdapter.getFirstVideo());
        }
    }

    private void initializeYouTubePlayer(final Video firstVideo) {
        YouTubePlayerFragment youTubePlayerFragment =
                (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtubePlayer);
        youTubePlayerFragment.initialize(getResources().getString(R.string.YOUTUBE_API_KEY),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                        if (!b) {
                            youTubePlayer.loadVideo(firstVideo.getVideoID());

                            youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                                @Override
                                public void onLoading() {

                                }

                                @Override
                                public void onLoaded(String s) {

                                }

                                @Override
                                public void onAdStarted() {

                                }

                                @Override
                                public void onVideoStarted() {

                                }

                                @Override
                                public void onVideoEnded() {
                                    queueRecyclerAdapter.removeFirstVideo();
                                    if (!queueRecyclerAdapter.isQueueEmpty()) {
                                        Video nextVideo = queueRecyclerAdapter.getFirstVideo();
                                        youTubePlayer.loadVideo(nextVideo.getVideoID());
                                    }
                                }

                                @Override
                                public void onError(YouTubePlayer.ErrorReason errorReason) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

    @OnClick(R.id.btnAddVideo)
    public void addVideo() {
        changeVisibility(View.GONE, View.VISIBLE);

        etSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
    }

    @OnClick(R.id.btnCancel)
    public void cancel() {
        changeVisibility(View.VISIBLE, View.GONE);

        searchRecyclerAdapter.clearSearch();
        etSearch.setText("");

        hideKeyboard();
    }

    private void changeVisibility(int queue, int search) {
        recyclerQueue.setVisibility(queue);
        btnAddVideo.setVisibility(queue);
        etSearch.setVisibility(search);
        layoutButtons.setVisibility(search);
        recyclerSearch.setVisibility(search);
    }

    @OnClick(R.id.btnSearch)
    public void searchYoutube() {
        if (TextUtils.isEmpty(etSearch.getText().toString())) {
            etSearch.setError(getString(R.string.search_empty_error));
            return;
        }

        searchRecyclerAdapter.clearSearch();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final SearchAPI searchAPI = retrofit.create(SearchAPI.class);
        Call<SearchResult> callSearch = searchAPI.getSearchResult(PART,
                Integer.valueOf(MAX_RESULTS).toString(),
                etSearch.getText().toString(),
                TYPE,
                getResources().getString(R.string.YOUTUBE_API_KEY));

        callSearch.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                SearchResult searchResult = response.body();
                List<Item> videoResults = searchResult.getItems();

                for (Item item : videoResults) {
                    Video video = new Video(item.getId().getVideoId(),
                            item.getSnippet().getTitle(),
                            item.getSnippet().getChannelTitle(),
                            item.getSnippet().getThumbnails().get_default().getUrl());
                    searchRecyclerAdapter.addVideo(video);
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

        etSearch.setText("");
        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void addVideoToQueue(Video video) {
        changeVisibility(View.VISIBLE, View.GONE);

        if (queueRecyclerAdapter.isQueueEmpty()) {
            initializeYouTubePlayer(video);
        }

        queueRecyclerAdapter.addVideo(video);
    }

    @Override
    protected void onDestroy() {
        ((MainApplication) getApplication()).closeRealm();
        super.onDestroy();
    }
}
