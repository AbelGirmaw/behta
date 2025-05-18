package slaa;

import android.content.Context;

// UIUpdateListener.java
public interface UIUpdateListener {
    void updateOutput(String output);
    void updateStatus(String status);
    void appendDecisionLog(String message);
    void speakMessage(String message);
    void navigateTo(Class<?> destination);

    Context getContextForFiles();
}