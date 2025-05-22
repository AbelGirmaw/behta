package slaa;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.EditText;


public class TextAppender {

    private static EditText editText;
    private static Context appContext;

    public static String plainText;
    // Set the EditText (called from Activity)
    public static void setEditText(EditText et) {
        editText = et;
    }

    public static void setContext(Context context) {
        appContext = context;
    }

    // Append text (can be called from any class)

    //commands
    private static final String TITLE_ONE="title_one";
    private static final  String TITLE_TWO="title_two";
    private static final String TITLE_THREE="title_three";
    private static final String TITLE_FOUR="title_four";
    private static final  String TITLE_FIVE="title_five";
    private static final  String TITLE_SIX="title_six";
    private static final String NORMAL_TEXT="normal";
    private static final String SAVE="save";
    private static int color=Color.BLACK;
    private static int bg_color=Color.WHITE;
    public  static void textWriter(String text,String text_type,String color,String bg_color){
        String saved_text="<tt$"+text_type+">"+"<cl$"+color+">"+"<bg$"+bg_color+">"+"@"+text_type+"/@";
        plainText+=saved_text;

            int color_value = authenticateColor(color);
            int bg_color_value = authenticateColor(bg_color);
            String result = PythonHelper.callPythonFileManagerFunction("write_text_to_student_file", appContext, plainText);
            switch (text_type) {
                case NORMAL_TEXT:
                    append(text + " ", 1.0f, color_value, bg_color_value);
                    break;
                case TITLE_ONE:
                    append(text + "\n", 1.5f, color_value, bg_color_value);
            }

    }

    private static int authenticateColor(String color) {
        if (color == null) return Color.BLACK;

        switch (color.toLowerCase()) {
            case "black":
                return Color.BLACK;
            case "white":
                return Color.WHITE;
            case "red":
                return Color.RED;
            case "green":
                return Color.GREEN;
            case "blue":
                return Color.BLUE;
            case "yellow":
                return Color.YELLOW;
            case "cyan":
                return Color.CYAN;
            case "magenta":
                return Color.MAGENTA;
            case "gray":
            case "grey":
                return Color.GRAY;
            case "ltgray":
                return Color.LTGRAY;
            case "dkgray":
                return Color.DKGRAY;
            case "orange":
                return Color.parseColor("#FFA500");
            case "purple":
                return Color.parseColor("#800080");
            case "pink":
                return Color.parseColor("#FFC0CB");
            case "brown":
                return Color.parseColor("#A52A2A");
            case "violet":
                return Color.parseColor("#EE82EE");
            case "indigo":
                return Color.parseColor("#4B0082");
            case "maroon":
                return Color.parseColor("#800000");
            case "beige":
                return Color.parseColor("#F5F5DC");
            case "gold":
                return Color.parseColor("#FFD700");
            case "silver":
                return Color.parseColor("#C0C0C0");
            case "teal":
                return Color.parseColor("#008080");
            default:
                return Color.BLACK; // fallback if color not recognized
        }
    }

    public static void append(String text, float relativeSize, int textColor, int bgColor) {
        if (editText != null) {
            SpannableString spanString = new SpannableString(text);

            // Apply font size
            spanString.setSpan(new RelativeSizeSpan(relativeSize), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Apply text color
            spanString.setSpan(new ForegroundColorSpan(textColor), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Apply background color
            spanString.setSpan(new BackgroundColorSpan(bgColor), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            editText.append(spanString);

        }
    }
    // Append text with font size
    static void append(String text, float relativeSize) {
        if (editText != null) {
            SpannableString spanString = new SpannableString(text);
            spanString.setSpan(new RelativeSizeSpan(relativeSize), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            editText.append(spanString);
        }
    }

}