package slaa;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class AbelTextEditor extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private EditText editText;
    private Button btnSave, btnOpen, btnClear;
    private Button btnPythonSave, btnPythonOpen, btnRandomWords;

    private DecisionMaker decisionMaker;
    private TextRecognizer textRecognizer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_abel_text_editor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize components
        textRecognizer = new TextRecognizer(this);
        decisionMaker = new DecisionMaker(this, editText);
        textRecognizer.setDecisionMaker(decisionMaker);
        decisionMaker.setTextRecognizer(textRecognizer);


        editText = findViewById(R.id.editText);
        btnSave = findViewById(R.id.btnSave);
        btnOpen = findViewById(R.id.btnOpen);
        btnClear = findViewById(R.id.btnClear);
        btnPythonSave = findViewById(R.id.btnPythonSave);
        btnPythonOpen = findViewById(R.id.btnPythonOpen);
        btnRandomWords = findViewById(R.id.btnRandomWords);

        TextAppender.setEditText(editText);
        TextAppender.setContext(this);  // So we can use context in Chaquopy call

        // Simulate call from another class
        DecisionMaker.writeDown("","normal", "black", "white");


    }
    public void saveFile(View view) {
        final String[] filename = {"note.txt"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save As");

        final EditText input = new EditText(this);
        input.setText(filename[0]);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            filename[0] = input.getText().toString();
            try {
                FileOutputStream fos = openFileOutput(filename[0], MODE_PRIVATE);
                fos.write(editText.getText().toString().getBytes());
                fos.close();
                showMessage("File saved successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("Error saving file!");
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public void openFile(View view) {
        final String[] files = fileList();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Open File");

        builder.setItems(files, (dialog, which) -> {
            String filename = files[which];
            try {
                FileInputStream fis = openFileInput(filename);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                editText.setText(sb.toString());
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("Error opening file!");
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public void pythonSaveFile(View view) {

        String text = editText.getText().toString();
        String result=PythonHelper.callPythonFileManagerFunction("write_text_to_file", this, text);
        showMessage(result);
    }
    private void promptFilenameAndSendToPython() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save As");

        final EditText input = new EditText(this);
        input.setText("note.txt");
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String filename = input.getText().toString().trim();
            if (!filename.isEmpty()) {
                String text = editText.getText().toString();
                String result = PythonHelper.callPythonFileManagerFunction(
                        "write_text_to_file", this, text, filename  // Pass both text and filename
                );
                showMessage(result);
            } else {
                Toast.makeText(this, "Filename cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }






    private void promptFilename(Consumer<String> onFilenameEntered) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter File Name");

        final EditText input = new EditText(this);
        input.setText("note.txt");
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String filename = input.getText().toString().trim();
            if (!filename.isEmpty()) {
                onFilenameEntered.accept(filename); // This is your "return"
            } else {
                Toast.makeText(this, "Filename cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    public void pythonOpenFile(View view) {
        String result=PythonHelper.callPythonFileManagerFunction("read_text_from_file", this);
        editText.setText(result);
    }

    public void getRandomWords(View view) {
        String result=PythonHelper.callPythonFileManagerFunction("get_100_random_words", this);
        editText.setText(result);
    }

    private void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}