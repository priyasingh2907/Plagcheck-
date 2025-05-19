import re

def preprocess_text(text):
    # Step 1: Lowercase
    text = text.lower()

    # Step 2: Remove punctuation (keep letters, digits, whitespace)
    text = re.sub(r'[^\w\s]', '', text)

    # Step 3: Normalize whitespace (replace multiple spaces/newlines with single space)
    text = re.sub(r'\s+', ' ', text).strip()

    return text

def preprocess_file(input_file, output_file):
    try:
        with open(input_file, 'r', encoding='utf-8') as f:
            text = f.read()

        cleaned_text = preprocess_text(text)

        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(cleaned_text)

        print(f"✅ Preprocessing complete! Cleaned data saved to: {output_file}")

    except FileNotFoundError:
        print(f"❌ File not found: {input_file}")
    except Exception as e:
        print(f"⚠️ Error during preprocessing: {e}")

def split_file_by_words(input_file, words_per_file=10000):
    try:
        with open(input_file, 'r', encoding='utf-8') as f:
            text = f.read()

        words = text.split()
        count = 0
        for i in range(0, len(words), words_per_file):
            chunk_words = words[i:i + words_per_file]
            chunk_text = ' '.join(chunk_words)
            with open(f'wikipedia_clean_part_{count}.txt', 'w', encoding='utf-8') as out_f:
                out_f.write(chunk_text)
            count += 1

        print(f"✅ Splitting complete! Created {count} files.")

    except FileNotFoundError:
        print(f"❌ File not found: {input_file}")
    except Exception as e:
        print(f"⚠️ Error during splitting: {e}")

# Usage
input_path = 'wikipedia_reference.txt'
cleaned_path = 'wikipedia_clean.txt'

preprocess_file(input_path, cleaned_path)
split_file_by_words(cleaned_path, words_per_file=10000)
