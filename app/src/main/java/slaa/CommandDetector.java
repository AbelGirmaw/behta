package slaa;

import android.content.Context;
import java.util.Arrays;
import java.util.List;

public class CommandDetector {
    private static final List<String> START_COMMANDS = Arrays.asList(
            "start", "begin", "commence", "activate"
    );

    private static final List<String> READ_COMMANDS = Arrays.asList(
            "read", "word", "say", "tell me"
    );

    private static final List<String> STOP_COMMANDS = Arrays.asList(
            "stop", "exit", "end", "close", "terminate"
    );

    private static final List<String> PAUSE_COMMANDS = Arrays.asList(
            "pause", "hold", "wait", "freeze"
    );

    public String detectCommand(String text) {
        if (text == null || text.isEmpty()) {
            return "error";
        }

        String lowercaseText = text.toLowerCase().trim();

        if (containsAny(lowercaseText, START_COMMANDS)) {
            return "start";
        } else if (containsAny(lowercaseText, READ_COMMANDS)) {
            return "read";
        } else if (containsAny(lowercaseText, STOP_COMMANDS)) {
            return "stop";
        } else if (containsAny(lowercaseText, PAUSE_COMMANDS)) {
            return "pause";
        }

        return "error";
    }

    private boolean containsAny(String input, List<String> commands) {
        for (String cmd : commands) {
            if (input.contains(cmd)) {
                return true;
            }
        }
        return false;
    }
}