package slaa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity
        implements SpeechRecognizer.RecognitionListener {

    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private SpeechRecognizer speechRecognizer;
    private TextView resultView;
    private TextView tvOutput;
    private DecisionMaker decisionMaker;
    private TextRecognizer textRecognizer;
    private boolean is_speak= false;
    private String is_close="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultView = findViewById(R.id.result_text);
        resultView.setMovementMethod(new ScrollingMovementMethod());
        tvOutput = findViewById(R.id.tv_output);

        // Initialize components
        textRecognizer = new TextRecognizer(this);
        decisionMaker = new DecisionMaker(this, resultView);
        textRecognizer.setDecisionMaker(decisionMaker);
        decisionMaker.setTextRecognizer(textRecognizer);
        is_close=decisionMaker.is_close;
        // Initialize Python
        PythonHelper.init(this);
        // Check permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startRecognition();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_REQUEST_RECORD_AUDIO);
        }

    }

    private void startRecognition() {
        speechRecognizer = new SpeechRecognizer(this, this);
        speechRecognizer.initModel("model-en-us");
    }

    @Override
    public void onResult(String result) {
        String processed_command=result;
        if(is_speak){
            processed_command="";
            is_speak=false;;
        }
        else if (processed_command.length()>18){
            is_speak=true;
            textRecognizer.processText(processed_command);
            // Send recognized text to TextEditorActivity
            Intent broadcastIntent = new Intent("SPEECH_RESULT");
            broadcastIntent.putExtra("recognized_text", processed_command);
            sendBroadcast(broadcastIntent);

        }

    }
    public void closeApp() {
        finishAffinity(); // Recommended
        System.exit(0);   // Forcefully terminates the process (use with caution)
    }
    @Override
    public void onPartialResult(String result) {

    }

    @Override
    public void onFinalResult(String result) {
//
    }

    @Override
    public void onError(String message) {
        runOnUiThread(() -> {
//            resultView.append("[Error] " + message + "\n");
            scrollToBottom();
        });
    }

    private void scrollToBottom() {
        resultView.post(() -> {
            int scrollAmount = resultView.getLayout().getLineTop(resultView.getLineCount())
                    - resultView.getHeight();
            if (scrollAmount > 0) {
                resultView.scrollTo(0, scrollAmount);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
        if (textRecognizer != null) {
            textRecognizer.shutdown();
        }
        super.onDestroy();
    }

    public void goToUserMainu(View view) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}