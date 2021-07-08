package de.deingreenstream.app.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import de.deingreenstream.app.R;
import de.deingreenstream.app.authentication.AppAccount;
import de.deingreenstream.app.data.SelectableTopic;

public class AccountActivity extends AppCompatActivity {

    public static final String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";

    private AppAccount account;
    private EditText accountName;

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

        accountName = findViewById(R.id.account_name);
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

        accountName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveButton.setEnabled(!TextUtils.isEmpty(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });
    }

    private void onSave(View view) {
        Intent returnIntent = new Intent();
        account.setUsername(accountName.getText().toString());
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