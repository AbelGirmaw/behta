package slaa;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateAccount extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, usernameEditText,
            emailEditText, passwordEditText, confirmPasswordEditText;
    private RadioGroup roleRadioGroup;
    private DatabaseManager dbManager;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database manager
        dbManager = new DatabaseManager(this);

        // Initialize views
        firstNameEditText = findViewById(R.id.firstName);
        lastNameEditText = findViewById(R.id.lastName);
        usernameEditText = findViewById(R.id.userName);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.registerPassword);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        roleRadioGroup = findViewById(R.id.roleGroup);
        registerButton = findViewById(R.id.register);


    }

    public void registerUser(View view) {
        try {
            // Get input values with null checks
            String firstName = firstNameEditText.getText() != null ? firstNameEditText.getText().toString().trim() : "";
            String lastName = lastNameEditText.getText() != null ? lastNameEditText.getText().toString().trim() : "";
            String username = usernameEditText.getText() != null ? usernameEditText.getText().toString().trim() : "";
            String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
            String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";
            String confirmPassword = confirmPasswordEditText.getText() != null ? confirmPasswordEditText.getText().toString().trim() : "";

            // Get selected role with validation
            int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
            if (selectedRoleId == -1) {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton roleRadioButton = findViewById(selectedRoleId);
            String role = roleRadioButton != null ? roleRadioButton.getText().toString() : "";

            // Validate inputs
            if (TextUtils.isEmpty(firstName)) {
                firstNameEditText.setError("First name is required");
                firstNameEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(lastName)) {
                lastNameEditText.setError("Last name is required");
                lastNameEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(username)) {
                usernameEditText.setError("Username is required");
                usernameEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError("Enter a valid email");
                emailEditText.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Password is required");
                passwordEditText.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordEditText.setError("Password must be at least 6 characters");
                passwordEditText.requestFocus();
                return;
            }

            if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Passwords do not match");
                confirmPasswordEditText.requestFocus();
                return;
            }

            // Check if username already exists
            if (dbManager.checkUsername(username)) {
                usernameEditText.setError("Username already taken");
                usernameEditText.requestFocus();
                return;
            }

            // Check if email already exists
            if (dbManager.checkEmail(email)) {
                emailEditText.setError("Email already registered");
                emailEditText.requestFocus();
                return;
            }

            // Attempt to insert user into database
            long id = dbManager.insertUser(firstName, lastName, username, email, role, password);

            if (id != -1) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                clearForm();
            } else {
                Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.e("RegistrationError", "Error during registration", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void clearForm() {
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        usernameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
        roleRadioGroup.clearCheck();
    }

    @Override
    protected void onDestroy() {
        dbManager.close();
        super.onDestroy();
    }
}

