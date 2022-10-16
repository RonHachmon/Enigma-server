package engine.enigma.bruteForce2.utils;

import engine.enigma.EnigmaException.WordNotValidInDictionaryException;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Dictionary implements Serializable  {
    private final Set<String> words;
    private final Set<Character> excludeChars;

    public Dictionary() {
        this.words = new HashSet<>();
        this.excludeChars = new HashSet<>();
    }
    public void setDictionary(String words, String excludeChars){
        this.excludeChars.addAll(excludeChars.toUpperCase().chars().mapToObj(ch -> (char)ch).collect(Collectors.toList()));

        cleanDictionaryFromExcludeChars(Arrays.asList(words.toUpperCase().trim().split(" ")));
    }

    private void cleanDictionaryFromExcludeChars(List<String> unCleanedWords) {
        unCleanedWords.forEach(word -> {
            this.words.add(word.replaceAll("[" + excludeChars + "]", ""));
        });
    }

    public Set<String> getDictionary() {
        return words;
    }

    public List<String> validateWordsAfterCleanExcludeChars(List<String> wordsToCheck) throws WordNotValidInDictionaryException {
        List<String> wordsToCheckAfterCleanExcludeChars = new ArrayList<>();

        wordsToCheck.forEach(word -> {
            wordsToCheckAfterCleanExcludeChars.add(word.replaceAll("[" + excludeChars + "]", ""));
        });

        validateWords(wordsToCheckAfterCleanExcludeChars);

        return wordsToCheckAfterCleanExcludeChars;
    }

    public void validateWords(List<String> wordsToCheckAfterCleanExcludeChars) throws WordNotValidInDictionaryException {
        WordNotValidInDictionaryException wordNotValidInDictionary = new WordNotValidInDictionaryException(words);
        wordsToCheckAfterCleanExcludeChars.forEach(word -> {
            if(!words.contains(word)) {
                wordNotValidInDictionary.addIllegalWord(word);
            }
        });

        if(wordNotValidInDictionary.isExceptionNeedToThrown()) {
            throw wordNotValidInDictionary;
        }
    }

    public boolean isAtDictionary(String encryptionOutput) {

        String[] allWord = encryptionOutput.split(" ");
        //the whole word is just spaces
        if(allWord.length==0)
        {
            return false;
        }

        for (int i = 0; i <allWord.length ; i++) {
            if(!words.contains(allWord[i]))
            {
                return false;
            }
        }
        return true;
    }

    public String cleanWord(String text) {
        StringBuilder stringBuilder= new StringBuilder(text);
        for (Character excludeChar : excludeChars) {
            for (int i = 0; i < stringBuilder.length(); i++) {
                if(stringBuilder.charAt(i)==excludeChar)
                {
                    stringBuilder.deleteCharAt(i);
                }
            }
        }

        return stringBuilder.toString();
    }
}