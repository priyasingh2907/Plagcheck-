import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class PlagiarismDetector1 {

    // ---------- Rabin-Karp Algorithm ----------
    public static boolean rabinKarp(String text, String pattern) {
        int d = 256;
        int q = 101;  // a prime number for modulus
        int m = pattern.length();
        int n = text.length();
        int p = 0; // hash for pattern
        int t = 0; // hash for text
        int h = 1;

        if (m > n) return false;

        for (int i = 0; i < m - 1; i++)
            h = (h * d) % q;

        for (int i = 0; i < m; i++) {
            p = (d * p + pattern.charAt(i)) % q;
            t = (d * t + text.charAt(i)) % q;
        }

        for (int i = 0; i <= n - m; i++) {
            if (p == t) {
                int j;
                for (j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j))
                        break;
                }
                if (j == m)
                    return true;
            }
            if (i < n - m) {
                t = (d * (t - text.charAt(i) * h) + text.charAt(i + m)) % q;
                if (t < 0)
                    t = t + q;
            }
        }
        return false;
    }

    // ---------- KMP Algorithm ----------
    public static boolean kmp(String text, String pattern) {
        int[] lps = computeLPSArray(pattern);
        int i = 0; // index for text
        int j = 0; // index for pattern
        int n = text.length();
        int m = pattern.length();

        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == m) {
                return true;
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0)
                    j = lps[j - 1];
                else
                    i++;
            }
        }
        return false;
    }

    private static int[] computeLPSArray(String pattern) {
        int length = 0;
        int i = 1;
        int m = pattern.length();
        int[] lps = new int[m];
        lps[0] = 0;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0)
                    length = lps[length - 1];
                else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    // ---------- Boyer-Moore Algorithm with HashMap for bad character ----------
    private static HashMap<Character, Integer> badCharTableMap(String pattern) {
        HashMap<Character, Integer> badChar = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            badChar.put(pattern.charAt(i), i);
        }
        return badChar;
    }

    public static boolean boyerMoore(String text, String pattern) {
        int m = pattern.length();
        int n = text.length();
        if (m > n) return false;

        HashMap<Character, Integer> badChar = badCharTableMap(pattern);
        int s = 0; // shift

        while (s <= n - m) {
            int j = m - 1;

            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j))
                j--;

            if (j < 0) {
                return true;
            } else {
                char c = text.charAt(s + j);
                int bcIndex = badChar.getOrDefault(c, -1);
                s += Math.max(1, j - bcIndex);
            }
        }
        return false;
    }

    // Read whole file content as a String
    private static String readFileAsString(Path path) throws IOException {
        // Read file bytes with UTF-8, fallback to ISO_8859_1 if MalformedInputException occurs
        try {
            return Files.readString(path);
        } catch (IOException e) {
            // try fallback
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes);
        }
    }

    // Main method: accepts one argument - the student's file path
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java PlagiarismDetector1 <student_file_path>");
            return;
        }

        Path studentFile = Paths.get(args[0]);
        Path datasetFolder = Paths.get("clean dataset");

        if (!Files.exists(studentFile)) {
            System.out.println("Student file does not exist: " + studentFile);
            return;
        }
        if (!Files.isDirectory(datasetFolder)) {
            System.out.println("Dataset folder not found: " + datasetFolder.toAbsolutePath());
            return;
        }

        try {
            String studentText = readFileAsString(studentFile);

            // We use a fixed pattern length for substring checks for better performance
            // Let's check plagiarism in chunks of size 50 (can be adjusted)
            int chunkSize = 50;

            // Keep track if plagiarism detected
            boolean plagiarismFound = false;

            DirectoryStream<Path> stream = Files.newDirectoryStream(datasetFolder, "*.txt");
            outer:
            for (Path refFile : stream) {
                String refText = readFileAsString(refFile);

                // Check studentText in sliding windows of chunkSize length
                for (int i = 0; i <= studentText.length() - chunkSize; i++) {
                    String chunk = studentText.substring(i, i + chunkSize);

                    boolean foundRK = rabinKarp(refText, chunk);
                    boolean foundKMP = kmp(refText, chunk);
                    boolean foundBM = boyerMoore(refText, chunk);

                    if (foundRK && foundKMP && foundBM) {
                        System.out.println("🔍 Plagiarism Detected!");
                        System.out.println("Matched Text: \"" + chunk + "\"");
                        System.out.println("Found in reference file: " + refFile.getFileName());
                        plagiarismFound = true;
                        break outer;  // Stop after first detected plagiarism
                    }
                }
            }

            if (!plagiarismFound) {
                System.out.println("No plagiarism detected.");
            }

        } catch (IOException e) {
            System.out.println("Error reading files: " + e.getMessage());
        }
    }
}
