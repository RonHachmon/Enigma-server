package engine.EnigmaException;

import java.util.HashSet;
import java.util.Set;

public class WordNotValidInDictionaryException extends Exception {
    private final String startingMessage = "Error: String cannot be processed. it is not part of the dictionary:" + System.lineSeparator();

    private final Set<String> dictionaryWords;
    private final Set<String> illegalDictionaryWords;

    public WordNotValidInDictionaryException(Set<String> dictionaryWords) {
        this.dictionaryWords = dictionaryWords;
        this.illegalDictionaryWords = new HashSet<>();
    }

    public void addIllegalWord(String illegalWord) {
        illegalDictionaryWords.add(illegalWord);
    }

    public boolean isExceptionNeedToThrown() {
        return illegalDictionaryWords.size() > 0;
    }

    @Override
    public String getMessage() {
        return startingMessage + String.join(", ", illegalDictionaryWords) +
                System.lineSeparator() + "The possible word from the dictionary are:" + System.lineSeparator() + dictionaryWords;
    }
}
