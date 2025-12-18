package org.drugov.lingua;

import org.drugov.lingua.morph.LuceneLemmatizer;
import org.drugov.lingua.morph.Lemmatizer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LuceneLemmatizerTest {

    private static Lemmatizer lemmatizer;

    @BeforeAll
    static void setUp() {
        lemmatizer = new LuceneLemmatizer();
    }

    @Test
    void englishLemmaIsReturned() {
        assertEquals("maintain", lemmatizer.lemma("maintained"));
    }

    @Test
    void russianLemmaIsReturned() {
        assertEquals("сделать", lemmatizer.lemma("сделала"));
    }

    @Test
    void mixedTextContainsExpectedLemma() {
        Set<String> lemmas = lemmatizer.lemmas("Yakone has maintained his grip on the underworld");
        assertTrue(lemmas.contains("maintain"));
    }

    @Test
    void htmlIsSanitizedAndTokenized() {
        List<String> tokens = lemmatizer.tokenize("<span>Save</span> your breath.");
        assertEquals(List.of("save", "your", "breath"), tokens);
    }
}
