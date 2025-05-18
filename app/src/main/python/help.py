import re

# 1. Preprocessing: lowercase and strip punctuation
def preprocess(text):
    text = text.lower()
    text = re.sub(r"[^\w\s]", " ", text)
    return re.sub(r"\s+", " ", text).strip()

# 2. Negation words
NEGATION = r"\b(no|not|never|don't|dont|won't|wouldn't|can't|cannot)\b"

# 3. Patterns for requesting help
HELP_PATTERNS = [
    r"\b(help|assist|support)\b.*\b(me|please)\b",       # "help me please"
    r"\b(i need|i want|i'd like|i would like)\b.*\b(help|assistance)\b", 
    r"\b(can you|could you)\b.*\b(help|assist)\b",        # "can you help"
    r"\b(help|support)\b",                               # standalone "help"
    r"\b(give me a hand)\b",                              # colloquial
    r"\b(i'm stuck|i am stuck)\b",                        # indirect ask
    r"\b(could use)\b.*\b(help|support)\b",               # "could use help"
]

# 4. Detect intent
def detect_intent(text):
    t = preprocess(text)
    
    # 4a. If there's a negation near a help keyword → no_help
    #    e.g. "I don't need help", "no assistance please"
    neg_before = re.search(f"{NEGATION}.*(help|assist|support)", t)
    neg_after  = re.search(f"(help|assist|support).*{NEGATION}", t)
    if neg_before or neg_after:
        return "no_help"
    
    # 4b. Otherwise if any help-pattern matches → help
    for patt in HELP_PATTERNS:
        if re.search(patt, t):
            return "help"
    
    return "unknown"

# 5. Response mapping
RESPONSES = {
    "help":    "Sure—how can I assist you today?",
    "no_help": "Okay, no problem—let me know if you change your mind.",
    "unknown": "I'm not sure what you need. Could you clarify or rephrase?"
}

def get_response(intent):
    return RESPONSES[intent]

# 6. Chatbot loop
def chatbot():
    print("🤖 Chatbot: Hi! Type something. (Type 'exit' to quit)")
    while True:
        user_input = input("You: ").strip()
        if user_input.lower() in ("exit", "quit"):
            print("🤖 Chatbot: Goodbye!")
            break

        intent = detect_intent(user_input)
        print("🤖 Chatbot:", get_response(intent))

if __name__ == "__main__":
    chatbot()
