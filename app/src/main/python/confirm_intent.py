import re

def identify_confirm_intent(user_input):
    # Normalize input (lowercase and strip whitespace)
    text = user_input.strip().lower()

    # Define yes and no patterns using regular expressions
    yes_pattern = r"\b(yes|yeah|yep|sure|of course|affirmative|ok|okay|yup|definitely|certainly|absolutely)\b"
    no_pattern = r"\b(no|nope|nah|not really|never|negative|no way)\b"

    if re.search(yes_pattern, text):
        return "yes"
    elif re.search(no_pattern, text):
        return "no"
    else:
        return "other"