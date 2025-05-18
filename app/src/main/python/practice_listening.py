import re

def preprocess(text):
    text = text.lower()
    # Normalize contractions
    contractions = {
        r"\bdon't\b": "dont",
        r"\bcan't\b": "cant",
        r"\bwon't\b": "wont",
        r"\bwouldn't\b": "wouldnt",
        r"\bshouldn't\b": "shouldnt",
        r"\bdoesn't\b": "doesnt",
        r"\bdidn't\b": "didnt",
        r"\baren't\b": "arent",
        r"\bisn't\b": "isnt",
    }
    for pattern, replacement in contractions.items():
        text = re.sub(pattern, replacement, text)
    text = re.sub(r"[^\w\s]", " ", text)
    return re.sub(r"\s+", " ", text).strip()

# Patterns for negation and intent
NEGATION_PATTERN = re.compile(r"\b(no|not|never|dont|cant|wont|nah|nope|donot|avoid|refuse|skip)\b")

LISTENING_PATTERNS = [
    r"\b(practice|developed|practice the|improve|develop|work on|enhance|train|better)\b.*\b(listening|my listening|is listening)\b",
    r"\b(listening)\b.*\b(practice|train|improve|develop|work on)\b",
    r"\bi want to\b.*\b(listen|practice listening|improve my listening)\b",
    r"\bi need to\b.*\b(practice|develop).*listening\b",
    r"\blistening practice\b",
    r"\blisten better\b",
    r"\bhow to listen\b",
    r"\bi want to listen english\b",

]

def detect_listening_intent(user_input):
    text = preprocess(user_input)
    for pattern in LISTENING_PATTERNS:
        if re.search(pattern, text):
            if NEGATION_PATTERN.search(text):
                return "no_practice"
            return "practice"
    return "unknown"
def get_response(intent):
    responses = {
        "practice": "Greate! Let's start practicing your listening skills.",
        "no_practice": "okay. We will not do any listening practice right now.",
        "unknown": "I’m not sure if you want to practice listening. Could you rephrase?"
    }
    return responses[intent]
def get_practice_listening_response(text):
    intent=detect_listening_intent(text)
    response=get_response(intent)
    return {'intent':f'{intent}_listening','message':response}