import re

def preprocess(text):
    text = text.lower().strip()
    return re.sub(r"[^\w\s]", "", text)

# Bot name and patterns
BOT_NAME = "africa"

# Patterns indicating bot is called
CALL_PATTERNS = [

    r"\bafrica[.!?,]?\b",
    r"\bcan you.*africa\b",
    r"\btalk to africa\b",
    r"\bmy africa\b",
    r"\btalk to africa\b",
    r"\bafrica\s+(please|help|tell)\b",
]

# Patterns indicating bot is *not* being called
NEGATION_PATTERNS = [
    r"\bdont\s+call\s+africa\b",
    r"\bi\W*am\W*not\W*talking\W*to\W*africa\b",
    r"\bno\W*need\W*for\W*africa\b",
    r"\bi\W*avoid\W*africa\b",
    r"\bi\W*refuse\W*to\W*talk\W*to\W*africa\b",
]


# Greeting keywords and patterns
GREETINGS = [
    r"\bhi\b", r"\bhello\b", r"\bhey\b", r"\bgood\s+(morning|afternoon|evening)\b",
    r"\bhowdy\b", r"\bwhats up\b", r"\bgreetings\b"
]

# Greeting directed at the bot
GREETING_PATTERNS = [fr"{greeting}\s+(africa)\b" for greeting in GREETINGS] + [
    r"\b(africa)\s+(hi|hello|hey|howdy|greetings)\b"
]

# Negations (not greeting Africa)
NEGATIONS = [
    r"\bnot\s+(saying\s+)?(hi|hello|hey)\s+to\s+africa\b",
    r"\bi\s+didnt\s+(even\s+)?greet\s+africa\b",
    r"\bno\s+(need|greeting)\s+for\s+africa\b",
    r"\bi\s+refuse\s+to\s+say\s+(hi|hello)\s+to\s+africa\b"
]


FAREWELL_PATTERNS = [
    r"\bbye\b", r"\bgoodbye\b", r"\bsee you\b", r"\blater\b", r"\bfarewell\b",
    r"\bbye africa\b", r"\bgoodbye africa\b"
]

# Thanks patterns
THANKS_PATTERNS = [
    r"\bthank you\b", r"\bthanks\b", r"\bthx\b", r"\bappreciate it\b", r"\bthanks africa\b"
]

# Negative sentiment patterns
NEGATIVE_PATTERNS = [
    r"\bi hate\b", r"\byou are useless\b", r"\byou suck\b", r"\bdumb bot\b", r"\bstupid\b"
]

def is_greeting_to_bot(text):
    text = preprocess(text)

    for neg in NEGATIONS:
        if re.search(neg, text):
            message='why you say that.'
            return {'intent':'negative_greeting','message':message}
    # Check for farewells
    for pattern in FAREWELL_PATTERNS:
        if re.search(pattern, text):
            message= "Goodbye! Come back anytime."
            return  {'intent':'close','message':message}

    # Check for thanks
    for pattern in THANKS_PATTERNS:
        if re.search(pattern, text):
            message= "You're welcome! Happy to help."
            return  {'intent':'thanks','message':message}

    # Check for negative
    for pattern in NEGATIVE_PATTERNS:
        if re.search(pattern, text):
            message="That’s not very kind, but I’m still here if you need me."
            return  {'intent':'thanks','message':message}

    for pat in GREETING_PATTERNS:
        if re.search(pat, text):
            message=' Hello there! How can I assist you today?'
            return  {'intent':'thanks','message':message}
    else:
        return  {'intent':'unknown','message':""}

def is_bot_called(text):
    text = preprocess(text)
    response={'intent':'unknown','message':""}
    # Check for negation first
    for neg in NEGATION_PATTERNS:
        if re.search(neg, text):

            return {'intent':'negative_call_bot','message':'why you say that.'}

    return  response



def detect_user_ask_name_intent(user_input):
    # Normalize input
    user_input = user_input.lower().strip()

    # Patterns to detect asking for name
    name_asking_patterns = [
        r"\bwhat(?:'s| is)?\s+your\s+name\b",
        r"\bcan\s+you\s+tell\s+me\s+your\s+name\b",
        r"\bmay\s+i\s+know\s+your\s+name\b",
        r"\bi\s+want\s+to\s+know\s+your\s+name\b",
        r"\btell\s+me\s+your\s+name\b",
        r"\byour\s+name\s*(\?)?",
        r"\bwho\s+are\s+you\b"
    ]

    # Patterns to detect negative intent toward name asking
    negative_name_patterns = [
        r"\bi\s+don't\s+want\s+to\s+know\s+your\s+name\b",
        r"\bi\s+don't\s+care\s+about\s+your\s+name\b",
        r"\bno\s+need\s+to\s+tell\s+me\s+your\s+name\b",
        r"\bi\s+didn't\s+ask\s+your\s+name\b",
        r"\bnot\s+interested\s+in\s+your\s+name\b",
        r"\bdon't\s+say\s+your\s+name\b"
    ]

    for pattern in negative_name_patterns:
        if re.search(pattern, user_input):
            return {'intent':"no_ask_name",'message':'why.my name is important to use this application'}

    for pattern in name_asking_patterns:
        if re.search(pattern, user_input):
            return {'intent':"ask_name",'message':'my name is africa.'}

    return {'intent':'unknown','message':''}

def detect_intro_intent(user_input):
    user_input = user_input.lower().strip()

    # Positive intent patterns (asking for introduction)
    positive_patterns = [
        r"\bintroduce\s+(yourself|you)\b",
        r"\bcan\s+you\s+introduce\s+(yourself|you)\b",
        r"\bcould\s+you\s+introduce\s+(yourself|you)\b",
        r"\btell\s+me\s+about\s+(yourself|you)\b",
        r"\bi\s+want\s+to\s+know\s+about\s+(you|yourself)\b",
        r"\bwho\s+are\s+you\b",
        r"\bgive\s+me\s+a\s+brief\s+about\s+you\b"
    ]

    # Negative intent patterns
    negative_patterns = [
        r"\bi\s+don't\s+want\s+to\s+know\s+about\s+you\b",
        r"\bi\s+don't\s+care\s+who\s+you\s+are\b",
        r"\bno\s+need\s+to\s+introduce\s+yourself\b",
        r"\bi\s+didn't\s+ask\s+you\s+to\s+introduce\b",
        r"\bdon't\s+introduce\s+yourself\b"
    ]

    for pattern in negative_patterns:
        if re.search(pattern, user_input):
            return "negative"

    for pattern in positive_patterns:
        if re.search(pattern, user_input):
            return "ask_introduction"

    return "unknown"

def get_introduction_response():
    return (
        "Hello! I’m your virtual English assistant, here to help you practice and improve your U.S. English listening and speaking skills. "
        ", I’m designed to support you every step of the way with friendly, interactive conversations, "
        "pronunciation feedback, and customized exercises tailored to your level.\n\n"
        "I was specifically created to support Visually Impaired Student learners, ensuring accessibility and ease of use. "
        "You can speak to me naturally, and I’ll respond just like a real conversation partner. \n\n"
        
        "I'm Simple android based artificial intelligence bot and developed by Abel.\n\n"
        "If you’re not sure where to start, just say something like “Let’s practice introductions”  and I’ll guide you. "
        "You can even ask me to slow down or repeat myself if you need extra help understanding.\n\n"
        "So, are you ready to begin? Let’s make learning English fun and accessible, together. I’m always here for you."
    )

# Example usage
def user_ask_introduction_intent(user_input):
    intent = detect_intro_intent(user_input)

    if intent == "ask_introduction":
        return {'intent':'ask_introduction','message':get_introduction_response()}
    elif intent == "negative":
        message="Alright! I won’t introduce myself right now. Let me know if you change your mind."
        return {'intent':'negative_ask_introduction',"message":message}
    else:
        message= "I'm here to help you practice English. Feel free to ask me anything!"
        return {'intent':'unknown','message':''}
# Test loop
def test_bot_call():
    print("🧠 Type something. I'll tell you if you're calling the bot 'Africa'. Type 'exit' to quit.\n")
    while True:
        user_input = input("You: ")
        if user_input.lower() == "exit":
            print("👋 Goodbye!")
            break
        print("🤖 Africa:", is_bot_called(user_input))

if __name__ == "__main__":
    test_bot_call()
