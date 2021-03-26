package com.example.greenstream.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.greenstream.R;
import com.example.greenstream.adapters.InformationAdapter;
import com.example.greenstream.data.InformationItem;
import com.example.greenstream.dialog.AppDialogBuilder;
import com.example.greenstream.viewmodels.FeedViewModel;
import com.google.android.material.navigation.NavigationView;

/**
 * Class representing the activity that feeds content to the user.
 * Manages UI states and accesses the {@link com.example.greenstream.Repository Repository} via the
 * {@link FeedViewModel}.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private FeedViewModel viewModel;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView informationListView = findViewById(R.id.information_list);
        informationListView.setLayoutManager(new LinearLayoutManager(this));
        InformationAdapter adapter = new InformationAdapter(createItemActionListener());
        viewModel.getRecommendations().observe(this, adapter::setData);
        informationListView.setAdapter(adapter);
        Log.d(TAG, "Activity created");
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
            return true;
        }
        return false;
    }

    /**
     * Creates the listener that listens to actions on the information items, namely
     * - showing the information
     * - giving feedback on the information
     */
    private InformationAdapter.ItemActionListener createItemActionListener() {
        return new InformationAdapter.ItemActionListener() {
            @Override
            public void onFeedbackAction(InformationItem informationItem) {
                AppDialogBuilder.feedbackDialog(MainActivity.this, informationItem.getId())
                        .show();
            }

            @Override
            public void onShowAction(InformationItem informationItem) {
                viewModel.showInformation(informationItem);
            }
        };
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