package com.example.greenstream.dialog;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import com.example.greenstream.R;
import com.example.greenstream.Repository;
import com.example.greenstream.data.Feedback;
import com.example.greenstream.data.Label;

import java.util.List;

/**
 * Utility class for creating dialogs.
 */
public class AppDialogBuilder {

    /**
     * Creates a feedback dialog in the given context for the information item with the given id.
     * A selection between predefined options and a custom text feedback can be made.
     */
    public static AlertDialog feedbackDialog(Context context, long id, List<Label> labels, Repository.ResponseListener<Feedback> listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] items = new String[labels.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = labels.get(i).getName();
        }

        return builder.setTitle(R.string.feedback)
                .setItems(items, (dialogInterface, i) -> {
                    Label label = labels.get(i);
                    if (label.getId() == 1)
                        feedbackTextDialog(context, id, label, listener).show();
                    else
                        listener.onResponse(new Feedback(id, label, ""));
                })
                .create();
    }

    /**
     * Creates a custom text feedback dialog in the given context for the information item with the
     * given id.
     */
    private static AlertDialog feedbackTextDialog(Context context, long id, Label label, Repository.ResponseListener<Feedback> listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setSingleLine(false);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(250)});
        input.setMaxLines(4);
        input.setGravity(Gravity.START | Gravity.TOP);
        input.setHorizontalScrollBarEnabled(false);

        @SuppressLint("InflateParams")
        FrameLayout view = (FrameLayout) LayoutInflater.from(context)
                .inflate(R.layout.dialog_content_view_frame, null);
        view.addView(input);

        return builder.setTitle(R.string.feedback)
                .setMessage(R.string.information_feedback_message)
                .setView(view)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.send, ((dialogInterface, i) ->
                        listener.onResponse(new Feedback(id, label, input.getText().toString()))))
                .create();
    }

    public static AlertDialog accountSelectionDialog(Activity activity, Account[] accounts, Repository.ResponseListener<Account> responseListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String[] items = new String[accounts.length + 1];
        for (int i = 0; i < accounts.length; i++) {
            items[i] = accounts[i].name;
        }
        items[accounts.length] = activity.getString(R.string.add_account);

        return builder.setTitle(R.string.account_activity_title)
                .setItems(items, (dialogInterface, i) ->
                        responseListener.onResponse((i < accounts.length) ? accounts[i] : null))
                .create();
    }

    public static AlertDialog registerDialog(Context context, Repository.ResponseListener<Void> responseListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        @SuppressLint("InflateParams")
        FrameLayout view = (FrameLayout) LayoutInflater.from(context)
                .inflate(R.layout.dialog_content_view_frame, null);
        View privacySwitch = LayoutInflater.from(context)
                .inflate(R.layout.privacy_switch, view);
        ((TextView) privacySwitch.findViewById(R.id.privacy_text)).setMovementMethod(LinkMovementMethod.getInstance());

        AlertDialog dialog = builder.setTitle(R.string.register)
                .setView(view)
                .setPositiveButton(android.R.string.ok, ((dialogInterface, i) -> responseListener.onResponse(null)))
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        ((SwitchCompat) privacySwitch.findViewById(R.id.privacy_switch))
                .setOnCheckedChangeListener((compoundButton, b) ->
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(b));
        dialog.setOnShowListener(dialogInterface ->
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false));

        return dialog;
    }

    /**
     * Creates {@link Toast} messages to notify the user whether the feedback could be send.
     */
    public static Repository.FeedbackReceivedCallback getCallbackFromContext(Context context) {
        return new Repository.FeedbackReceivedCallback() {
            @Override
            public void onFeedbackReceivedSuccess() {
                Toast.makeText(context, R.string.feedback_received_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFeedbackFailed() {
                Toast.makeText(context,
                        R.string.feedback_received_failed,
                        Toast.LENGTH_SHORT
                ).show();
            }
        };
    }
}
