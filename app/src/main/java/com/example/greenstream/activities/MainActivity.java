package com.example.greenstream.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.greenstream.R;
import com.example.greenstream.adapters.InformationAdapter;
import com.example.greenstream.authentication.AppAccount;
import com.example.greenstream.data.ExtendedInformationItem;
import com.example.greenstream.data.ListState;
import com.example.greenstream.data.InformationItem;
import com.example.greenstream.data.PersonalListType;
import com.example.greenstream.viewmodels.MainViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.greenstream.adapters.InformationAdapter.ItemActionListener.ACTION_FEEDBACK;
import static com.example.greenstream.adapters.InformationAdapter.ItemActionListener.ACTION_LIKE;
import static com.example.greenstream.adapters.InformationAdapter.ItemActionListener.ACTION_SHOW;
import static com.example.greenstream.adapters.InformationAdapter.ItemActionListener.ACTION_WATCH_LATER;

/**
 * Class representing the activity that feeds content to the user.
 * Manages UI states and updates data via the
 * {@link MainViewModel}.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private MainViewModel viewModel;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private View accountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        drawer = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        accountView = navigationView.getHeaderView(0);
        accountView.setOnClickListener(this::onAccountViewClicked);

        viewModel.login(this, true);
        viewModel.getAccount().observe(this, this::onAccountUpdate);

        RecyclerView informationListView = findViewById(R.id.information_list);
        informationListView.setLayoutManager(new LinearLayoutManager(this));
        List<Integer> supportedActions = Arrays.asList(
                ACTION_SHOW,
                ACTION_FEEDBACK,
                ACTION_LIKE,
                ACTION_WATCH_LATER
        );
        InformationAdapter<InformationItem> adapter = new InformationAdapter<>((action, informationItem) ->
        {
            switch (action) {
                case ACTION_SHOW:
                    viewModel.showInformation(informationItem);
                    break;
                case InformationAdapter.ItemActionListener.ACTION_FEEDBACK:
                    viewModel.showFeedbackDialog(this, informationItem);
                    break;
                case InformationAdapter.ItemActionListener.ACTION_WATCH_LATER:
                    viewModel.addToWatchLater(informationItem);
                    break;
                case InformationAdapter.ItemActionListener.ACTION_LIKE:
                    if (informationItem instanceof ExtendedInformationItem)
                        viewModel.updateLiked((ExtendedInformationItem) informationItem);
                    break;
            }
        }, viewModel::updateFeed, supportedActions);
        LiveData<List<InformationItem>> feed = viewModel.getFeed();
        feed.observe(this, adapter::setData);
        informationListView.setAdapter(adapter);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.list_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        // Reset feed when swiping down
        swipeRefreshLayout.setOnRefreshListener(viewModel::resetFeed);

        // Update progress bars according to the state of the feed
        View errorMessage = findViewById(R.id.feed_error_message);
        Button retryButton = findViewById(R.id.retry_button);
        retryButton.setOnClickListener(view -> viewModel.updateFeed());
        viewModel.getFeedState().observe(this, listState -> {
            adapter.setListState(listState);
            boolean refreshing = swipeRefreshLayout.isRefreshing();
            swipeRefreshLayout.setRefreshing(refreshing && listState == ListState.LOADING);
            boolean viewList =
                    (listState != ListState.FAILED)
                            || (feed.getValue() != null && !feed.getValue().isEmpty());
            swipeRefreshLayout.setVisibility(viewList ? VISIBLE : GONE);
            errorMessage.setVisibility(viewList ? GONE : VISIBLE);
        });

        Log.d(TAG, "Activity created");
    }

    private void onAccountUpdate(AppAccount account) {
        TextView loginName = findViewById(R.id.login_name);
        TextView loginEmail = findViewById(R.id.login_email);
        boolean hasAccount = (account != null);
        accountView.setVisibility(hasAccount ? VISIBLE : GONE);
        navigationView.getMenu().setGroupVisible(R.id.nav_group_login, !hasAccount);
        navigationView.getMenu().setGroupVisible(R.id.nav_group_view, hasAccount);
        if (hasAccount) {
            loginName.setText(account.getUsername());
            loginEmail.setText(account.getEmail());
        }
    }

    private void onAccountViewClicked(View view) {
        viewModel.logout();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings_nav_item) {
            openSettings();
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.login_nav_item) {
            viewModel.login(this, false);
            return true;
        } else {
            Intent intent = new Intent(this, ItemListActivity.class);
            if (id == R.id.watch_later_nav_item)
                intent.putExtra(ItemListActivity.PERSONAL_LIST_TYPE_EXTRA, PersonalListType.WATCH_LATER.toString());
            else if (id == R.id.liked_nav_item)
                intent.putExtra(ItemListActivity.PERSONAL_LIST_TYPE_EXTRA, PersonalListType.LIKED.toString());
            else if (id == R.id.history_nav_item)
                intent.putExtra(ItemListActivity.PERSONAL_LIST_TYPE_EXTRA, PersonalListType.HISTORY.toString());
            else
                return false;
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    /**
     * Open the {@link SettingsActivity settings} of the app.
     */
    private void openSettings() {
        Log.d(TAG, "Launching settings");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}