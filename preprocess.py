import re

def preprocess(text):
    # Convert to lowercase
    text = text.lower()
    # Remove punctuation and non-alphanumeric characters (except spaces)
    text = re.sub(r'[^a-z0-9\s]', '', text)
    # Replace multiple spaces with a single space
    text = re.sub(r'\s+', ' ', text).strip()
    return text

def preprocess_file(input_path, output_path):
    try:
        # Open file with utf-8 and ignore bad characters
        with open(input_path, 'r', encoding='utf-8', errors='ignore') as file:
            raw_text = file.read()
        
        cleaned_text = preprocess(raw_text)

        with open(output_path, 'w', encoding='utf-8') as file:
            file.write(cleaned_text)

        print(f"✅ Preprocessed file saved as '{output_path}'")

    except FileNotFoundError:
        print(f"❌ Error: File '{input_path}' not found.")
    except Exception as e:
        print(f"❌ Unexpected error: {e}")

# -------- Main Execution --------
if __name__ == "__main__":
    input_file = "student_paper.txt"       # Your student file
    output_file = "student_clean.txt"      # Output for Java algorithms
    preprocess_file(input_file, output_file)
