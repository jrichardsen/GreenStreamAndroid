package de.deingreenstream.app.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.greenstream.R;
import de.deingreenstream.app.preferences.TimePreference;
import de.deingreenstream.app.preferences.TimePreferenceDialogFragmentCompat;

/**
 * Class representing the activity that manages the settings of the app.
 *
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    private static final int TIME_PREF_REQUEST_CODE = 0;
    private static final String TIME_PREF_TAG = "TimePreferenceDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Log.d(TAG, "Activity created");
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        /**
         * Set custom procedure for launching the {@link TimePreference} dialog.
         * @param preference If this is a {@link TimePreference}, the respective custom
         *                   {@link TimePreferenceDialogFragmentCompat dialog} will be created,
         *                   otherwise it will use the default behaviour.
         */
        @Override
        public void onDisplayPreferenceDialog(Preference preference) {
            if (preference instanceof TimePreference) {
                DialogFragment dialogFragment = TimePreferenceDialogFragmentCompat
                        .createFromPreference((TimePreference) preference);
                dialogFragment.setTargetFragment(this, TIME_PREF_REQUEST_CODE);
                dialogFragment.show(getParentFragmentManager(), TIME_PREF_TAG);
            } else {
                super.onDisplayPreferenceDialog(preference);
            }
        }
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // React to UI back button (in top bar) as if physical back button was pressed
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}