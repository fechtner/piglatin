package com.company.piglatin;

/**
 * Convertor into PigLatin.
 */
public interface PigConverter {
    /**
     * Converts inpu string into pig latin.
     * @param inputString string for conversion, must not be null
     * @return output string
     */
    String convert(String inputString);
}
