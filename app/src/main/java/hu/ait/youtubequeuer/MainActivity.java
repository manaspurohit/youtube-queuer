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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerQueue)
    RecyclerView recyclerQueue;
    @BindView(R.id.btnAddVideo)
    Button btnAddVideo;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.layoutButtons)
    LinearLayout layoutButtons;

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
    }

    @OnFocusChange(R.id.etSearch)
    public void focusLost(boolean hasFocus) {
        if (!hasFocus) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
