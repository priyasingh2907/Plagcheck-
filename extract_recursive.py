import os
import json

def extract_wikipedia_text_recursive(root_folder, output_file):
    with open(output_file, 'w', encoding='utf-8') as out:
        for root, dirs, files in os.walk(root_folder):
            for file in files:
                if file.endswith(".json"):
                    file_path = os.path.join(root, file)
                    try:
                        with open(file_path, 'r', encoding='utf-8') as f:
                            data = json.load(f)
                            if isinstance(data, dict):
                                # If it's a JSON object
                                text = data.get('text') or data.get('content') or ''
                                if isinstance(text, list):  # Sometimes text is a list of strings
                                    text = ' '.join(text)
                                out.write(text + "\n\n")
                            elif isinstance(data, list):
                                # If it's a list of JSON objects
                                for entry in data:
                                    text = entry.get('text') or entry.get('content') or ''
                                    if isinstance(text, list):
                                        text = ' '.join(text)
                                    out.write(text + "\n\n")
                    except Exception as e:
                        print(f"⚠️ Error reading {file_path}: {e}")

    print(f"✅ Extraction complete. Output saved to: {output_file}")

# Example usage
extract_wikipedia_text_recursive(
    root_folder=r"C:\Users\HP\Downloads\archive",
    output_file="wikipedia_reference.txt"
)
