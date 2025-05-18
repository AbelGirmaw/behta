package slaa;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PracticeListeningManager {
    private List<String> wordList;
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
        float accuracy=correctCount/wordarray.length;
        return "your listening task is Completed! and know you answered"+correctCount +"correctly. from"+wordarray.length+" words "+ "know your score: " +correctCount*5 +"from 50 Points" +"your listening accuracy is "  + accuracy + "%";
    }

    public String getProgressStatus() {
        if (wordarray == null || wordarray.length<1) return "";
        int accuracy=30;
        return "Progress: " + currentIndex + "/" + wordList.size() + " | Accuracy: " + accuracy + "%";
    }
}
