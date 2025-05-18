package slaa;

import android.content.Context;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class PythonHelper {
    public static void init(Context context) {
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(context));
        }
    }

    public static String callPythonFunction(String functionName, Object... args) {
        Python py = Python.getInstance();
        try {
            Object result = py.getModule("command_detector").callAttr(functionName, args);
            if (result != null){

                return result.toString();
            }
            return "unknown";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }



    public static String callPythonFileManagerFunction(String functionName, Object... args) {
        Python py = Python.getInstance();
        try {
            Object result = py.getModule("file_manager").callAttr(functionName, args);
            if (result != null){

                return result.toString();
            }
            return "unknown";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}