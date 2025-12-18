package org.drugov.lingua.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Holder for lemmatization result and collected issues.
 */
public class LemmaResult {
    private final Set<String> lemmas;
    private final List<String> problematicTokens;

    public LemmaResult(Set<String> lemmas, List<String> problematicTokens) {
        this.lemmas = Collections.unmodifiableSet(new LinkedHashSet<>(lemmas));
        this.problematicTokens = Collections.unmodifiableList(problematicTokens);
    }

    public Set<String> getLemmas() {
        return lemmas;
    }

    public List<String> getProblematicTokens() {
        return problematicTokens;
    }
}
