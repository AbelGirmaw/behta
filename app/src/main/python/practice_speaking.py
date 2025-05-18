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

SPEAKING_PATTERNS = [
    r"\b(practice|improve|develop|work on|enhance|train|better)\b.*\b(speaking|speak|speech|oral|talking)\b",
    r"\b(speaking|talking|speech|oral)\b.*\b(practice|train|improve|develop|work on)\b",
    r"\bi want to\b.*\b(speak|talk|practice speaking|improve my speaking)\b",
    r"\bi need to\b.*\b(practice|develop).*speaking\b",
    r"\bspeaking practice\b",
    r"\bspeak better\b",
    r"\bhow to speak\b",
    r"\bi want to speak english\b",



    r"\b(practice|improve|develop|work on|enhance|train|better)\b.*\b(speaking|my speaking)\b",
    r"\b(speaking)\b.*\b(practice|train|improve|develop|work on)\b",
    r"\bi want to\b.*\b(speak|practice speaking|improve my speaking)\b",
    r"\bi need to\b.*\b(practice|develop).*speaking\b",
    r"\bspeaking practice\b",
    r"\bhow to speak\b",
    r"\bi want to speak english\b",
]

def detect_speaking_intent(user_input):
    text = preprocess(user_input)
    for pattern in SPEAKING_PATTERNS:
        if re.search(pattern, text):
            if NEGATION_PATTERN.search(text):
                return "no_practice"
            return "practice"
    return "unknown"
def get_response(intent):
    responses = {
        "practice": "Greate! Let's start practicing your speaking skills.",
        "no_practice": "okay. We won’t do any speaking practice right now.",
        "unknown": "I’m not sure if you want to practice speaking. Could you rephrase?"
    }
    return responses[intent]
def get_practice_speaking_response(text):
    intent=detect_speaking_intent(text)
    response=get_response(intent)
    return {'intent':f'{intent}_speaking','message':response}