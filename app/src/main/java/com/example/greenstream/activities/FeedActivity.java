package com.example.greenstream.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.greenstream.R;
import com.example.greenstream.adapters.InformationAdapter;
import com.example.greenstream.data.InformationItem;
import com.example.greenstream.dialog.AppDialogBuilder;
import com.example.greenstream.viewmodels.FeedViewModel;

/**
 * Class representing the activity that feeds content to the user.
 * Manages UI states and accesses the {@link com.example.greenstream.Repository Repository} via the
 * {@link FeedViewModel}.
 */
public class FeedActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FeedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        viewModel = new ViewModelProvider(this).get(FeedViewModel.class);

        RecyclerView informationListView = findViewById(R.id.information_list);
        informationListView.setLayoutManager(new LinearLayoutManager(this));
        InformationAdapter adapter = new InformationAdapter(createItemActionListener());
        viewModel.getRecommendations().observe(this, adapter::setData);
        informationListView.setAdapter(adapter);
        Log.d(TAG, "Activity created");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.settings_menu_item)
            openSettings();
        else
            return super.onOptionsItemSelected(item);
        return true;
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
                AppDialogBuilder.feedbackDialog(FeedActivity.this, informationItem.getId())
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