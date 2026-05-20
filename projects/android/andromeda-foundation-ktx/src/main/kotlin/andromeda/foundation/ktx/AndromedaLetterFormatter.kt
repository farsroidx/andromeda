@file:Suppress("unused")

package andromeda.foundation.ktx

import andromeda.foundation.formatter.AndromedaLetterFormatter

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

val String.persianNumerals: String
    get() = this.toPersianNumerals()

val String.arabicNumerals: String
    get() = this.toArabicNumerals()

val String.englishNumerals: String
    get() = this.toEnglishNumerals()

val String.standardizedPersian: String
    get() = this.standardizePersianText()

fun String.toPersianNumerals(): String = AndromedaLetterFormatter.toPersianNumerals(this)

fun String.toArabicNumerals(): String = AndromedaLetterFormatter.toArabicNumerals(this)

fun String.toEnglishNumerals(): String = AndromedaLetterFormatter.toEnglishNumerals(this)

fun String.arabicToPersian(convertNumerals: Boolean = true): String = AndromedaLetterFormatter.arabicToPersian(this, convertNumerals)

fun String.persianToArabic(convertNumerals: Boolean = true): String = AndromedaLetterFormatter.persianToArabic(this, convertNumerals)

fun String.standardizePersianText(): String = AndromedaLetterFormatter.standardizePersianText(this)

fun String.detectNumerals(): Map<String, Boolean> = AndromedaLetterFormatter.detectNumerals(this)

fun String.extractNumbers(): List<String> = AndromedaLetterFormatter.extractNumbers(this)

fun String.normalizeForComparison(): String = AndromedaLetterFormatter.normalizeForComparison(this)
