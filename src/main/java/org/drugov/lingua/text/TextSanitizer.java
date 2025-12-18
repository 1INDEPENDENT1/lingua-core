package org.drugov.lingua.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for cleaning and tokenizing text.
 */
public final class TextSanitizer {

    private static final Pattern HTML_TAG = Pattern.compile("<[^>]+>");
    private static final Pattern TOKEN_PATTERN = Pattern.compile("[A-Za-zА-Яа-яЁё]+");

    private TextSanitizer() {
    }

    public static String removeHtml(String text) {
        if (text == null) {
            return "";
        }
        return HTML_TAG.matcher(text).replaceAll(" ");
    }

    public static String normalizeToken(String token) {
        if (token == null) {
            return "";
        }
        String trimmed = token.trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        return trimmed.toLowerCase(Locale.ROOT);
    }

    public static List<String> tokenize(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }
        String sanitized = removeHtml(text);
        Matcher matcher = TOKEN_PATTERN.matcher(sanitized);
        List<String> tokens = new ArrayList<>();
        while (matcher.find()) {
            String token = normalizeToken(matcher.group());
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
        return tokens;
    }
}
