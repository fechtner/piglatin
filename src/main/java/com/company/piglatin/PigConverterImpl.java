package com.company.piglatin;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Basic implementation of conversion into pig-latin.
 */
public class PigConverterImpl implements PigConverter {
    /**
     * Hyphen character to split words.
     */
    private static final String HYPHEN = "-";
    /**
     * Separators between words.
     */
    private static final String WORD_SEPARATORS = " \n\t";
    /**
     * Static suffix to don't do any transforamtions.
     */
    private static final String STATIC_SUFFIX = "way";
    /**
     * Which characters are vowels.
     */
    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList('a', 'e', 'i', 'o', 'u'));
    /**
     * Which characters are consonants.
     */
    private static final Set<Character> CONSONANTS = new HashSet<>(Arrays.asList('b', 'c', 'd', 'f', 'g', 'h',
            'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'));
    /**
     * Which characters are punctuations.
     */
    private static final Set<Character> PUNCTUATIONS = new HashSet<>(Arrays.asList('\'', '.', ',', ';', ':'));
    /**
     * Suffix for words starting with vowel.
     */
    private static final String VOWEL_SUFFIX = "way";
    /**
     * Suffix for words starting with consonant.
     */
    private static final String CONSONANT_SUFFIX = "ay";

    @Override
    public String convert(final String inputString) {
        if (inputString == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        String[] words = StringUtils.split(inputString, WORD_SEPARATORS);
        return Arrays.stream(words)
                .map(this::convertHyphens)
                .collect(Collectors.joining(" "));
    }

    /**
     * Convart words, which may contain hyphens.
     * @param inputHyphens word for conversion
     * @return converted words
     */
    private String convertHyphens(final String inputHyphens) {
        String[] words = StringUtils.split(inputHyphens, HYPHEN);
        return Arrays.stream(words)
                .map(this::convertWord)
                .collect(Collectors.joining(HYPHEN));
    }

    /**
     * Convert simple word without whitespaces and hyphens.
     * @param inputWord  word for conversion
     * @return converted words
     */
    private String convertWord(final String inputWord) {
        if (inputWord.length() == 0) {
            return inputWord;
        }

        if (inputWord.endsWith(STATIC_SUFFIX)) {
            return inputWord;
        }

        List<Integer> capitalIndices = findOutCapitalIndices(inputWord);
        List<Punctuation> punctuations = findOutPunctuations(inputWord);

        String loweredWord = inputWord.toLowerCase();
        loweredWord = removePunctuations(loweredWord);

        if (loweredWord.length() > 0) {
            Character firstLetter = loweredWord.charAt(0);
            if (VOWELS.contains(firstLetter)) {
                loweredWord = transformVowelWord(loweredWord);
            } else if (CONSONANTS.contains(firstLetter)) {
                loweredWord = transformConsonantWord(loweredWord);
            }
        }

        String transformedWord = insertPunctuations(loweredWord, punctuations);
        transformedWord = capitalize(transformedWord, capitalIndices);
        return transformedWord;
    }

    /**
     * Remove all punctuations from word.
     * @param originalWord original word for transformation
     * @return transformed word
     */
    protected String removePunctuations(String originalWord) {
        StringBuilder transformedWord = new StringBuilder();

        for (int i = 0; i < originalWord.length(); i++) {
            char ch = originalWord.charAt(i);
            if (!PUNCTUATIONS.contains(ch)) {
                transformedWord.append(ch);
            }
        }

        return transformedWord.toString();
    }

    /**
     * Transform word starting with consonant.
     * @param inputWord original word for transformation
     * @return transformed word
     */
    private String transformConsonantWord(final String inputWord) {
        String firstLetter = String.valueOf(inputWord.charAt(0));
        return inputWord.substring(1) + firstLetter + CONSONANT_SUFFIX;
    }

    /**
     * Transform word starting with vowel.
     * @param inputWord original word for transformation
     * @return transformed word
     */
    private String transformVowelWord(final String inputWord) {
        return inputWord + VOWEL_SUFFIX;
    }

    /**
     * Apply capitalization to specific indexes
     * @param originalWord original word for transformation
     * @param capitalIndices list of indices to capitalizations, starts with 0
     * @return transformed word
     */
    protected String capitalize(final String originalWord,
                                final List<Integer> capitalIndices) {
        StringBuilder transformedWord = new StringBuilder(originalWord);
        capitalIndices.forEach(index ->
                transformedWord.setCharAt(index, Character.toUpperCase(originalWord.charAt(index)))
        );

        return transformedWord.toString();
    }

    /**
     * Insert puctuations.
     * @param originalWord original word for transformation
     * @param punctuations list of punctuations to insert
     * @return transformed word
     */
    protected String insertPunctuations(final String originalWord,
                                        final List<Punctuation> punctuations) {

        StringBuilder transformedWord = new StringBuilder(originalWord);
        punctuations.forEach(punctuation -> {
            int offset = transformedWord.length() - punctuation.getFromBack();
            transformedWord.insert(offset, punctuation.getPunctuation());
        });

        return transformedWord.toString();
    }

    /**
     * Find out indices with capital letters.
     * @param inputWord original word
     * @return list of capitalized indices starting from 0
     */
    protected List<Integer> findOutCapitalIndices(final String inputWord) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < inputWord.length(); i++) {
            if (Character.isUpperCase(inputWord.charAt(i))) {
                indices.add(i);
            }
        }

        return indices;
    }

    /**
     * find out punctuations in the word.
     * @param inputWord original word
     * @return list of punctuations in the word
     */
    protected List<Punctuation> findOutPunctuations(final String inputWord) {
        List<Punctuation> punctuations = new ArrayList<>();

        int length = inputWord.length();
        for (int i = 0; i < length; i++) {
            char chr = inputWord.charAt(i);
            if (PUNCTUATIONS.contains(chr)) {
                punctuations.add(new Punctuation(length - (i + 1), chr));
            }
        }
        Collections.reverse(punctuations);
        return punctuations;
    }

    /**
     * Identifier of the punctuation.
     */
    protected static class Punctuation {
        /**
         * Punctuation offset from the end of the word.
         */
        private final int fromBack;
        /**
         * Punctuation character.
         */
        private final Character punctuation;

        Punctuation(int fromBack, Character punctuation) {
            this.fromBack = fromBack;
            this.punctuation = punctuation;
        }

        int getFromBack() {
            return fromBack;
        }

        Character getPunctuation() {
            return punctuation;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Punctuation that = (Punctuation) o;

            return new EqualsBuilder()
                    .append(fromBack, that.fromBack)
                    .append(punctuation, that.punctuation)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(fromBack)
                    .append(punctuation)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return "Punctuation{" + fromBack + ", " + punctuation + '}';
        }
    }
}
