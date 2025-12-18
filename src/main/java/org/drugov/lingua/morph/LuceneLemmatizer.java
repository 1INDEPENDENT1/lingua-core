package org.drugov.lingua.morph;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.english.EnglishLuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.drugov.lingua.lang.Language;
import org.drugov.lingua.model.LemmaResult;
import org.drugov.lingua.text.TextSanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Lucene Morphology based lemmatizer for Russian and English.
 */
public class LuceneLemmatizer implements Lemmatizer {

    private static final Logger log = LoggerFactory.getLogger(LuceneLemmatizer.class);
    private static final Pattern RU_PATTERN = Pattern.compile("[а-яё]+");
    private static final Pattern EN_PATTERN = Pattern.compile("[a-z]+");

    private final LuceneMorphology russianMorphology;
    private final LuceneMorphology englishMorphology;
    private final boolean filterServiceParts;

    public LuceneLemmatizer() {
        this(true);
    }

    public LuceneLemmatizer(boolean filterServiceParts) {
        this.filterServiceParts = filterServiceParts;
        try {
            this.russianMorphology = new RussianLuceneMorphology();
            this.englishMorphology = new EnglishLuceneMorphology();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize Lucene morphology", e);
        }
    }

    @Override
    public String lemma(String token) {
        if (token == null) {
            return "";
        }
        String normalized = TextSanitizer.normalizeToken(token);
        if (normalized.isEmpty()) {
            return normalized;
        }
        Language language = detectLanguage(normalized);
        try {
            switch (language) {
                case RU:
                    return primaryLemma(russianMorphology, normalized);
                case EN:
                    return primaryLemma(englishMorphology, normalized);
                default:
                    return normalized;
            }
        } catch (RuntimeException e) {
            log.warn("Could not lemmatize token: {}", normalized, e);
            return normalized;
        }
    }

    @Override
    public Set<String> lemmas(String text) {
        return analyze(text).getLemmas();
    }

    @Override
    public LemmaResult analyze(String text) {
        List<String> tokens = tokenize(text);
        Set<String> result = new HashSet<>();
        List<String> problematic = new ArrayList<>();
        for (String token : tokens) {
            try {
                String lemma = lemma(token);
                if (shouldInclude(token, lemma)) {
                    result.add(lemma);
                }
            } catch (RuntimeException e) {
                problematic.add(token);
                log.warn("Error while processing token: {}", token, e);
            }
        }
        return new LemmaResult(result, problematic);
    }

    @Override
    public List<String> tokenize(String text) {
        return TextSanitizer.tokenize(text);
    }

    private boolean shouldInclude(String token, String lemma) {
        if (!filterServiceParts) {
            return true;
        }
        Language language = detectLanguage(token);
        try {
            if (language == Language.RU) {
                return russianMorphology.getMorphInfo(token)
                        .stream()
                        .noneMatch(this::isServicePart);
            }
            if (language == Language.EN) {
                return englishMorphology.getMorphInfo(token)
                        .stream()
                        .noneMatch(this::isServicePart);
            }
        } catch (RuntimeException e) {
            log.warn("Failed to check service part for token: {}", token, e);
            return true;
        }
        return true;
    }

    private String primaryLemma(LuceneMorphology morphology, String token) {
        List<String> normalForms = morphology.getNormalForms(token);
        if (normalForms.isEmpty()) {
            return token;
        }
        return normalForms.get(0);
    }

    private Language detectLanguage(String token) {
        if (RU_PATTERN.matcher(token).matches()) {
            return Language.RU;
        }
        if (EN_PATTERN.matcher(token).matches()) {
            return Language.EN;
        }
        return Language.AUTO;
    }

    private boolean isServicePart(String morphInfo) {
        String upper = morphInfo.toUpperCase();
        return upper.contains("ПРЕДЛ")
                || upper.contains("СОЮЗ")
                || upper.contains("МЕЖД")
                || upper.contains("ЧАСТ")
                || upper.contains("МС")
                || upper.contains("PREP")
                || upper.contains("CONJ")
                || upper.contains("ARTICLE")
                || upper.contains("PRON");
    }
}
