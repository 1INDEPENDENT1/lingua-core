package org.drugov.lingua.morph;

import org.drugov.lingua.model.LemmaResult;

import java.util.List;
import java.util.Set;

/**
 * Lemmatization API.
 */
public interface Lemmatizer {

    /**
     * Returns lemma for single token.
     */
    String lemma(String token);

    /**
     * Returns unique set of lemmas found in text.
     */
    Set<String> lemmas(String text);

    /**
     * Detailed lemmatization information with issues collected.
     */
    LemmaResult analyze(String text);

    /**
     * Tokenize text into normalized tokens.
     */
    List<String> tokenize(String text);
}
