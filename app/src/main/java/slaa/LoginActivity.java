package slaa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    private DatabaseManager dbManager;
    private EditText  usernameEditText, passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialize database manager
        dbManager = new DatabaseManager(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize views
        usernameEditText = findViewById(R.id.loginUserName);
        passwordEditText = findViewById(R.id.loginPassword);
    }

    public void authenticateUser(View view) {
            try{
                String username = usernameEditText.getText() != null ? usernameEditText.getText().toString().trim() : "";
                String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";


              // Attempt to insert user into database
            boolean is_authenticate = dbManager.authenticateUser(username, password);

            if (!is_authenticate) {
                Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(LoginActivity.this, TeacherDashboard.class);
                startActivity(intent);
            }

        } catch (Exception e) {
            Toast.makeText(this,"Authentication error", Toast.LENGTH_LONG).show();
        }
    }
}