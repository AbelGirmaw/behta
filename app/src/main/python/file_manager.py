import os
import random

def get_file_path(context):
    files_dir = context.getFilesDir().getAbsolutePath()
    folder_path = os.path.join(files_dir, "chaque")
    os.makedirs(folder_path, exist_ok=True)
    return os.path.join(folder_path, "example.txt")

def write_text_to_file(context, text):
    file_path = get_file_path(context)
    with open(file_path, "w") as f:
        f.write(text)
    return f"Written to: {file_path}"

def edit_file_append(context, new_text):
    file_path = get_file_path(context)
    with open(file_path, "a") as f:
        f.write("\n" + new_text)
    return "Appended new content."

def read_text_from_file(context):
    file_path = get_file_path(context)
    if not os.path.exists(file_path):
        return "File not found."
    with open(file_path, "r") as f:
        return f.read()

def delete_file(context):
    file_path = get_file_path(context)
    if os.path.exists(file_path):
        os.remove(file_path)
        return "File deleted."
    else:
        return "File doesn't exist."

def get_100_random_words(context):
    content = read_text_from_file(context)
    words = content.split()
    if len(words) < 12:
        return "Not enough words in the file."
    sample_words = random.sample(words, 10)
    return ' '.join(sample_words)


def detect_user_intent(user_input):
    """Detect user intent based on natural language using regex."""
    user_input = user_input.lower().strip()

    # INTENT PATTERNS
    patterns = {
        "save": [
            r"\bsave\s+(this|note|file|it)\b",
            r"\bi\s+want\s+to\s+save\b",
            r"\bwrite\s+to\s+file\b"
        ],
        "edit": [
            r"\bedit\s+(a\s+)?file\b",
            r"\bmodify\s+(this|the)\s+text\b",
            r"\bi\s+want\s+to\s+change\b"
        ],
        "delete": [
            r"\bdelete\s+(a\s+)?file\b",
            r"\bremove\s+(this|that)\b",
            r"\bi\s+want\s+to\s+delete\b"
        ],
        "read": [
            r"\bread\s+(a\s+)?file\b",
            r"\bopen\s+and\s+read\b",
            r"\bshow\s+me\s+the\s+contents\b"
        ],
        "open_editor": [
            r"\bopen\s+(a\s+)?(text editor|notepad)\b",
            r"\bi\s+want\s+to\s+write\s+(a\s+)?note\b",
            r"\blaunch\s+(text editor|notepad)\b"
        ]
    }

    # Check each intent
    for intent, regex_list in patterns.items():
        for pattern in regex_list:
            if re.search(pattern, user_input):
                return intent

    return "unknown"

# === ACTION HANDLERS ===

def save_note():
    note = input("📝 Enter your note to save: ")
    if note.strip():
        with open("user_notes.txt", "a") as f:
            f.write(note + "\n")
        print("✅ Note saved to 'user_notes.txt'.")
    else:
        print("⚠️ Empty note. Nothing saved.")

def edit_file():
    filename = input("✏️ Enter filename to edit: ").strip()
    if not os.path.exists(filename):
        print("❌ File not found.")
        return
    print("📝 Opening for editing (append mode)...")
    with open(filename, "a") as f:
        content = input("Type text to add: ")
        f.write("\n" + content)
    print("✅ File updated.")

def delete_file():
    filename = input("🗑️ Enter filename to delete: ").strip()
    if os.path.exists(filename):
        os.remove(filename)
        print(f"✅ File '{filename}' deleted.")
    else:
        print("❌ File not found.")

def read_file():
    filename = input("📖 Enter filename to read: ").strip()
    if os.path.exists(filename):
        print(f"\n📄 Contents of '{filename}':\n")
        with open(filename, "r") as f:
            print(f.read())
    else:
        print("❌ File not found.")

def open_text_editor():
    print("📝 Text editor is simulated here. Type your content:")
    content = input("> ")
    with open("note_from_editor.txt", "w") as f:
        f.write(content)
    print("✅ Your content has been saved in 'note_from_editor.txt'.")

def unknown_intent():
    print("🤔 Sorry, I couldn't understand your intent. Try being more specific (e.g., 'delete file', 'save note').")

# === MAIN LOGIC ===

def main():
    print("💬 What do you want to do? (e.g., 'I want to save a note', 'Open text editor')")
    user_input = input("> ")

    intent = detect_user_intent(user_input)
    print(f"🔎 Detected Intent: {intent}")

    # Dispatch based on intent
    if intent == "save":
        save_note()
    elif intent == "edit":
        edit_file()
    elif intent == "delete":
        delete_file()
    elif intent == "read":
        read_file()
    elif intent == "open_editor":
        open_text_editor()
    else:
        unknown_intent()

if __name__ == "__main__":
    main()
