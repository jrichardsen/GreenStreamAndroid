package com.example.greenstream.dialog;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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
