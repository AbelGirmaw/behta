# my_module.py
import help
import  redirect_to
import africa
import practice_speaking
import practice_listening
import text_editor
import confirm_intent
import format_and_style
def greet(name):
    return f"Hello {name} from Python!"

def calculate(a, b):
    return {
        'sum': a + b,
        'difference': a - b,
        'product': a * b
    }

# command_processor.py
def process_command(command,current_activity):
    command=command.replace('{','')
    command=command.replace('}','')
    command=command.replace('"text" :','')
    command = command.lower().strip()
    intent={}
    response={'intent':'unknown','message':"this is the else page all commands not known. please ask me again?"}
    user_spoken=command.replace('"', "")
    is_call=False
    call_bot=['africa','my africa','a africa','african','a african','me africa','my in africa']
    if len(command) <=2:
        return {"intent":"unknown",'message':"",'user_spoken':user_spoken}
    else:
        print('you say ',command)
        if user_spoken in call_bot:
            return {'intent':'call_bot','message':'yes','user_spoken':user_spoken}

        if current_activity !="":
            print("in current activity")
            if current_activity=="":
                pass
            elif current_activity=="practice_speaking":
                return {'intent':"practice_speaking",'message':'','user_spoken':user_spoken}
            elif current_activity=="practice_listening":
                return {'intent': "practice_listening", 'message': '', 'user_spoken': user_spoken}
            elif current_activity == "take_note":
                print("in take note")
                confirm="other"
                try:
                    confirm=confirm_intent.identify_confirm_intent(user_spoken)
                    if confirm=='other':
                        confirm=format_and_style.detect_color_intent(user_spoken)
                except Exception as e:
                    print("the error is ",e)

                print('this is confirm data',confirm)
                return {'intent': "take_note", 'message': 'no', 'user_spoken': user_spoken,'confirm':confirm}

        if not is_call:
            intent=africa.is_bot_called(command)
            print('in call bot',intent)
            if intent['intent'] != "unknown":
                return intent.update({'user_spoken':user_spoken})
        if intent['intent'] == 'unknown':
            intent=africa.detect_user_ask_name_intent(command)
            print('this is name page intent',intent)
            if intent['intent'] != 'unknown':
                return {'intent':'ask_name','message':'my name is africa','user_spoken':user_spoken}
        if intent['intent'] == 'unknown':
            intent=africa.user_ask_introduction_intent(command)
            if intent['intent'] != 'unknown':
                return {'intent':intent['intent'],'message':intent['message'],'user_spoken':user_spoken}
        if intent['intent'] == 'unknown':
            intent=africa.is_greeting_to_bot(command)
            print('in greeting',intent)
            if intent['intent'] != "unknown":
                return {'intent':intent['intent'],'message':intent['message'],'user_spoken':user_spoken}
        if intent['intent'] == 'unknown':
            intent = redirect_to.detect_redirect_intent(command)
            message = redirect_to.get_redirect_response(intent)
            print('in redirerect page',intent)
            if intent != 'unknown':
                print('in redirect page unknown')
                return  {'intent':intent,'message':message,'user_spoken':user_spoken}

        if intent == 'unknown':
            intent=practice_speaking.get_practice_speaking_response(command)
            print('practice speaking page',intent)
            if intent['intent'] != 'unknown_speaking':
                return {'intent':intent['intent'],'message':intent['message'],'user_spoken':user_spoken}
        if intent['intent']=='unknown_speaking':
            intent=practice_listening.get_practice_listening_response(command)
            print('in practice listening skill',intent)
            if intent['intent'] != 'unknown_listening':
                return {'intent':intent['intent'],'message':intent['message'],'user_spoken':user_spoken}
        if intent['intent'] == 'unknown_listening':
            intent=text_editor.is_Take_Note(command)
            print('this is take not  agin anind page',intent)
            if intent['intent'] != 'unknown':
                return {'intent':intent['intent'],'message':intent['message'],'user_spoken':user_spoken}
        print('this is the last info',intent)
        return {'intent':'unknown','message':'','user_spoken':user_spoken}