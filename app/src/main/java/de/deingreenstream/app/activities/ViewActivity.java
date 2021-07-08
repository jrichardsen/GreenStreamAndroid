package de.deingreenstream.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import de.deingreenstream.app.R;
import de.deingreenstream.app.data.ExtendedInformationItem;
import de.deingreenstream.app.data.InformationItem;
import de.deingreenstream.app.viewmodels.ViewViewModel;

/**
 * Activity for displaying the contents of information items
 */
public class ViewActivity extends AppCompatActivity {

    public static final String EXTRA_INFORMATION = "com.example.greenstream.INFORMATION_EXTRA";

    private static final String TAG = "ViewActivity";

    private ViewViewModel viewModel;

    private InformationItem informationItem;
    private boolean loggedIn;
    private boolean hasExplanation;

    private MenuItem likeItem;
    private boolean liked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(ViewViewModel.class);

        TextView urlText = findViewById(R.id.url_text);
        ProgressBar progressBar = findViewById(R.id.view_load_progress);
        WebView contentWebView = findViewById(R.id.content_web);
        informationItem = getIntent().getParcelableExtra(EXTRA_INFORMATION);
        if (informationItem == null) {
            Log.e(TAG, "View activity was called without information object");
            finish();
        }
        hasExplanation = informationItem.getExplanation() != 0;
        String url = informationItem.getUrl();
        loggedIn = informationItem instanceof ExtendedInformationItem;
        if (loggedIn)
            liked = ((ExtendedInformationItem) informationItem).getLiked() != 0;
        urlText.setText(url);
        Log.d(TAG, "View activity loaded with url " + url);
        progressBar.setVisibility(View.VISIBLE);
        contentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        contentWebView.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu, menu);
        menu.findItem(R.id.view_help_item).setVisible(hasExplanation);
        likeItem = menu.findItem(R.id.view_like_item);
        likeItem.setVisible(loggedIn);
        updateLikedIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.view_like_item)
            likeStateChanged();
        else if (itemId == R.id.view_help_item)
            openExplanation();
        else if (itemId == R.id.view_share_item)
            shareItemSelected();
        else if (itemId == R.id.view_feedback_item)
            feedback();
        else if (itemId == R.id.open_in_browser_item)
            openExternal();
        else
            return super.onOptionsItemSelected(item);
        return true;
    }

    private void likeStateChanged() {
        liked = !liked;
        viewModel.setLikeState(informationItem.getId(), liked);
        updateLikedIcon();
    }

    private void openExplanation() {
        viewModel.openExplanation(informationItem.getExplanation());
    }

    private void shareItemSelected() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT, informationItem.getUrl());

        startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share)));
    }

    private void updateLikedIcon() {
        likeItem.setIcon(liked
                ? R.drawable.ic_liked_on_24dp
                : R.drawable.ic_liked_off_24dp
        );
    }

    private void feedback() {
        viewModel.showFeedbackDialog(this, informationItem);
    }

    private void openExternal() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(informationItem.getUrl()));
        startActivity(intent);
    }
}
