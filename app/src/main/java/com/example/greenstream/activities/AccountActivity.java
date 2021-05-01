package com.example.greenstream.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.greenstream.R;
import com.example.greenstream.authentication.AppAccount;
import com.example.greenstream.data.SelectableTopic;

import java.util.List;

public class AccountActivity extends AppCompatActivity {

    public static final String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";

    private AppAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle(R.string.account_activity_title);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        account = getIntent().getParcelableExtra(EXTRA_ACCOUNT);

        TextView accountName = findViewById(R.id.account_name);
        accountName.setText(account.getUsername());

        TextView accountEmail = findViewById(R.id.account_email);
        accountEmail.setText(account.getEmail());

        LinearLayout topicList = findViewById(R.id.topic_list);
        List<SelectableTopic> topics = account.getTopics();
        for (int i = 0; i < topics.size(); i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            checkBox.setText(topics.get(i).getName());
            checkBox.setChecked(topics.get(i).isSelected());
            checkBox.setTag(i);
            checkBox.setOnCheckedChangeListener((compoundButton, isChecked) ->
                    topics.get((Integer) compoundButton.getTag()).setSelected(isChecked));
            topicList.addView(checkBox);
        }

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(this::onLogout);

        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this::onCancel);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this::onSave);
    }

    private void onSave(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(EXTRA_ACCOUNT, account);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void onCancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void onLogout(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.log_out)
                .setMessage(R.string.log_out_question)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(EXTRA_ACCOUNT, (Parcelable) null);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onCancel(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}