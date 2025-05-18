package slaa;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.util.Locale;

public class TextRecognizer implements TextToSpeech.OnInitListener {
    private static final String TAG = "TextRecognizer";
    private TextToSpeech textToSpeech;
    private DecisionMaker decisionMaker;
    private Context context;

    public TextRecognizer(Context context) {
        this.context = context;
        this.textToSpeech = new TextToSpeech(context, this);
    }

    public void setDecisionMaker(DecisionMaker decisionMaker) {
        this.decisionMaker = decisionMaker;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language not supported");
            }
        } else {
            Log.e(TAG, "TextToSpeech initialization failed");
        }
    }

    public void processText(String text) {
        if (decisionMaker != null) {
            decisionMaker.processResult(text);
        }
    }

    public void speak(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}