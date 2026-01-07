@file:Suppress("unused")

package ir.farsroidx.andromeda.foundation.ktx

import ir.farsroidx.andromeda.foundation.format.LetterFormatter

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

val String.persianNumerals: String
    get() = this.toPersianNumerals()

val String.arabicNumerals: String
    get() = this.toArabicNumerals()

val String.englishNumerals: String
    get() = this.toEnglishNumerals()

val String.standardizedPersian: String
    get() = this.standardizePersianText()

fun String.toPersianNumerals(): String = LetterFormatter.toPersianNumerals(this)

fun String.toArabicNumerals(): String = LetterFormatter.toArabicNumerals(this)

fun String.toEnglishNumerals(): String = LetterFormatter.toEnglishNumerals(this)

fun String.arabicToPersian(convertNumerals: Boolean = true): String = LetterFormatter.arabicToPersian(this, convertNumerals)

fun String.persianToArabic(convertNumerals: Boolean = true): String = LetterFormatter.persianToArabic(this, convertNumerals)

fun String.standardizePersianText(): String = LetterFormatter.standardizePersianText(this)

fun String.detectNumerals(): Map<String, Boolean> = LetterFormatter.detectNumerals(this)

fun String.extractNumbers(): List<String> = LetterFormatter.extractNumbers(this)

fun String.normalizeForComparison(): String = LetterFormatter.normalizeForComparison(this)
