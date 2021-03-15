package com.example.greenstream.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.greenstream.R;
import com.example.greenstream.adapters.InformationAdapter;
import com.example.greenstream.data.Information;
import com.example.greenstream.dialog.AppDialogBuilder;
import com.example.greenstream.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        RecyclerView informationListView = findViewById(R.id.information_list);
        informationListView.setLayoutManager(new LinearLayoutManager(this));
        InformationAdapter adapter = new InformationAdapter(createItemActionListener());
        viewModel.getRecommendations().observe(this, adapter::setData);
        informationListView.setAdapter(adapter);
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

    private InformationAdapter.ItemActionListener createItemActionListener() {
        return new InformationAdapter.ItemActionListener() {
            @Override
            public void onFeedbackAction(Information information) {
                AppDialogBuilder.feedbackDialog(MainActivity.this, information.getId()).show();
            }

            @Override
            public void onShowAction(Information information) {
                viewModel.showInformation(information);
            }
        };
    }

    /**
     * Start the settings activity
     */
    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}