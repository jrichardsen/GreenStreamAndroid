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

import java.util.List;

/**
 * Adapter for displaying information items within a {@link RecyclerView}.
 */
public class InformationAdapter
        extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder> {

    private List<InformationItem> data;
    /**
     * The view holder for the view that was last clicked.
     * This view is the only one that might show their actions.
     */
    private InformationViewHolder lastSelectedViewHolder = null;
    private final ItemActionListener listener;

    public InformationAdapter(ItemActionListener itemClickListener) {
        listener = itemClickListener;
    }

    public void setData(List<InformationItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    protected List<InformationItem> data() {
        return data;
    }

    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.information_item, parent, false);
        return new InformationViewHolder(view, this::onItemClicked);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationViewHolder holder, int position) {
        holder.bind(data().get(position), listener);
    }

    @Override
    public int getItemCount() {
        return data().size();
    }

    /**
     * If a view was clicked, toggle the visibility of its actions.
     * If another view was showing their actions beforehand, these must now be set invisible.
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

    public interface ItemListener {
        void onItemClicked(InformationViewHolder viewHolder);
    }

    public interface ItemActionListener {

        void onFeedbackAction(InformationItem informationItem);

        void onShowAction(InformationItem informationItem);
    }

    /**
     * Class representing the view holder of this adapter.
     */
    static class InformationViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleText;
        private final TextView typeText;
        private final TextView descriptionText;
        private final LinearLayout actionLayout;
        private final Button feedbackButton;
        private final Button showButton;
        private boolean actionsVisible;

        InformationViewHolder(@NonNull View itemView, ItemListener listener) {
            super(itemView);

            itemView.setOnClickListener(view -> listener.onItemClicked(this));
            titleText = itemView.findViewById(R.id.title_text);
            typeText = itemView.findViewById(R.id.type_text);
            descriptionText = itemView.findViewById(R.id.description_text);
            actionLayout = itemView.findViewById(R.id.information_action_layout);
            feedbackButton = itemView.findViewById(R.id.feedback_button);
            showButton = itemView.findViewById(R.id.show_button);
        }

        private void bind(@NotNull InformationItem informationItem, ItemActionListener listener) {
            titleText.setText(informationItem.getTitle());
            typeText.setText(informationItem.getType());
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
