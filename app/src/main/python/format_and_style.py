import re

# Define a list of common colors
colors = [
    "red", "green", "blue", "yellow", "orange", "purple", "pink",
    "black", "white", "gray", "grey", "brown", "cyan", "magenta",
    "violet", "indigo", "maroon", "beige", "gold", "silver", "teal"
]

# Create a regex pattern to detect color words
color_pattern = r"\b(" + "|".join(colors) + r")\b"

def detect_color_intent(user_input):
    # Normalize input
    text = user_input.strip().lower()

    # Try to find color in the input
    match = re.search(color_pattern, text)
    if match:
        selected_color = match.group(1)
        return selected_color
    else:
        return "other"