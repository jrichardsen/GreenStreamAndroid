package com.example.greenstream.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.greenstream.R;
import com.example.greenstream.adapters.InformationAdapter;
import com.example.greenstream.data.ExtendedInformationItem;
import com.example.greenstream.data.InformationItem;
import com.example.greenstream.data.ListState;
import com.example.greenstream.data.PersonalListType;
import com.example.greenstream.dialog.AppDialogBuilder;
import com.example.greenstream.viewmodels.ItemListViewModel;

public class ItemListActivity extends AppCompatActivity {

    public static final String PERSONAL_LIST_TYPE_EXTRA = "PERSONAL_LIST_TYPE_EXTRA";

    private ItemListViewModel viewModel;
    private PersonalListType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        type = PersonalListType.valueOf(getIntent().getStringExtra(PERSONAL_LIST_TYPE_EXTRA));
        setTitle(type.getTitle());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(ItemListViewModel.class);

        RecyclerView informationListView = findViewById(R.id.information_list);
        informationListView.setLayoutManager(new LinearLayoutManager(this));
        InformationAdapter<ExtendedInformationItem> adapter = new InformationAdapter<>(createItemActionListener(),
                () -> viewModel.updatePersonalList(type));
        viewModel.getPersonalList(type).observe(this, adapter::setData);
        informationListView.setAdapter(adapter);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.list_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        // Reset feed when swiping down
        swipeRefreshLayout.setOnRefreshListener(viewModel::resetPersonalList);

        // Update progress bars according to the state of the feed
        viewModel.getPersonalListState().observe(this, listState -> {
            adapter.setShowProgressBar(listState != ListState.COMPLETED);
            boolean refreshing = swipeRefreshLayout.isRefreshing();
            swipeRefreshLayout.setRefreshing(refreshing && listState == ListState.LOADING);
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                AppDialogBuilder.feedbackDialog(ItemListActivity.this, informationItem.getId())
                        .show();
            }

            @Override
            public void onShowAction(InformationItem informationItem) {
                viewModel.showInformation(informationItem);
            }
        };
    }
}