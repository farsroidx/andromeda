@file:Suppress("UnusedVariable", "unused")

package andromeda.foundation.ktx

import andromeda.foundation.formatter.AndromedaPriceFormatter

// @formatter:off // TODO: Do not remove this line to preserve the code style ----------------------

fun Number?.formatPrice(
    thousandSeparator: Char = ',',
    decimalSeparator: Char = '.',
    decimalPlaces: Int = 0,
): String =
    AndromedaPriceFormatter.format(
        value = this,
        thousandSeparator = thousandSeparator,
        decimalSeparator = decimalSeparator,
        decimalPlaces = decimalPlaces,
    )

fun String?.formatPrice(
    thousandSeparator: Char = ',',
    decimalSeparator: Char = '.',
    decimalPlaces: Int = 0,
): String =
    AndromedaPriceFormatter.format(
        value = this,
        thousandSeparator = thousandSeparator,
        decimalSeparator = decimalSeparator,
        decimalPlaces = decimalPlaces,
    )

fun String?.removePriceFormat(
    thousandSeparator: Char = ',',
    decimalSeparator: Char = '.',
): String =
    AndromedaPriceFormatter.removeFormatting(
        formattedValue = this,
        thousandSeparator = thousandSeparator,
        decimalSeparator = decimalSeparator,
    )

fun String?.parsePrice(
    thousandSeparator: Char = ',',
    decimalSeparator: Char = '.',
): Double =
    AndromedaPriceFormatter.parse(
        formattedValue = this,
        thousandSeparator = thousandSeparator,
        decimalSeparator = decimalSeparator,
    )

fun String?.cleanNumber(): String = AndromedaPriceFormatter.cleanNumberString(this)
