package com.example.greenstream.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenstream.R;
import com.example.greenstream.data.InformationItem;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Adapter for displaying information items within a {@link RecyclerView}.
 */
public class InformationAdapter
        extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_PROGRESS_BAR = 1;

    private List<InformationItem> data = Collections.emptyList();
    /**
     * The view holder for the view that was last clicked.
     * This view is the only one that might show their actions.
     */
    private InformationViewHolder lastSelectedViewHolder = null;
    private boolean showProgressBar = false;
    private final ItemActionListener listener;
    private final LoadingListener loadingListener;

    public InformationAdapter(ItemActionListener itemClickListener, LoadingListener loadingListener) {
        listener = itemClickListener;
        this.loadingListener = loadingListener;
    }

    public void setData(@NotNull List<InformationItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size())
            return TYPE_PROGRESS_BAR;
        else return TYPE_ITEM;
    }

    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ITEM)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.information_item, parent, false);
        else
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar, parent, false);
        return new InformationViewHolder(view, viewType == TYPE_ITEM, this::onItemClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_ITEM)
            holder.bind(data.get(position), listener);
        else if (viewType == TYPE_PROGRESS_BAR)
            // load new data as soon as the progress bar is bound to a view holder
            loadingListener.loadMoreData();
    }

    @Override
    public int getItemCount() {
        // Get one extra item for showing the progress bar
        return data.size() + (showProgressBar ? 1 : 0);
    }

    /**
     * If a view was clicked, toggle the visibility of its actions.
     * If another view was showing their actions beforehand, these must now be set invisible.
     *
     * @param viewHolder The view holder that was clicked
     */
    private void onItemClicked(InformationViewHolder viewHolder) {
        if (viewHolder.equals(lastSelectedViewHolder))
            // Toggle visibility for actions
            viewHolder.setActionsVisible(!viewHolder.areActionsVisible());
        else {
            if (lastSelectedViewHolder != null)
                // Set actions of the last view invisible
                lastSelectedViewHolder.setActionsVisible(false);
            viewHolder.setActionsVisible(true);
            lastSelectedViewHolder = viewHolder;
        }
    }

    public void setShowProgressBar(boolean showProgressBar) {
        if (this.showProgressBar != showProgressBar) {
            this.showProgressBar = showProgressBar;
            notifyDataSetChanged();
        }
    }

    public interface ItemListener {
        void onItemClicked(InformationViewHolder viewHolder);
    }

    public interface ItemActionListener {

        void onFeedbackAction(InformationItem informationItem);

        void onShowAction(InformationItem informationItem);
    }

    public interface LoadingListener {
        void loadMoreData();
    }

    /**
     * Class representing the view holder of this adapter.
     */
    static class InformationViewHolder extends RecyclerView.ViewHolder {

        private TextView titleText;
        private TextView typeText;
        private TextView descriptionText;
        private LinearLayout actionLayout;
        private Button feedbackButton;
        private Button showButton;
        private boolean actionsVisible;

        InformationViewHolder(@NonNull View itemView, boolean hasItem, ItemListener listener) {
            super(itemView);
            if (hasItem) {
                itemView.setOnClickListener(view -> listener.onItemClicked(this));
                titleText = itemView.findViewById(R.id.title_text);
                typeText = itemView.findViewById(R.id.type_text);
                descriptionText = itemView.findViewById(R.id.description_text);
                actionLayout = itemView.findViewById(R.id.information_action_layout);
                feedbackButton = itemView.findViewById(R.id.feedback_button);
                showButton = itemView.findViewById(R.id.show_button);
            }
        }

        private void bind(@NotNull InformationItem informationItem, ItemActionListener listener) {
            titleText.setText(informationItem.getTitle());
            typeText.setText(informationItem.getType().getName());
            descriptionText.setText(informationItem.getDescription());
            setActionsVisible(false);
            feedbackButton.setOnClickListener(view -> listener.onFeedbackAction(informationItem));
            showButton.setOnClickListener(view -> listener.onShowAction(informationItem));
        }

        private void setActionsVisible(boolean visible) {
            actionsVisible = visible;
            actionLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
        }

        boolean areActionsVisible() {
            return actionsVisible;
        }
    }
}
