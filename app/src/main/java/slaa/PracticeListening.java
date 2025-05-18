package slaa;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PracticeListening extends AppCompatActivity {
    private EditText Speaking_output,statusTextView;
    private List<String> wordList;
    private int currentIndex = 0;
    private int correctCount = 0;
    private DecisionMaker decisionMaker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practice_listening);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Speaking_output=findViewById(R.id.practiceListeningOutput);
        statusTextView = findViewById(R.id.practiceListeningStatus);

        decisionMaker = new DecisionMaker(this);
        decisionMaker.setPracticeListeningActivity(this);



        getRandomWords(); // Start practice

    }




    public void getRandomWords() {
        String result = PythonHelper.callPythonFileManagerFunction("get_100_random_words", this);
        wordList = Arrays.asList(result.split("\\s+"));
        currentIndex = 0;
        correctCount = 0;
        nextWord();
    }


    public void checkListeningSkill(String spokenWord) {
        String originalWord = wordList.get(currentIndex).toLowerCase(Locale.ROOT);
        if (spokenWord.equals(originalWord)) {
            correctCount++;
        }
        Speaking_output.setText(spokenWord);
        currentIndex++;
        displayStatus(); // Show real-time status
        nextWord();
        finalStatus();
    }

    public String nextWord() {
        String word = "";
        if (currentIndex < wordList.size()) {
            word = wordList.get(currentIndex);


        } else {
            int score = (int) ((correctCount / (float) wordList.size()) * 100);
            Speaking_output.setText("Your pronunciation score: " + score + "%");
        }
        return word;
    }




    private void displayStatus() {
        int total = currentIndex;
        if (total == 0) total = 1;  // Avoid division by zero
        int accuracy = (int) ((correctCount / (float) total) * 100);
        statusTextView.setText("Progress: " + currentIndex + "/" + wordList.size()
                + " | Accuracy: " + accuracy + "%");
    }


    public String finalStatus(){
        String final_status="";
        if (currentIndex >= wordList.size()) {
            int finalScore = (int) ((correctCount / (float) wordList.size()) * 100);
            Speaking_output.setText("Your final pronunciation score: " + finalScore + "%");
            statusTextView.setText("Completed! Final Accuracy " + finalScore + "%");
            final_status="Your First round practice is Completed! and your final pronunciation score is " + finalScore + "%" + "and Your Final Accuracy is " + finalScore + "%";
        }
        return final_status;
    }

    public void updateUI(String spokenText, String statusText) {
        Speaking_output.setText(spokenText);
        statusTextView.setText(statusText);
    }



}