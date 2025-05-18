import re

def is_Take_Note(user_input):
    """Analyze user input to detect intent to take a note or not."""

    negative_patterns = [
        r"\b(don't|do not|no need to|forget|not necessary to)\s+(take|write|save|jot|record|note)\b",
        r"\b(not now|maybe later|skip it|never mind|cancel it)\b",
        r"\b(no|nah|nope)\s+(note|save|record|need)\b"
    ]

    positive_patterns = [
        r"\btake\s+(a\s+)?note\b",
        r"\bwrite\s+this\s+down\b",
        r"\bnote\s+this\b",
        r"\bsave\s+(this|it)\b",
        r"\bi\s+want\s+to\s+(remember|save|note|write)\b",
        r"\bi\s+need\s+to\s+(remember|note|jot)\b",
        r"\bplease\s+(open|start|launch)\s+(a\s+)?(text editor|notepad)\b",
        r"\blet\s+me\s+(write|save|jot|note)\s+(this|something)\b",
        r"\bi\s+think\s+i\s+should\s+(remember|note)\b"
    ]

    # Normalize input
    user_input = user_input.lower().strip()

    # Check for negatives first
    for pattern in negative_patterns:
        if re.search(pattern, user_input):
            message= "Okay.  I don’t write a note now. If you want feel free to ask."
            return  {'intent':'no_take_note','message':message}

    # Then check for positives
    for pattern in positive_patterns:
        if re.search(pattern, user_input):
            message="It looks like you want to take a note. What should I write down?"
            return  {'intent':'take_note','message':message}

    # Ambiguous or unclear
    message= "Hmm, I’m not sure. Did you want to take a note?"
    return  {'intent':'unknown','message':message}
