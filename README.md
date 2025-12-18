# lingua-core

A lightweight Java 17 library that provides Russian/English lemmatization and tokenization on top of Lucene Morphology without any web or Spring dependencies. The module is intended to be embedded into larger applications (e.g., "Puzzle→Anki") as a standalone JAR.

## Features
- Normalizes tokens (HTML removal, lowercasing, simple tokenization for Latin and Cyrillic text).
- Computes lemmas (base forms) for Russian and English using Lucene Morphology (local jars in `lucene-jars`).
- Extracts unique lemmas from arbitrary text/HTML and reports problematic tokens via `LemmaResult`.
- Optional filtering of service parts of speech (enabled by default, configurable via constructor).

## Project structure
- `org.drugov.lingua.morph.Lemmatizer` — public API for single-token lemmatization, full-text analysis, and tokenization.
- `org.drugov.lingua.morph.LuceneLemmatizer` — Lucene-based implementation with RU/EN language detection and optional service-part filtering.
- `org.drugov.lingua.text.TextSanitizer` — HTML stripping, token normalization, and tokenization utilities.
- `org.drugov.lingua.model.LemmaResult` — container for unique lemmas plus a list of tokens that could not be processed cleanly.
- `org.drugov.lingua.lang.Language` — language enum used for internal detection and configuration.

## Usage
Add the Lucene morphology jars from `lucene-jars/` to your local Maven repository or keep the system-scoped dependency paths unchanged. Build the module:

```bash
mvn clean package
```

Consume the API from your application:

```java
import org.drugov.lingua.morph.Lemmatizer;
import org.drugov.lingua.morph.LuceneLemmatizer;

Lemmatizer lemmatizer = new LuceneLemmatizer();          // filters service parts of speech by default
String lemma = lemmatizer.lemma("maintained");           // -> "maintain"
Set<String> lemmas = lemmatizer.lemmas("Yakone has maintained his grip on the underworld");

// Disable service-part filtering if you need every detected lemma
Lemmatizer rawLemmatizer = new LuceneLemmatizer(false);
```

For more detail on the processing pipeline, inspect `LuceneLemmatizer`: it normalizes tokens, detects language via regex (`[а-яё]+` / `[a-z]+`), routes to the appropriate Lucene morphology, and optionally skips service parts of speech when collecting lemmas. Tokenization relies on `TextSanitizer` to drop HTML tags and extract alphabetic runs from mixed text.

## Testing
Run the bundled unit tests:

```bash
mvn test
```

Tests cover basic RU/EN lemmatization, mixed-text lemma extraction, and HTML tokenization examples.
