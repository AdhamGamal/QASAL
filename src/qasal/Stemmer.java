package qasal;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public final class Stemmer {

    private static final ArrayList<String> definiteArticles = new ArrayList<>();
    private static final ArrayList<String> punctuation = new ArrayList<>();
    private static final ArrayList<String> diacritics = new ArrayList<>();
    private static final ArrayList<String> quadRoots = new ArrayList<>();
    private static final ArrayList<String> stopWords = new ArrayList<>();
    private static final ArrayList<String> duplicate = new ArrayList<>();
    private static final ArrayList<String> prefixes = new ArrayList<>();
    private static final ArrayList<String> suffixes = new ArrayList<>();
    private static final ArrayList<String> patterns = new ArrayList<>();
    private static final ArrayList<String> triRoots = new ArrayList<>();
    private static final ArrayList<String> firstWaw = new ArrayList<>();
    private static final ArrayList<String> firstYah = new ArrayList<>();
    private static final ArrayList<String> lastAlif = new ArrayList<>();
    private static final ArrayList<String> lastYah = new ArrayList<>();
    private static final ArrayList<String> strange = new ArrayList<>();
    private static final ArrayList<String> midWaw = new ArrayList<>();
    private static final ArrayList<String> midYah = new ArrayList<>();

    private boolean notStemmped = false;
    private boolean rootFound = false;

    public Stemmer() {
        if (stopWords.isEmpty()) {
            readInStaticFiles();
        }
    }

    public String formatWord(String currentWord) {
        notStemmped = false;
        rootFound = false;
        StringBuffer modifiedWord = new StringBuffer(currentWord);

        modifiedWord = removeDiacritics(modifiedWord);
        if (notStemmped) {
            return modifiedWord.toString();
        }
        modifiedWord = removeNonLetter(modifiedWord);
        if (notStemmped) {
            return modifiedWord.toString();
        }
        modifiedWord = removePunctuation(modifiedWord);
        if (notStemmped) {
            return modifiedWord.toString();
        }

        String stemmedWord = stemWord(modifiedWord.toString());
        notStemmped = false;
        rootFound = false;
        return stemmedWord;
    }

    private StringBuffer removeDiacritics(StringBuffer currentWord) {
        for (int i = 0; i < currentWord.length(); i++) {
            if (diacritics.contains(currentWord.charAt(i) + "")) {
                currentWord = currentWord.deleteCharAt(i);
            }
        }
        if (currentWord.length() == 0) {
            notStemmped = true;
        }
        return currentWord;
    }

    public StringBuffer removePunctuation(StringBuffer currentWord) {
        for (int i = 0; i < currentWord.length(); i++) {
            if (punctuation.contains(currentWord.charAt(i) + "")) {
                currentWord = currentWord.deleteCharAt(i);
            }
        }
        if (currentWord.length() == 0) {
            notStemmped = true;
        }
        return currentWord;
    }

    private StringBuffer removeNonLetter(StringBuffer currentWord) {
        for (int i = 0; i < currentWord.length(); i++) {
            if (!(Character.isLetter(currentWord.charAt(i)) || currentWord.charAt(i) == 'ّ')) {
                currentWord = currentWord.deleteCharAt(i);
            }
        }
        if (currentWord.length() == 0) {
            notStemmped = true;
        }
        return currentWord;
    }

    private String stemWord(String word) {
        String strange = checkDefiniteArticle(word);
        if (!checkStrangeWords(strange)) {
            if (!checkStrangeWords(word)) {
                word = checkBylength(word);
                if (!rootFound) {
                    word = checkDefiniteArticle(word);
                }
                if (!rootFound) {
                    word = checkPatterns(word);
                }
                if (!rootFound) {
                    word = checkForSuffixes(word);
                }

                if (!rootFound) {
                    word = checkForPrefixes(word);
                }
            }
        } else {
            word = strange;
        }
        return word;
    }

    private String checkBylength(String word) {
        if (!checkStopwords(word)) {
            if (word.length() == 2) {
                word = isTwoLetters(word);
            }

            if (word.length() == 3 && !rootFound) {
                word = isThreeLetters(word);
            }

            if (word.length() == 4 && !rootFound) {
                isFourLetters(word);
            }
        }
        return word;
    }

    public boolean checkStopwords(String currentWord) {
        rootFound = false;
        if (stopWords.contains(currentWord)) {
            rootFound = true;
        }
        return rootFound;
    }

    private boolean checkStrangeWords(String currentWord) {
        if (strange.contains(currentWord)) {
            rootFound = true;
        }
        return rootFound;
    }

    private String checkPatterns(String word) {
        StringBuffer root = new StringBuffer("");
        if (word.length() > 0) {
            if (word.charAt(0) == '\u0623' || word.charAt(0) == '\u0625' || word.charAt(0) == '\u0622') {
                word = word.replaceFirst(word.charAt(0) + "", "\u0627");
            }
        }

        int numberSameLetters = 0;
        String pattern = "";
        String modifiedWord = "";

        for (Object buffer : patterns) {
            root.setLength(0);
            pattern = buffer.toString();
            if (pattern.length() == word.length()) {
                numberSameLetters = 0;
                for (int j = 0; j < word.length(); j++) {
                    if (pattern.charAt(j) == word.charAt(j)
                            && pattern.charAt(j) != '\u0641'
                            && pattern.charAt(j) != '\u0639'
                            && pattern.charAt(j) != '\u0644') {
                        numberSameLetters++;
                    }
                }

                if (word.length() == 6 && word.charAt(3) == word.charAt(5) && numberSameLetters == 2) {
                    root.append(word.charAt(1));
                    root.append(word.charAt(2));
                    root.append(word.charAt(3));
                    modifiedWord = root.toString();
                    modifiedWord = isThreeLetters(modifiedWord);
                    if (rootFound) {
                        return modifiedWord;
                    } else {
                        root.setLength(0);
                    }
                }

                if (word.length() - 3 <= numberSameLetters) {
                    for (int j = 0; j < word.length(); j++) {
                        if (pattern.charAt(j) == '\u0641'
                                || pattern.charAt(j) == '\u0639'
                                || pattern.charAt(j) == '\u0644') {
                            root.append(word.charAt(j));
                        }
                    }

                    modifiedWord = root.toString();
                    modifiedWord = isThreeLetters(modifiedWord);

                    if (rootFound) {
                        word = modifiedWord;
                        return word;
                    }
                }
            }
        }
        return word;
    }

    private String checkDefiniteArticle(String word) {
        String definiteArticle = "";
        for (Object buffer : definiteArticles) {
            definiteArticle = buffer.toString();
            if (word.startsWith(definiteArticle)) {
                return word.replaceFirst(definiteArticle, "");
            }
        }
        return word;
    }

    private String checkForPrefixes(String word) {
        String prefixe = "";
        String modifiedWord = word;
        for (Object buffer : prefixes) {
            prefixe = buffer.toString();
            if (prefixe.regionMatches(0, modifiedWord, 0, prefixe.length())) {
                modifiedWord = modifiedWord.substring(prefixe.length());

                modifiedWord = checkBylength(modifiedWord);

                if (!rootFound) {
                    modifiedWord = checkPatterns(modifiedWord);
                }
                if (!rootFound) {
                    modifiedWord = checkForSuffixes(modifiedWord);
                }
                if (rootFound) {
                    return modifiedWord;
                }
            }
        }
        return word;
    }

    private String checkForSuffixes(String word) {
        String suffixe = "";
        String modifiedWord = word;
        for (Object buffer : suffixes) {
            suffixe = buffer.toString();
            if (modifiedWord.endsWith(suffixe)) {
                modifiedWord = modifiedWord.substring(0, modifiedWord.length() - suffixe.length());
                modifiedWord = checkBylength(modifiedWord);
                if (!rootFound && modifiedWord.length() > 2) {
                    modifiedWord = checkPatterns(modifiedWord);
                }
                if (rootFound) {
                    return modifiedWord;
                }
            }
        }
        return word;
    }

    private String isTwoLetters(String word) {

        word = duplicate(word);
        if (!rootFound) {
            word = firstWeak(word);
        }
        if (!rootFound) {
            word = middleWeak(word);
        }
        if (!rootFound) {
            word = lastWeak(word);
        }
        return word;
    }

    private String isThreeLetters(String word) {
        String root = "";
        if (word.charAt(0) == '\u0627' || word.charAt(0) == '\u0624' || word.charAt(0) == '\u0626') {
            root = word.replaceFirst(word.charAt(0) + "", "\u0623");
        }
        if (word.charAt(2) == '\u0648' || word.charAt(2) == '\u064a' || word.charAt(2) == '\u0627'
                || word.charAt(2) == '\u0649' || word.charAt(2) == '\u0621' || word.charAt(2) == '\u0626') {
            root = lastWeak(word.substring(0, 2));
            if (rootFound) {
                return root;
            }
        }
        if (word.charAt(1) == '\u0648' || word.charAt(1) == '\u064a' || word.charAt(1) == '\u0627' || word.charAt(1) == '\u0626') {
            root = middleWeak(word.substring(0, 1) + word.substring(2));
            if (rootFound) {
                return root;
            }
        }
        if (word.charAt(1) == '\u0624' || word.charAt(1) == '\u0626') {
            if (word.charAt(2) == '\u0645' || word.charAt(2) == '\u0632' || word.charAt(2) == '\u0631') {
                root = word.substring(0, 1) + '\u0627' + word.substring(2);
            } else {
                root = word.substring(0, 1) + '\u0623' + word.substring(2);
            }
        }
        if (word.charAt(2) == '\u0651') {
            root = word.substring(0, 1) + word.substring(1, 2);
        }
        if (root.length() == 0) {
            if (triRoots.contains(word)) {
                rootFound = true;
                return word;
            }
        } else if (triRoots.contains(root)) {
            rootFound = true;
            return root;
        }
        return word;
    }

    private void isFourLetters(String word) {
        if (quadRoots.contains(word)) {
            rootFound = true;
        }
    }

    private String duplicate(String word) {
        if (duplicate.contains(word)) {
            word = word + word;
            rootFound = true;
        }
        return word;
    }

    private String firstWeak(String word) {
        if (firstWaw.contains(word)) {
            word = "و" + word;
            rootFound = true;
        } else if (firstYah.contains(word)) {
            word = "ي" + word;
            rootFound = true;
        }
        if (rootFound) {
        }
        return word;
    }

    private String middleWeak(String word) {
        StringBuffer buffer = new StringBuffer(word);
        if (midWaw.contains(word)) {
            buffer.insert(1, "و");
            rootFound = true;
        } else if (midYah.contains(word)) {
            buffer.insert(1, "ي");
            rootFound = true;
        }
        if (rootFound) {
        }
        return buffer.toString();
    }

    private String lastWeak(String word) {
        if (lastAlif.contains(word)) {
            word += "ا";
            rootFound = true;
        } else if (lastYah.contains(word)) {
            word += "ي";
            rootFound = true;
        }
        return word;
    }

    protected void readInStaticFiles() {
        addVectorFromFile(Strings.StemmerFilesPath + "definite_article.txt", definiteArticles);
        addVectorFromFile(Strings.StemmerFilesPath + "duplicate.txt", duplicate);
        addVectorFromFile(Strings.StemmerFilesPath + "first_waw.txt", firstWaw);
        addVectorFromFile(Strings.StemmerFilesPath + "first_yah.txt", firstYah);
        addVectorFromFile(Strings.StemmerFilesPath + "last_alif.txt", lastAlif);
        addVectorFromFile(Strings.StemmerFilesPath + "last_yah.txt", lastYah);
        addVectorFromFile(Strings.StemmerFilesPath + "mid_waw.txt", midWaw);
        addVectorFromFile(Strings.StemmerFilesPath + "mid_yah.txt", midYah);
        addVectorFromFile(Strings.StemmerFilesPath + "prefixes.txt", prefixes);
        addVectorFromFile(Strings.StemmerFilesPath + "punctuation.txt", punctuation);
        addVectorFromFile(Strings.StemmerFilesPath + "quad_roots.txt", quadRoots);
        addVectorFromFile(Strings.StemmerFilesPath + "stopwords.txt", stopWords);
        addVectorFromFile(Strings.StemmerFilesPath + "suffixes.txt", suffixes);
        addVectorFromFile(Strings.StemmerFilesPath + "patterns.txt", patterns);
        addVectorFromFile(Strings.StemmerFilesPath + "tri_roots.txt", triRoots);
        addVectorFromFile(Strings.StemmerFilesPath + "diacritics.txt", diacritics);
        addVectorFromFile(Strings.StemmerFilesPath + "strange.txt", strange);
    }

    protected void addVectorFromFile(String fileName, ArrayList buffer) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_16));
            String file;
            String words[];
            while ((file = reader.readLine()) != null) {
                words = file.split(" ");
                for (String word : words) {
                    buffer.add(word);
                }
            }
        } catch (Exception e) {
        }
    }
}
