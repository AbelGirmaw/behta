import re

# 1. Preprocessing: lowercase, normalize contractions, strip punctuation, collapse spaces
def preprocess(text: str) -> str:
    text = text.lower()
    # normalize common contractions so negation words stay intact
    contractions = {
        r"\bdon't\b": "dont",
        r"\bcan\'t\b": "cant",
        r"\bwon't\b": "wont",
        r"\bwouldn't\b": "wouldnt",
        r"\bshouldn't\b": "shouldnt",
        r"\bdoesn't\b": "doesnt",
        r"\bdidn't\b": "didnt",
        r"\bnot\b": "not"  # ensure plain 'not' stays
    }
    for pattern, repl in contractions.items():
        text = re.sub(pattern, repl, text)
    # now remove any remaining punctuation
    text = re.sub(r"[^\w\s]", " ", text)
    # collapse multiple spaces
    return re.sub(r"\s+", " ", text).strip()

# 2. Negation detector (matches the normalized forms)
NEGATION_PATTERN = re.compile(
    r"\b(no|not|never|dont|cant|cannot|wont|wouldnt)\b"
)

# 3. Redirect intents and their patterns (unchanged)
REDIRECT_PATTERNS = {
    'home': [
        r"\b(back|go|return|redirect)\b.*\b(home|home page|homepage|main page)\b",
        r"\b(homepage|main page|home )\b.*\b(page)\b",
    ],
    'login': [
        r"\b(go|take me|redirect)\b.*\b(login|sign in|log in)\b",
        r"\b(login|sign in)\b",
    ],
    'signup': [
        r"\b(go|take me|redirect)\b.*\b(sign up|register|create account)\b",
        r"\b(sign up|register|create account)\b",
    ],
    'profile': [
        r"\b(go|take me|redirect)\b.*\b(profile|my profile)\b",
        r"\b(profile|my profile)\b",
    ],
    'dashboard': [
        r"\b(go|take me|redirect)\b.*\b(dashboard)\b",
        r"\bdashboard\b",
    ],
    'settings': [
        r"\b(go|take me|redirect)\b.*\b(settings|preferences)\b",
        r"\b(settings|preferences)\b",
    ],
    'logout': [
        r"\b(log out|logout|sign out|end session)\b",
    ],
}

# 4. Detect intent (with negation handling)
def detect_redirect_intent(user_input: str) -> str:
    t = preprocess(user_input)
    for intent, patterns in REDIRECT_PATTERNS.items():
        for patt in patterns:
            if re.search(patt, t):
                if NEGATION_PATTERN.search(t):
                    return f"no_{intent}"
                return intent
    return 'unknown'

# 5. Map intent to response
REDIRECT_RESPONSES = {
    'home':        " Redirecting to the Home Page…",
    'no_home':     "Okay, I won’t redirect you to the Home Page.",
    'login':       " Redirecting to the Login Page…",
    'no_login':    " Okay, I won’t redirect you to the Login Page.",
    'signup':      " Redirecting to the Sign-Up Page…",
    'no_signup':   " Okay, I won’t redirect you to the Sign-Up Page.",
    'profile':     " Opening your Profile Page…",
    'no_profile':  " Okay, I won’t open your Profile Page.",
    'dashboard':   " Taking you to the Dashboard…",
    'no_dashboard':" Okay, I won’t take you to the Dashboard.",
    'settings':    " Opening Settings…",
    'no_settings': " Okay, I won’t open Settings.",
    'logout':      " Signing you out…",
    'no_logout':   " Okay, I won’t sign you out.",
    'unknown':     "I’m not sure where you’d like to go. Could you clarify?"
}

def get_redirect_response(intent: str) -> str:
    return REDIRECT_RESPONSES.get(intent, REDIRECT_RESPONSES['unknown'])

# 6. Chatbot loop
def chatbot():
    print("🤖 Chatbot: Ask me where to go! (Type 'exit' to quit.)")
    while True:
        user_input = input("You: ").strip()
        if user_input.lower() in ('exit', 'quit'):
            print("🤖 Chatbot: Goodbye!")
            break

        intent = detect_redirect_intent(user_input)
        response = get_redirect_response(intent)
        print(f"🤖 Chatbot: {response}")

if __name__ == "__main__":
    chatbot()
