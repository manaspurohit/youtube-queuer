package hu.ait.youtubequeuer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import hu.ait.youtubequeuer.data.SearchResult;
import hu.ait.youtubequeuer.retrofit.SearchAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnAddVideo)
    public void addVideo() {
        recyclerQueue.setVisibility(View.GONE);
        btnAddVideo.setVisibility(View.GONE);
        etSearch.setVisibility(View.VISIBLE);
        layoutButtons.setVisibility(View.VISIBLE);
        recyclerSearch.setVisibility(View.VISIBLE);

        etSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
    }

    @OnClick(R.id.btnCancel)
    public void cancel() {
        recyclerQueue.setVisibility(View.VISIBLE);
        btnAddVideo.setVisibility(View.VISIBLE);
        etSearch.setVisibility(View.GONE);
        layoutButtons.setVisibility(View.GONE);
        recyclerSearch.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnSearch)
    public void searchYoutube() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final SearchAPI searchAPI = retrofit.create(SearchAPI.class);
        Call<SearchResult> callSearch = searchAPI.getSearchResult("snippet", "15",
                etSearch.getText().toString(),
                "video",
                getResources().getString(R.string.YOUTUBE_API_KEY));

        callSearch.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                SearchResult searchResult = response.body();
                etSearch.setText(searchResult.getItems().get(0).getSnippet().getTitle());
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {

            }
        });
    }

    @OnFocusChange(R.id.etSearch)
    public void focusLost(boolean hasFocus) {
        if (!hasFocus) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
