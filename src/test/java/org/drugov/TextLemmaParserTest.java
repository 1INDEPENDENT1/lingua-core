package org.drugov;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextLemmaParserTest {

    @Test
    void sortWordsOnRussianAndEnglishWords_handlesMixedText() throws IOException {
        TextLemmaParser parser = new TextLemmaParser();
        String text = "Узнаю эти широкие плечи I recognize those broad shoulders";

        Map<String, Integer> expected = new HashMap<>();
        expected.put("узнать", 1);
        expected.put("этот", 1);
        expected.put("широкий", 1);
        expected.put("плечо", 1);
        expected.put("i", 1);
        expected.put("recognize", 1);
        expected.put("that", 1);
        expected.put("broad", 1);
        expected.put("shoulder", 1);

        assertEquals(expected, parser.sortWordsOnRussianAndEnglishWords(text));
    }

    @Test
    void getZeroForm_stripsPunctuationBeforeLemmatization() throws IOException {
        TextLemmaParser parser = new TextLemmaParser();

        assertEquals("широкий", parser.getZeroForm("широкие,"));
        assertEquals("shoulder", parser.getZeroForm("shoulders!"));
    }
}
