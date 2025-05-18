package slaa;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StudentLoginPage extends AppCompatActivity {
    int currentValue = 0;
    private EditText studentPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        studentPassword=findViewById(R.id.studentPassword);
    }
    // Clears everything
    private void clearAll() {
        currentValue = 0;
        studentPassword.setText("0");
    }

    // Removes the last digit
    private void clear() {
        String text = studentPassword.getText().toString();
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
            if (text.equals("")) {
                currentValue = 0;
                studentPassword.setText("0");
            } else {
                currentValue = Integer.parseInt(text);
                studentPassword.setText(text);
            }
        }
    }

    // Increases number by 1 up to 9
    private void increaseByOne() {
        if (currentValue < 9) {
            currentValue += 1;
            studentPassword.setText(String.valueOf(currentValue));
        }
    }

    // Increases number by 2 up to 9
    private void increaseByTwo() {
        if (currentValue < 9) {
            currentValue += 2;
            if (currentValue > 9) currentValue = 9;
            studentPassword.setText(String.valueOf(currentValue));
        }
    }
}