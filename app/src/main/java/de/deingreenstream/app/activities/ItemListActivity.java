package de.deingreenstream.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import de.deingreenstream.app.R;
import de.deingreenstream.app.adapters.InformationAdapter;
import de.deingreenstream.app.data.ExtendedInformationItem;
import de.deingreenstream.app.data.ListState;
import de.deingreenstream.app.data.PersonalListType;
import de.deingreenstream.app.viewmodels.ItemListViewModel;

import java.util.Arrays;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static de.deingreenstream.app.adapters.InformationAdapter.ItemActionListener.ACTION_FEEDBACK;
import static de.deingreenstream.app.adapters.InformationAdapter.ItemActionListener.ACTION_LIKE;
import static de.deingreenstream.app.adapters.InformationAdapter.ItemActionListener.ACTION_REMOVE_ITEM;
import static de.deingreenstream.app.adapters.InformationAdapter.ItemActionListener.ACTION_SHOW;

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
        List<Integer> supportedActions = Arrays.asList(
                ACTION_SHOW,
                ACTION_FEEDBACK,
                ACTION_LIKE,
                ACTION_REMOVE_ITEM
        );
        InformationAdapter<ExtendedInformationItem> adapter = new InformationAdapter<>((action, informationItem) ->
        {
            switch (action) {
                case ACTION_SHOW:
                    viewModel.showInformation(informationItem);
                    break;
                case ACTION_FEEDBACK:
                    viewModel.showFeedbackDialog(this, informationItem);
                    break;
                case ACTION_LIKE:
                    if(type == PersonalListType.LIKED)
                        viewModel.removeFromPersonalList(informationItem);
                    else
                        viewModel.updateLiked((ExtendedInformationItem) informationItem);
                    break;
                case ACTION_REMOVE_ITEM:
                    viewModel.removeFromPersonalList(informationItem);
                default:
                    break;
            }
        }, () -> viewModel.updatePersonalList(type), supportedActions);
        LiveData<List<ExtendedInformationItem>> personalList = viewModel.getPersonalList(type);
        personalList.observe(this, adapter::setData);
        informationListView.setAdapter(adapter);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.list_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        // Reset feed when swiping down
        swipeRefreshLayout.setOnRefreshListener(viewModel::resetPersonalList);

        // Update progress bars according to the state of the list
        View errorMessage = findViewById(R.id.list_error_message);
        Button retryButton = findViewById(R.id.retry_button);
        retryButton.setOnClickListener(view -> viewModel.updatePersonalList(type));
        viewModel.getPersonalListState().observe(this, listState -> {
            adapter.setListState(listState);
            boolean refreshing = swipeRefreshLayout.isRefreshing();
            swipeRefreshLayout.setRefreshing(refreshing && listState == ListState.LOADING);
            swipeRefreshLayout.setVisibility((listState != ListState.FAILED) ? VISIBLE : GONE);
            errorMessage.setVisibility((listState == ListState.FAILED) ? VISIBLE : GONE);
            boolean viewList =
                    (listState != ListState.FAILED)
                            || (personalList.getValue() != null
                            && !personalList.getValue().isEmpty());
            swipeRefreshLayout.setVisibility(viewList ? VISIBLE : GONE);
            errorMessage.setVisibility(viewList ? GONE : VISIBLE);
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
}