package com.s92077274.hasitha_all_labs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);  // this must match your XML filename

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameEditText = findViewById(R.id.editTextText);
        passwordEditText = findViewById(R.id.editTextTextPassword2);
        loginButton = findViewById(R.id.button);

        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Username and Password cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                long result = dbHelper.insertUserData(username, password);

                if (result != -1) {
                    Toast.makeText(LoginActivity.this, "Login data saved successfully! Navigating to Map...", Toast.LENGTH_SHORT).show();


                    android.content.Intent intent = new android.content.Intent(LoginActivity.this, MapActivity.class);
                    startActivity(intent);
                    finish();

                    usernameEditText.setText("");
                    passwordEditText.setText("");
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to save login data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
