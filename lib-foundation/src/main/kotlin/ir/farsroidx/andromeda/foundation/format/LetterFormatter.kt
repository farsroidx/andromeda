@file:Suppress("unused")

package ir.farsroidx.andromeda.foundation.format

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

object LetterFormatter {

    private val persianToEnglishNumerals = mapOf(
        '۰' to '0', '۱' to '1', '۲' to '2', '۳' to '3', '۴' to '4',
        '۵' to '5', '۶' to '6', '۷' to '7', '۸' to '8', '۹' to '9'
    )

    private val arabicToEnglishNumerals = mapOf(
        '٠' to '0', '١' to '1', '٢' to '2', '٣' to '3', '٤' to '4',
        '٥' to '5', '٦' to '6', '٧' to '7', '٨' to '8', '٩' to '9'
    )

    private val englishToPersianNumerals = mapOf(
        '0' to '۰', '1' to '۱', '2' to '۲', '3' to '۳', '4' to '۴',
        '5' to '۵', '6' to '۶', '7' to '۷', '8' to '۸', '9' to '۹'
    )

    private val englishToArabicNumerals = mapOf(
        '0' to '٠', '1' to '١', '2' to '٢', '3' to '٣', '4' to '٤',
        '5' to '٥', '6' to '٦', '7' to '٧', '8' to '٨', '9' to '٩'
    )

    private val arabicToPersianLetters = mapOf(
        'ك' to 'ک', 'ي' to 'ی', 'ة' to 'ه', 'ؤ' to 'و', 'إ' to 'ا',
        'أ' to 'ا', 'آ' to 'آ', 'ى' to 'ی', 'ئ' to 'ی', 'ٱ' to 'ا',
        'ۂ' to 'ه', 'ۃ' to 'ه', 'ۅ' to 'و', 'ۆ' to 'و', 'ۇ' to 'و',
        'ۈ' to 'و', 'ۉ' to 'و', 'ې' to 'ی', 'ۑ' to 'ی', 'ے' to 'ی'
    )

    private val persianToArabicLetters = mapOf(
        'ک' to 'ك', 'ی' to 'ي', 'ه' to 'ة', 'آ' to 'آ', 'ا' to 'ا'
    ).plus(arabicToPersianLetters.entries.associate { (k, v) -> v to k })

    private val commonReplacements = mapOf(
        "ﻻ" to "لا", "ﻷ" to "لأ", "ﻹ" to "لإ", "ﻵ" to "لآ",
        "ﻼ" to "لا", "﷼" to "ریال", "﷽" to "بسم الله الرحمن الرحیم"
    )

    fun toPersianNumerals(value: String): String {
        return value.map { char ->
            when (char) {
                in englishToPersianNumerals -> englishToPersianNumerals[char]!!
                in arabicToEnglishNumerals -> {
                    val englishNum = arabicToEnglishNumerals[char]!!
                    englishToPersianNumerals[englishNum]!!
                }
                else -> char
            }
        }.joinToString("")
    }

    fun toArabicNumerals(value: String): String {
        return value.map { char ->
            when (char) {
                in englishToArabicNumerals -> englishToArabicNumerals[char]!!
                in persianToEnglishNumerals -> {
                    val englishNum = persianToEnglishNumerals[char]!!
                    englishToArabicNumerals[englishNum]!!
                }
                else -> char
            }
        }.joinToString("")
    }

    fun toEnglishNumerals(value: String): String {
        return value.map { char ->
            when (char) {
                in persianToEnglishNumerals -> persianToEnglishNumerals[char]!!
                in arabicToEnglishNumerals -> arabicToEnglishNumerals[char]!!
                else -> char
            }
        }.joinToString("")
    }

    fun arabicToPersian(value: String, convertNumerals: Boolean = true): String {

        var result = value

        // Convert common text issues
        commonReplacements.forEach { (from, to) ->
            result = result.replace(from, to)
        }

        // Convert Arabic letters to Persian
        result = result.map { char ->
            arabicToPersianLetters[char] ?: char
        }.joinToString("")

        // Convert numerals if requested
        if (convertNumerals) {
            result = toPersianNumerals(result)
        }

        return result
    }

    fun persianToArabic(value: String, convertNumerals: Boolean = true): String {

        var result = value

        // Convert Persian letters to Arabic
        result = result.map { char ->
            persianToArabicLetters[char] ?: char
        }.joinToString("")

        // Convert numerals if requested
        if (convertNumerals) {
            result = toArabicNumerals(result)
        }

        return result
    }

    fun standardizePersianText(value: String): String {

        var result = arabicToPersian(value, convertNumerals = true)

        // Fix spacing issues
        result = result
            .replace(Regex("\\s+"), " ") // Multiple spaces to single space
            .replace(Regex("\\s*,\\s*"), "، ") // Fix comma spacing
            .replace(Regex("\\s*\\.\\s*"), ". ") // Fix period spacing
            .trim()

        // Fix common Persian punctuation
        result = result
            .replace('?', '؟')
            .replace(';', '؛')

        return result
    }

    fun detectNumerals(value: String): Map<String, Boolean> {

        var hasPersian = false
        var hasArabic = false
        var hasEnglish = false

        for (char in value) {
            when (char) {
                in persianToEnglishNumerals -> hasPersian = true
                in arabicToEnglishNumerals -> hasArabic = true
                in '0'..'9' -> hasEnglish = true
            }

            // Early exit if all found
            if (hasPersian && hasArabic && hasEnglish) break
        }

        val isMixed = listOf(hasPersian, hasArabic, hasEnglish).count { it } > 1

        return mapOf(
            "hasPersian" to hasPersian,
            "hasArabic"  to hasArabic,
            "hasEnglish" to hasEnglish,
            "isMixed"    to isMixed
        )
    }

    fun extractNumbers(value: String): List<String> {
        val englishText = toEnglishNumerals(value)
        return Regex("\\d+(\\.\\d+)?").findAll(englishText)
            .map { it.value }
            .toList()
    }

    fun normalizeForComparison(value: String): String {

        var result = arabicToPersian(value, convertNumerals = false) // Convert letters only
            result = toEnglishNumerals(result) // Convert all numerals to English

        return result
    }

    fun containsPersianOrArabic(value: String): Boolean {
        val persianArabicRange = Regex("[\u0600-\u06FF]")
        return persianArabicRange.containsMatchIn(value)
    }

    fun getDominantNumeralSystem(value: String): String {

        val detection = detectNumerals(value)

        val counts = listOf(
            "Persian" to (if (detection["hasPersian"] == true) 1 else 0),
            "Arabic"  to (if (detection["hasArabic"]  == true) 1 else 0),
            "English" to (if (detection["hasEnglish"] == true) 1 else 0)
        )

        val trueCount = counts.count { it.second > 0 }

        return when {
            trueCount               == 0    -> "None"
            trueCount               >  1    -> "Mixed"
            detection["hasPersian"] == true -> "Persian"
            detection["hasArabic"]  == true -> "Arabic"
            detection["hasEnglish"] == true -> "English"
            else                            -> "None"
        }
    }
}