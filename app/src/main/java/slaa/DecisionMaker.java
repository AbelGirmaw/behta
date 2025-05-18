package slaa;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;


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
    private String current_activity="";
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
        if (result != "" && result !=null) {
            result = PythonHelper.callPythonFunction("process_command", result,current_activity);
            random_words = PythonHelper.callPythonFileManagerFunction("get_100_random_words", activity);
            String jsonString = result;
            try {
                // Parse the JSON string
                JSONObject json = new JSONObject(jsonString);

                command = json.getString("intent");
                message = json.getString("message");
                user_spoken = json.getString("user_spoken");


            } catch (Exception e) {
                command = e.toString();
            }

            if (command != null && command != "" && command != "unknown") {

                switch (command.toLowerCase()) {
                    case COMMAND_HELLO:
                    case COMMAND_CALL_BOT:
                        handleResponse(message);
                        command=null;
                        current_activity=null;
                        break;
                    case close:
                        handleResponse(message);
                        closeApp();
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
                        handleResponse(message, AbelTextEditor.class);
                        break;
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
                        unknown_response += command;
//
//                    default:
//                        handleResponse(command);
                }
            } else {
                handleResponse("Error processing command");
            }
        }
    }
    public void closeApp() {
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