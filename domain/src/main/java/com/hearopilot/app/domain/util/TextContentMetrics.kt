package com.hearopilot.app.domain.util

/**
 * Language-agnostic content significance helpers.
 *
 * Whitespace-delimited word counts under-count Japanese and Cantonese because
 * sentences commonly contain no spaces. CJK text is therefore measured by
 * Han/Hiragana/Katakana code points while other scripts keep word semantics.
 */
object TextContentMetrics {
    fun hasEnoughContent(
        text: String,
        minWords: Int,
        minCjkChars: Int = minWords * 2
    ): Boolean {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return false

        val cjkChars = trimmed.codePoints()
            .filter { cp -> isCjkCodePoint(cp) }
            .count()

        if (cjkChars >= minCjkChars.toLong()) return true

        val words = trimmed.split(Regex("\\s+")).count { token ->
            token.any { it.isLetterOrDigit() }
        }
        return words >= minWords
    }

    private fun isCjkCodePoint(cp: Int): Boolean =
        cp in 0x3400..0x4DBF || // CJK Extension A
            cp in 0x4E00..0x9FFF || // CJK Unified Ideographs
            cp in 0x3040..0x309F || // Hiragana
            cp in 0x30A0..0x30FF || // Katakana
            cp in 0x31F0..0x31FF    // Katakana Phonetic Extensions
}
