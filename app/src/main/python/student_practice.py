import re

# Define patterns for each intent
SPEAKING_PATTERNS = [
    r"\b(practice|improve|develop|work on|enhance|train|better)\b.*\b(speaking|my speaking)\b",
    r"\b(speaking)\b.*\b(practice|train|improve|develop|work on)\b",
    r"\bi want to\b.*\b(speak|practice speaking|improve my speaking)\b",
    r"\bi need to\b.*\b(practice|develop).*speaking\b",
    r"\bspeaking practice\b",
    r"\bhow to speak\b",
    r"\bi want to speak english\b",
]

LISTENING_PATTERNS = [
    r"\b(practice|improve|develop|work on|enhance|train|better)\b.*\b(listening|my listening)\b",
    r"\b(listening)\b.*\b(practice|train|improve|develop|work on)\b",
    r"\bi want to\b.*\b(listen|practice listening|improve my listening)\b",
    r"\bi need to\b.*\b(practice|develop).*listening\b",
    r"\blistening practice\b",
    r"\blisten better\b",
    r"\bhow to listen\b",
    r"\bi want to listen english\b",
]

READING_PATTERNS = [
    r"\b(practice|improve|develop|work on|enhance|train|better)\b.*\b(reading|my reading)\b",
    r"\b(reading)\b.*\b(practice|train|improve|develop|work on)\b",
    r"\bi want to\b.*\b(read|practice reading|improve my reading)\b",
    r"\bi need to\b.*\b(practice|develop).*reading\b",
    r"\breading practice\b",
    r"\bhow to read\b",
    r"\bi want to read english\b",
]

PRONUNCIATION_PATTERNS = [
    r"\b(practice|improve|develop|work on|enhance|train|better)\b.*\b(pronunciation|my pronunciation)\b",
    r"\b(pronunciation)\b.*\b(practice|train|improve|develop|work on)\b",
    r"\bi want to\b.*\b(pronounce|practice pronunciation|improve my pronunciation)\b",
    r"\bi need to\b.*\b(practice|develop).*pronunciation\b",
    r"\bpronunciation practice\b",
    r"\bhow to pronounce\b",
    r"\bi want to pronounce english words better\b",
]

WRITING_PATTERNS = [
    r"\b(practice|improve|develop|work on|enhance|train|better)\b.*\b(writing|my writing)\b",
    r"\b(writing)\b.*\b(practice|train|improve|develop|work on)\b",
    r"\bi want to\b.*\b(write|practice writing|improve my writing)\b",
    r"\bi need to\b.*\b(practice|develop).*writing\b",
    r"\bwriting practice\b",
    r"\bhow to write\b",
    r"\bi want to write better\b",
]

# Negative intent patterns
NEGATIVE_PATTERNS = [
    r"\b(don't|dont|not)\b.*\b(practice|improve|develop|work on)\b.*\b(speaking|listening|reading|pronunciation|writing)\b",
    r"\b(i don't want to|i dont want to)\b.*\b(speak|listen|read|pronounce|write)\b",
    r"\bno\b.*\b(practice|improve|develop|work on)\b.*\b(speaking|listening|reading|pronunciation|writing)\b",
]

# Responses for intents
intent_responses = {
    'speaking': "Great! Let's start practicing your speaking.",
    'listening': "Awesome! Let's work on your listening skills.",
    'reading': "Perfect! Let's begin with a reading activity.",
    'pronunciation': "Excellent! Let's improve your pronunciation.",
    'writing': "Nice! Let's practice your writing."
}

def detect_intent(user_input):
    # Check for negative intents first
    for pattern in NEGATIVE_PATTERNS:
        if re.search(pattern, user_input, re.IGNORECASE):
            return "Okay, no worries! We can try another area or skip this practice."

    # Check for positive intents
    for pattern in SPEAKING_PATTERNS:
        if re.search(pattern, user_input, re.IGNORECASE):
            return intent_responses['speaking']
    for pattern in LISTENING_PATTERNS:
        if re.search(pattern, user_input, re.IGNORECASE):
            return intent_responses['listening']
    for pattern in READING_PATTERNS:
        if re.search(pattern, user_input, re.IGNORECASE):
            return intent_responses['reading']
    for pattern in PRONUNCIATION_PATTERNS:
        if re.search(pattern, user_input, re.IGNORECASE):
            return intent_responses['pronunciation']
    for pattern in WRITING_PATTERNS:
        if re.search(pattern, user_input, re.IGNORECASE):
            return intent_responses['writing']

    # Default response if no match
    return "Sorry, I couldn't understand your intent. Please specify whether you'd like to practice speaking, listening, reading, pronunciation, or writing."

# Example interaction loop
if __name__ == "__main__":
    while True:
        user_input = input("What would you like to practice today? (Type 'exit' to quit): ")
        if user_input.lower() == 'exit':
            print("Goodbye!")
            break
        response = detect_intent(user_input)
        print(response)
