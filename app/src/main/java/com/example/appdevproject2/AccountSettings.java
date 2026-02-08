package com.example.appdevproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountSettings extends AppCompatActivity {

    // I-declare ang mga UI elements
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private Button btnEdit;

    // Para sa pag-save ng data
    private SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS_NAME = "user_profile";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Tiyaking tama ang layout file na naka-set dito
        setContentView(R.layout.activity_account_settings);

        // I-initialize ang SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);

        // I-bind ang mga view mula sa XML
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        btnEdit = findViewById(R.id.btnEdit);

        // Kuhanin at ipakita ang naka-save na data
        loadUserProfile();

        // Mag-set ng listener para sa Edit button
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
        findViewById(R.id.returnbtn).setOnClickListener(v -> finish());
    }

    /**
     * Kinukuha ang naka-save na first name at last name mula sa SharedPreferences
     * at inilalagay ito sa mga EditText.
     */
    private void loadUserProfile() {
        String firstName = sharedPreferences.getString(KEY_FIRST_NAME, "");
        String lastName = sharedPreferences.getString(KEY_LAST_NAME, "");

        editTextFirstName.setText(firstName);
        editTextLastName.setText(lastName);
    }


    /**
     * Isine-save ang mga bagong value mula sa EditText papuntang SharedPreferences.
     */
    private void saveUserProfile() {
        // Kunin ang text mula sa EditTexts
        String newFirstName = editTextFirstName.getText().toString().trim();
        String newLastName = editTextLastName.getText().toString().trim();

        // I-validate kung hindi blangko ang mga field
        if (newFirstName.isEmpty() || newLastName.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // I-save ang data gamit ang SharedPreferences Editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST_NAME, newFirstName);
        editor.putString(KEY_LAST_NAME, newLastName);
        editor.apply(); // I-apply ang changes

        // Magpakita ng confirmation message
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
    }
}
