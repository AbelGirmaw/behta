package slaa;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;


public class DecisionMaker {
    private Activity currentActivity;
    private MainActivity activity;
    private TextView resultView;
    private TextRecognizer textRecognizer;
    private PracticeSpeaking practiceSpeaking;
    private PracticeListening practiceListening;
    private Context context;
    private EditText practiceListeningOutput,practiceListeningStatus;
    private String unknown_response;
    private String state;
    private float score;
    private boolean is_africa_speak=false;
    private String current_activity="";
    private String color;
    private String is_color;
    private String bg_color;
    private String text_type;
    private String note_title;
    private String note_content;
    private  String is_content_asked;
    private String content_color;
    private  String is_asked;
    private String confirm;
    private static final String[] colors = {

            "red", "green", "blue", "yellow", "orange", "purple", "pink",
            "black", "white", "gray", "grey", "brown", "cyan", "magenta",
            "violet", "indigo", "maroon", "beige", "gold", "silver", "teal"
    };

    private String target_page="MainActivity";
    public String is_close="";
    // Command constants
    private static final String COMMAND_UNKNOWN = "unknown";
    private static final String COMMAND_CALL_BOT = "call_bot";
    private static final String COMMAND_HELLO = "hello";
    private static final String COMMAND_HOME = "home";
    private static final String COMMAND_LOGIN = "login";
    private static final String COMMAND_THANKS = "thanks";
    private static final String practice_speaking = "practice_speaking";
    private static final String practice_listening = "practice_listening";
    private static final String student_dashboard = "student_dashboard";
    private static final String teacher_dashboard = "teacher_dashboard";
    private static final String create_account = "signup";
    private static final String close="close";

    private static final String ask_name="ask_name";
    private static final String ask_introduction= "ask_introduction";


    private static final String FILE_WRITE = "file_write";
    private static final String FILE_APPEND = "file_append";
    private static final String FILE_READ = "file_read";
    private static final String FILE_DELETE = "file_delete";
    private static final String FILE_RANDOM_WORDS = "file_random";
    private static final String OPEN_TEXT_EDITOR = "take_note";

    public DecisionMaker(MainActivity activity, TextView resultView) {
        this.activity = activity;
        this.resultView = resultView;
    }
    public DecisionMaker(Context context) {
        this.context = context;
    }

    public DecisionMaker(AbelTextEditor abelTextEditor, EditText editText) {

    }

    public void setPracticeListeningActivity(PracticeListening activity) {
        this.practiceListening = activity;
    }



    public void setTextRecognizer(TextRecognizer textRecognizer) {
        this.textRecognizer = textRecognizer;
    }
    PracticeListeningManager practiceListeningManager =new PracticeListeningManager();
    PracticeSpeakingManager practiceSpeakingManager = new PracticeSpeakingManager();
    public void processResult(String result) {
        String command="";
        String message="";
        String user_spoken="";
        String random_words;
        if (!Objects.equals(result, "") && result !=null && !is_africa_speak) {
            result = PythonHelper.callPythonFunction("process_command", result,current_activity);
            random_words = PythonHelper.callPythonFileManagerFunction("get_100_random_words", activity);
            String jsonString = result;
            try {
                // Parse the JSON string
                JSONObject json = new JSONObject(jsonString);

                command = json.getString("intent");
                message = json.getString("message");
                user_spoken = json.getString("user_spoken");
                if (current_activity == "take_note" && Objects.equals(command,current_activity)){
                    confirm= json.getString("confirm");
                }

            } catch (Exception e) {
                command = e.toString();
            }

            if (command != "" && command != "unknown") {
                if(Objects.equals(current_activity, close)){
                    closeApp(activity);
                }
                switch (command.toLowerCase()) {
                    case COMMAND_HELLO:
                    case COMMAND_CALL_BOT:
                        handleResponse(message);
                        command=null;
                        current_activity=null;
                        break;
                    case close:
                        handleResponse(message);
                        current_activity=close;
                    case ask_name:
                    case "ask_introduction":
                        handleResponse(message);
                        break;
                    case COMMAND_HOME:
                        handleResponse(message, HomeActivity.class);
                        command=null;
                        break;

                    case practice_speaking:
                        if(current_activity  != practice_speaking){
                            handleResponse(message, PracticeListening.class);
                            current_activity="practice_speaking";
                            practiceSpeakingManager.getRandomWords(random_words);
                            break;

                        }
                        String next_word=practiceSpeakingManager.checkListeningSkill(user_spoken);
                        if (next_word == null){
                            String final_status=practiceSpeakingManager.getFinalStatus();
                            current_activity="";
                            handleResponse(final_status);
                        }
                        else{
                            updatePracticeListeningResultView(user_spoken,current_activity);
                            handleResponse("say ,"+ next_word);

                        }
                        command=null;
                        break;

                    case practice_listening:
                        if(current_activity  != practice_listening){
                            handleResponse(message, PracticeListening.class);
                            current_activity="practice_listening";
                            practiceListeningManager.getRandomWords(random_words);
                            break;

                        }
                        String next_phrase=practiceListeningManager.checkListeningSkill(user_spoken);
                        if (next_phrase == null){
                            String final_status=practiceListeningManager.getFinalStatus();
                            current_activity="";
                            handleResponse(final_status);
                        }
                        else{
                            updatePracticeListeningResultView(user_spoken,current_activity);
                            handleResponse("say ,"+ next_phrase);

                        }
                        command=null;
                        break;

                    case student_dashboard:
                        handleResponse(message, StudentDashboard.class);
                        command=null;
                        break;

                    case teacher_dashboard:
                        handleResponse(message, TeacherDashboard.class);
                        command=null;
                        break;

                    case create_account:
                        handleResponse(message, CreateAccount.class);
                        command=null;
                        break;

                    case COMMAND_LOGIN:
                        handleResponse(message, StudentLoginPage.class);
                        command=null;
                        break;

                    case COMMAND_THANKS:
                        handleResponse(message);
                        command=null;
                        break;
                    case OPEN_TEXT_EDITOR:
                        if(current_activity  != OPEN_TEXT_EDITOR){
                            handleResponse("in current activity "+message, AbelTextEditor.class);
                            current_activity=OPEN_TEXT_EDITOR;
                            break;
                        }
                        else {
                            if(text_type == null){
                                if (is_asked == null){
                                    is_asked="yes";
                                    handleResponse("what is your note title please tell me");
                                    break;
                                }
                                else if (Objects.equals(confirm,"yes")){
                                    text_type="title_one";
                                    handleResponse("ok by which color you want to write");
                                    break;
                                }
                                else if(Objects.equals(confirm,"no")){
                                    handleResponse("please tell me the correct title again");
                                    break;
                                }
                                else if(confirm != null){
                                    note_title=user_spoken;
                                    handleResponse(" your note title is "+note_title +"\n is it correct");
                                    break;
                                }
                                else {
                                    handleResponse("please confirm the title. just say yes or no");
                                    break;
                                }

                            }else if(color== null){
                                boolean found = isColorExist(confirm);
                                if (is_asked == null){
                                    is_asked="yes";
                                    handleResponse("by which color i write? please tell me the title color");
                                    break;
                                }
                                else if (Objects.equals(confirm,"yes")&& is_color!=null){
                                    color= is_color;
                                    handleResponse("please tell me the for ground color.");
                                    is_color=null;
                                    break;
                                }
                                else if(Objects.equals(confirm,"no")){
                                    handleResponse("ok please tell me the correct color.");
                                    is_color=null;
                                    break;
                                }
                                else if(found){
                                    is_color=confirm;
                                    handleResponse(" your title color is "+is_color +" is it correct");
                                    break;
                                }
                                else {
                                    handleResponse(" please tell me the correct color");
                                    break;
                                }
                            }else if(bg_color== null) {
                                boolean found = isColorExist(confirm);
                                if (is_asked == null) {
                                    is_asked = "yes";
                                    handleResponse("do you want add for ground color. please tell me");
                                    break;
                                } else if (Objects.equals(confirm, "yes") && is_color != null) {
                                    bg_color = is_color;
                                    writeDown(note_title,text_type,color,bg_color);
                                    handleResponse("ok i write the title,please tell me the content color.");
                                    is_color=null;
                                    break;

                                } else if (Objects.equals(confirm, "no")) {
                                    handleResponse("please tell me The correct for ground color. white  or what?");
                                    is_color=null;
                                    break;
                                } else if (found) {
                                    is_color = confirm;
                                    handleResponse(" your for ground  color is " + is_color + " is it correct");
                                    break;
                                }else {
                                    handleResponse("please select and confirm your title for ground color");
                                    break;
                                }

                            }else if(note_title != null) {
                                if(content_color==null){
                                    boolean found = isColorExist(confirm);
                                    if (is_asked == null) {
                                        is_asked = "yes";
                                        handleResponse("do you want to change the content color. please tell me");
                                        break;
                                    } else if (Objects.equals(confirm, "yes") && is_color != null) {
                                        content_color = is_color;
                                        is_content_asked="yes";
                                        handleResponse(" please tell me the content text what i write");
                                        is_color=null;
                                        break;
                                    } else if (Objects.equals(confirm, "no")) {
                                        handleResponse("please tell me correct content color. what you want black or what?");
                                        is_color=null;
                                        break;
                                    } else if (found) {
                                        is_color = confirm;
                                        handleResponse(" your text  color is " + is_color + " is it correct");
                                        break;
                                    }
                                    else {
                                        handleResponse("please select and confirm your content text color");
                                        break;
                                    }
                                }else{
                                    if (is_content_asked==null)
                                    {
                                        handleResponse("please tell me the content what you want write");
                                        is_content_asked="yes";
                                        break;
                                    }

                                     else if (Objects.equals(confirm, "yes") && note_content!=null) {
                                        writeDown(note_content,"normal",content_color,"white");
                                        handleResponse("okay,i write that correctly please tell me the next.");
                                        note_content=null;
                                        break;
                                    } else if (Objects.equals(confirm, "no")) {
                                        handleResponse("okay,please tell me what i write");
                                        note_content=null;

                                        break;
                                    }else if (note_content == null){
                                        handleResponse("am i correct? you ask to write "+user_spoken);
                                        note_content=user_spoken;
                                        break;
                                    }
                                     else{
                                         handleResponse("please confirm just say yes or no, your text is."+note_content);
                                         break;
                                     }
                                }
                            }
                            else {
                                handleResponse("the color is already selected please confirm it");
                                break;
                            }

                        }
                    case FILE_WRITE:
                        String result1 = PythonHelper.callPythonFileManagerFunction("write_text_to_file", activity, "Initial text here.");
                        handleResponse(result1);
                        command=null;
                        break;

                    case FILE_APPEND:
                        String result2 = PythonHelper.callPythonFileManagerFunction("edit_file_append", activity, "Appended line.");
                        handleResponse(result2);
                        command=null;
                        break;

                    case FILE_READ:
                        String result3 = PythonHelper.callPythonFileManagerFunction("read_text_from_file", activity);
                        handleResponse(result3);
                        command=null;
                        break;

                    case FILE_DELETE:
                        String result4 = PythonHelper.callPythonFileManagerFunction("delete_file", activity);
                        handleResponse(result4);
                        command=null;
                        break;

                    case FILE_RANDOM_WORDS:
                        String result5 = PythonHelper.callPythonFileManagerFunction("get_100_random_words", activity);
                        handleResponse(result5);
                        command=null;
                        break;

                    case COMMAND_UNKNOWN:
                        handleResponse(command);
//
                    default:
                        handleResponse(command);

                }
            } else {
                handleResponse("Error processing command");
            }
        }else {
            resumeListening();
        }
    }

private void resumeListening(){
        is_africa_speak=false;
}
    private static  Boolean isColorExist(String color){
        Boolean is_color=false;
        for (String name : colors){
            if(Objects.equals(color,name)){
                is_color=true;
                break;
            }
        }
        return is_color;
    }

    public static void writeDown(String text, String text_type, String textColor, String bgColor) {
        // Append from non-activity class
        text_type=text_type;
        TextAppender.textWriter(text,text_type,textColor,bgColor);
    }



    public void closeApp(Activity activity) {
        activity.finishAffinity();
        System.exit(0);   // Forcefully terminates the process (use with caution)
    }
    private void handleResponse(String message) {
//        updateResultView(message);
        if (textRecognizer != null) {
            textRecognizer.speak(message);
        }
    }

    private void handleResponse(String message, Class<?> destination) {
        handleResponse(message);
        navigateToActivity(destination);
    }

    private void updateResultView(String message) {
        activity.runOnUiThread(() -> {
            resultView.append("[Decision] " + message + "\n");
            scrollToBottom();
        });
    }

    //students practice listening
    private void updatePracticeListeningResultView(String output, String status) {
        if (practiceListening != null) {
            practiceListening.runOnUiThread(() -> {
                if (practiceListeningOutput != null) {
                    practiceListeningOutput.setText(output);
                }
                if (practiceListeningStatus != null) {
                    practiceListeningStatus.setText(status);
                }
            });
        }
    }

    public void handleListeningPractice(String user_spoken) {
        try {
            String spokenWord = user_spoken;

            String next = "hello world";

            if (next == null) {
                String finalStatus = "practiceListening.getFinalStatus()";
                practiceListening.runOnUiThread(() -> {
                    practiceListening.updateUI(spokenWord, finalStatus);
                });
            } else {
                String status = "it worked";
                practiceListening.runOnUiThread(() -> {
                    practiceListening.updateUI(spokenWord, status);
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void navigateToActivity(Class<?> destination) {
        activity.runOnUiThread(() -> {
            try {
                Intent intent = new Intent(activity, destination);
                activity.startActivity(intent);
            } catch (Exception e) {
                updateResultView("Error: Could not open " + destination.getSimpleName());
            }
        });
    }

    private void scrollToBottom() {
        activity.runOnUiThread(() -> {
            int scrollAmount = resultView.getLayout().getLineTop(resultView.getLineCount())
                    - resultView.getHeight();
            if (scrollAmount > 0) {
                resultView.scrollTo(0, scrollAmount);
            }
        });
    }

}