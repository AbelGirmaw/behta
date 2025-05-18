package slaa;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PracticeSpeakingManager {
    private int currentIndex = 0;
    private int correctCount = 0;
    private String[] wordarray;

    public void getRandomWords(String rawWords) {
        wordarray = rawWords.split(" "); // This allocates the needed array size
        currentIndex = 0;
        correctCount = 0;
    }


    public String checkListeningSkill(String spokenWord) {
        String originalWord = wordarray[currentIndex].toLowerCase();
        if (spokenWord.equals(originalWord)) {
            correctCount++;
        }
        currentIndex++;
        return nextWord();
    }

    public String nextWord() {
        if (wordarray.length > currentIndex) {
            return wordarray[currentIndex];
        } else {
            return null; // end of list
        }
    }

    public String getFinalStatus() {

        if (wordarray == null || wordarray.length<1) return "No words practiced.";
        float accuracy=correctCount*10;
        return "your speaking task is Completed! and know you answered and pronounce"+correctCount +" words correctly. from"+wordarray.length+" words "+ "know your score: " +correctCount*5 +"from 50 Points" +"your speaking accuracy is "  + accuracy + "%";
    }

    public String getProgressStatus() {
        if (wordarray == null || wordarray.length<1) return "";
        int accuracy=30;
        return "Progress: " + currentIndex + "/" + wordarray.length + " | Accuracy: " + accuracy + "%";
    }
}

