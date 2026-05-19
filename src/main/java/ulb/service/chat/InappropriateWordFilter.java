package ulb.service.chat;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class responsible for censoring inappropriate words inside chat messages.
 */
public class InappropriateWordFilter {

	/**
	 * Pattern used to find words made of letters.
	 *
	 * Explanation of "\\p{L}+":
	 * - "\\p{L}" means "any Unicode letter": this includes letters beyond plain ASCII, so accented letters are
	 * supported.
	 * - The "+" means "one or more times".
	 *
	 * As a result, this pattern finds sequences such as "hello", "école" or "Connard",
	 * while naturally ignoring surrounding punctuation such as '!', '?', ',' or '.'.
	 */
	private static final Pattern WORD_PATTERN = Pattern.compile("\\p{L}+");

	/**
	 * Default list of inappropriate words.
	 *
	 * This list is intentionally stored in lowercase. During construction, each word is
	 * normalized before being stored, so comparisons remain case-insensitive and accent-insensitive.
	 */
	private static final Set<String> DEFAULT_WORDS = Set.of("abruti", "connard", "connasse", "conne", "cretin",
			"debile", "encule", "idiot", "imbecile", "merde", "pute", "salope", "saloppe", "stupide");

	/**
	 * Internal set used for comparisons.
	 *
	 * The stored words are normalized versions of the inappropriate words.
	 */
	private final Set<String> inappropriateWords;

	// Builds a filter with the default inappropriate words.
	public InappropriateWordFilter() {
		this(DEFAULT_WORDS);
	}

	/**
	 * Builds a filter with a custom set of inappropriate words.
	 *
	 * Each provided word is normalized before being stored:
	 * - accents are removed,
	 * - letters are converted to lowercase,
	 * - blank entries are ignored.
	 *
	 * @param inappropriateWords the words that must be censored when found in a message
	 */
	public InappropriateWordFilter(Set<String> inappropriateWords) {
		// Create a temporary set that will store the cleaned words.
		Set<String> normalizedWords = new HashSet<>();

		for (String word : inappropriateWords) {
			String normalizedWord = normalize(word);
			if (!normalizedWord.isBlank()) {
				normalizedWords.add(normalizedWord);
			}
		}

		this.inappropriateWords = Set.copyOf(normalizedWords);
	}

	/**
	 * Normalizes a word so that comparisons are easier and more tolerant.
	 *
	 * This method performs two transformations:
	 * - Remove accents/diacritical marks.
	 * - Convert the word to lowercase.
	 *
	 * Examples:
	 * - "Crétin" -> "cretin"
	 * - "DÉBILE" -> "debile"
	 *
	 * @param word the word to normalize
	 * @return the normalized word
	 */
	private static String normalize(String word) {
		// NFD decomposes accented characters into base letters + combining marks (accents, ...).
		String normalized = Normalizer.normalize(word, Normalizer.Form.NFD);

		// Remove the combining marks (for example accents) that were separated by NFD.
		String withoutDiacritics = normalized.replaceAll("\\p{M}+", "");

		// Lowercase the result so that comparisons are case-insensitive.
		return withoutDiacritics.toLowerCase();
	}

	/**
	 * Returns a censored version of the given message. (= main function)
	 *
	 * How it works:
	 * - Find each word in the message using "WORD_PATTERN".
	 * - Normalize the found word so that the comparison ignores case and accents.
	 * - If the word belongs to the inappropriate word list, replace it with a masked version.
	 * - If not, keep the original word unchanged.
	 *
	 * Important detail: punctuation is preserved because only the word itself is replaced.
	 * For example, "Connard!" becomes "C*****d!".
	 *
	 * @param message the original message
	 * @return the censored message, or the original value if it is null or empty
	 */
	public String censor(String message) {
		// If there is nothing to process, return the input as-is.
		if (message == null || message.isEmpty()) {
			return message;
		}

		// Create a matcher that will scan the message and find each word.
		Matcher matcher = WORD_PATTERN.matcher(message);

		// StringBuffer is used here because the Matcher API appendReplacement/appendTail
		// works directly with StringBuffer.
		StringBuffer censored = new StringBuffer();

		while (matcher.find()) {
			// The exact word found in the original message, preserving its original case.
			String word = matcher.group();

			// Compare a normalized version of the found word with the normalized banned words.
			if (this.inappropriateWords.contains(normalize(word))) {
				// Replace only the matched word with its masked form.
				// Matcher.quoteReplacement(...) is used to ensure the replacement text is treated
				// as plain text, not as a special regular-expression replacement string.
				matcher.appendReplacement(censored, Matcher.quoteReplacement(mask(word)));
			} else {
				// Keep normal words unchanged.
				matcher.appendReplacement(censored, Matcher.quoteReplacement(word));
			}
		}

		// Append the remaining part of the message that comes after the last match.
		matcher.appendTail(censored);
		return censored.toString();
	}

	/**
	 * Masks the inside of a word while keeping its first and last characters visible.
	 *
	 * Examples: "cat" --> "c*t"; "shit" --> "s**t"; "ab" --> "ab"; "a" --> "a"
	 *
	 * Words of length 1 or 2 are left unchanged because there is no meaningful "middle"
	 * to hide while preserving the first and last characters.
	 *
	 * @param word the word to mask
	 * @return the masked word
	 */
	private static String mask(String word) {
		if (word.length() <= 2) {
			return word;
		}
		return word.charAt(0) + "*".repeat(word.length() - 2) + word.charAt(word.length() - 1);
	}
}
