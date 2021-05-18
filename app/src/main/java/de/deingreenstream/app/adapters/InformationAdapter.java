package de.deingreenstream.app.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.greenstream.R;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;

import de.deingreenstream.app.data.ExtendedInformationItem;
import de.deingreenstream.app.data.InformationItem;
import de.deingreenstream.app.data.ListState;

/**
 * Adapter for displaying information items within a {@link RecyclerView}.
 */
public class InformationAdapter<T extends InformationItem>
        extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_PROGRESS_BAR = 1;
    private static final int TYPE_ERROR_MESSAGE = 2;

    private List<T> data = Collections.emptyList();
    private int footerType = 0;
    private final ItemActionListener listener;
    private final LoadingListener loadingListener;
    private final List<Integer> supportedActions;

    public InformationAdapter(ItemActionListener itemClickListener, LoadingListener loadingListener, List<Integer> supportedActions) {
        listener = itemClickListener;
        this.loadingListener = loadingListener;
        this.supportedActions = supportedActions;
    }

    public void setData(@NotNull List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size())
            return footerType;
        else return TYPE_ITEM;
    }

    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_ITEM)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.information_item, parent, false);
        else if (viewType == TYPE_PROGRESS_BAR)
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar, parent, false);
        else
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.error_item, parent, false);
        return new InformationViewHolder(view, viewType, listener, supportedActions);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_ITEM)
            holder.bind(data.get(position));
        else if (viewType == TYPE_PROGRESS_BAR)
            // load new data as soon as the progress bar is bound to a view holder
            loadingListener.loadMoreData();
        else if (viewType == TYPE_ERROR_MESSAGE)
            holder.bindErrorItem(loadingListener);
    }

    @Override
    public int getItemCount() {
        // Get one extra item for showing the progress bar
        return data.size() + ((footerType != 0) ? 1 : 0);
    }

    public void setListState(ListState listState) {
        int footerType = 0;
        if (listState == ListState.LOADING || listState == ListState.READY)
            footerType = TYPE_PROGRESS_BAR;
        else if (listState == ListState.FAILED)
            footerType = TYPE_ERROR_MESSAGE;
        if (this.footerType != footerType) {
            this.footerType = footerType;
            notifyDataSetChanged();
        }
    }

    public interface ItemActionListener {

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({ACTION_SHOW, ACTION_FEEDBACK, ACTION_WATCH_LATER, ACTION_LIKE, ACTION_REMOVE_ITEM})
        @interface ActionType {}

        int ACTION_SHOW = 0;
        int ACTION_FEEDBACK = 1;
        int ACTION_WATCH_LATER = 2;
        int ACTION_LIKE = 3;
        int ACTION_REMOVE_ITEM = 4;

        void onAction(@ActionType int action, InformationItem informationItem);
    }

    public interface LoadingListener {
        void loadMoreData();
    }

    /**
     * Class representing the view holder of this adapter.
     */
    static class InformationViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        private final View itemView;
        private ImageView image;
        private TextView titleText;
        private TextView typeText;
        private TextView descriptionText;
        private ImageView likedButton;
        private ImageView overflowMenuButton;

        private Button retryButton;
        private InformationItem informationItem;
        private final ItemActionListener listener;
        private final List<Integer> supportedActions;

        InformationViewHolder(@NonNull View itemView, int viewType, ItemActionListener listener, List<Integer> supportedActions) {
            super(itemView);
            this.itemView = itemView;
            this.listener = listener;
            this.supportedActions = supportedActions;
            if (viewType == TYPE_ITEM) {
                image = itemView.findViewById(R.id.item_image);
                titleText = itemView.findViewById(R.id.title_text);
                typeText = itemView.findViewById(R.id.type_text);
                descriptionText = itemView.findViewById(R.id.description_text);
                likedButton = itemView.findViewById(R.id.liked_action_item);
                overflowMenuButton = itemView.findViewById(R.id.overflow_menu_button);
            } else if (viewType == TYPE_ERROR_MESSAGE) {
                retryButton = itemView.findViewById(R.id.retry_button);
            }
        }

        private void bind(@NotNull InformationItem informationItem) {
            this.informationItem = informationItem;
            if (supportedActions.contains(ItemActionListener.ACTION_SHOW))
                itemView.setOnClickListener(view -> listener.onAction(ItemActionListener.ACTION_SHOW, informationItem));
            if (!TextUtils.isEmpty(informationItem.getImage()))
                Glide.with(image.getContext()).load(informationItem.getImage()).into(image);
            else
                image.setImageDrawable(null);
            titleText.setText(informationItem.getTitle());
            typeText.setText(informationItem.getType().getName());
            descriptionText.setText(informationItem.getDescription());
            overflowMenuButton.setOnClickListener(this::showPopup);
            updateLikedIcon();
            likedButton.setOnClickListener(view -> {
                if (informationItem instanceof ExtendedInformationItem) {
                    ExtendedInformationItem extended = (ExtendedInformationItem) informationItem;
                    long liked = extended.getLiked();
                    if (liked == 0)
                        liked = System.currentTimeMillis() / 1000L;
                    else
                        liked = 0;
                    extended.setLiked(liked);
                    listener.onAction(ItemActionListener.ACTION_LIKE, extended);
                    updateLikedIcon();
                }
            });
        }

        private void updateLikedIcon() {
            if (supportedActions.contains(ItemActionListener.ACTION_LIKE) && this.informationItem instanceof ExtendedInformationItem) {
                likedButton.setVisibility(View.VISIBLE);
                likedButton.setImageDrawable(ResourcesCompat.getDrawable(itemView.getResources(),
                        (((ExtendedInformationItem) informationItem).getLiked() != 0)
                                ? R.drawable.ic_liked_on_24dp
                                : R.drawable.ic_liked_off_24dp
                        , null));
            } else
                likedButton.setVisibility(View.GONE);
        }

        private void showPopup(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.item_actions_menu);
            Menu menu = popup.getMenu();
            boolean extended = informationItem instanceof ExtendedInformationItem;
            menu.findItem(R.id.feedback_action_item).setVisible(
                    supportedActions.contains(ItemActionListener.ACTION_FEEDBACK)
            );
            menu.findItem(R.id.watch_later_action_item).setVisible(
                    supportedActions.contains(ItemActionListener.ACTION_WATCH_LATER)
                            && extended
            );
            menu.findItem(R.id.remove_item_action).setVisible(
                    supportedActions.contains(ItemActionListener.ACTION_REMOVE_ITEM)
            );
            popup.show();
        }

        private void bindErrorItem(LoadingListener loadingListener) {
            retryButton.setOnClickListener(view -> loadingListener.loadMoreData());
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int id = menuItem.getItemId();
            if (id == R.id.feedback_action_item) {
                listener.onAction(ItemActionListener.ACTION_FEEDBACK, informationItem);
                return true;
            }
            if (id == R.id.watch_later_action_item) {
                listener.onAction(ItemActionListener.ACTION_WATCH_LATER, informationItem);
                return true;
            }
            if (id == R.id.remove_item_action) {
                listener.onAction(ItemActionListener.ACTION_REMOVE_ITEM, informationItem);
            }
            return false;
        }
    }
}
