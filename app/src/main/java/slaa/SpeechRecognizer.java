package slaa;

import android.content.Context;
import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.android.RecognitionListener;
import org.vosk.android.SpeechService;
import org.vosk.android.StorageService;
import java.io.IOException;

public class SpeechRecognizer implements RecognitionListener {
    private Model model;
    private SpeechService speechService;
    private final Context context;
    private final RecognitionListener listener;

    public interface RecognitionListener {
        void onPartialResult(String result);
        void onFinalResult(String result);
        void onError(String message);
        void onResult(String result);
    }

    public SpeechRecognizer(Context context, RecognitionListener listener) {
        this.context = context.getApplicationContext();
        this.listener = listener;
    }

    public void initModel(String modelPath) {
        StorageService.unpack(context, modelPath, "model",
                model -> {
                    this.model = model;
                    startListening();
                },
                error -> listener.onError("Model error: " + error.getMessage()));
    }

    public void startListening() {
        try {
            Recognizer rec = new Recognizer(model, 16000.0f);
            speechService = new SpeechService(rec, 16000.0f);
            speechService.startListening(this);
        } catch (IOException e) {
            listener.onError("Recognition error: " + e.getMessage());
        }
    }

    public void stopListening() {
        if (speechService != null) {
            speechService.stop();
            speechService = null;
        }
    }

    @Override
    public void onPartialResult(String hypothesis) {
        String text = extractTextFromJson(hypothesis);
        listener.onPartialResult(text);
    }

    @Override
    public void onFinalResult(String hypothesis) {
        String text = extractTextFromJson(hypothesis);
        listener.onFinalResult(text);
    }

    @Override
    public void onError(Exception e) {
        listener.onError("Error: " + e.getMessage());
    }

    private String extractTextFromJson(String voskOutput) {
return voskOutput;
         }

    // Unused but required methods
    @Override public void onResult(String hypothesis) {
        String text = extractTextFromJson(hypothesis);
        listener.onResult(text);
    }
    @Override public void onTimeout() {}
}