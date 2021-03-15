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
import com.example.greenstream.data.Information;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder> {

    private List<Information> data;
    private InformationViewHolder lastSelectedViewHolder = null;
    private final ItemActionListener listener;

    public InformationAdapter(ItemActionListener itemClickListener) {
        listener = itemClickListener;
    }

    public void setData(List<Information> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    protected List<Information> data() {
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

    private void onItemClicked(InformationViewHolder viewHolder) {
        if (viewHolder.equals(lastSelectedViewHolder))
            viewHolder.setActionsVisible(!viewHolder.areActionsVisible());
        else {
            if (lastSelectedViewHolder != null)
                lastSelectedViewHolder.setActionsVisible(false);
            viewHolder.setActionsVisible(true);
            lastSelectedViewHolder = viewHolder;
        }
    }

    public interface ItemListener {
        void onItemClicked(InformationViewHolder viewHolder);
    }

    public interface ItemActionListener {

        void onFeedbackAction(Information information);

        void onShowAction(Information information);
    }

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

        private void bind(@NotNull Information information, ItemActionListener listener) {
            titleText.setText(information.getTitle());
            typeText.setText(information.getType());
            descriptionText.setText(information.getDescription());
            setActionsVisible(false);
            feedbackButton.setOnClickListener(view -> listener.onFeedbackAction(information));
            showButton.setOnClickListener(view -> listener.onShowAction(information));
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
