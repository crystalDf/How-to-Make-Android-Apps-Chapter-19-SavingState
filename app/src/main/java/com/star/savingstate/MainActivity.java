package com.star.savingstate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private EditText mNotesEditText;
    private Button mSettingsButton;

    private static final int SETTINGS_INFO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotesEditText = (EditText) findViewById(R.id.multi_edit_text);

        if (savedInstanceState != null) {
            String notes = savedInstanceState.getString("NOTES");
            mNotesEditText.setText(notes);
        }

        String notes = getSharedPreferences("State", Context.MODE_PRIVATE)
                .getString("NOTES", "");

        if (!TextUtils.isEmpty(notes)) {
            mNotesEditText.setText(notes);
        }

        mSettingsButton = (Button) findViewById(R.id.settings_button);

        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_INFO);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("NOTES", mNotesEditText.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {

        saveSettings();

        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_INFO && resultCode == RESULT_OK) {
            updateNotesText();
        }
    }

    private void saveSettings() {
        SharedPreferences.Editor editor =
                getSharedPreferences("State", Context.MODE_PRIVATE).edit();

        editor.putString("NOTES", mNotesEditText.getText().toString()).commit();
    }

    private void updateNotesText() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean("pref_text_bold", false)) {
            mNotesEditText.setTypeface(null, Typeface.BOLD);
        } else {
            mNotesEditText.setTypeface(null, Typeface.NORMAL);
        }

        String textSize = sharedPreferences.getString("pref_text_size", "16");
        mNotesEditText.setTextSize(Float.parseFloat(textSize));
    }
}
