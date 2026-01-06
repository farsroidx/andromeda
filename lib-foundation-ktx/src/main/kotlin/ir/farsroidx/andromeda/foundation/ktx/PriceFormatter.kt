@file:Suppress("UnusedVariable", "unused")

package ir.farsroidx.andromeda.foundation.ktx

import ir.farsroidx.andromeda.foundation.format.PriceFormatter

fun Number?.formatPrice(
    thousandSeparator: Char = ',',
    decimalSeparator: Char = '.',
    decimalPlaces: Int = 0,
): String =
    PriceFormatter.format(
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
    PriceFormatter.format(
        value = this,
        thousandSeparator = thousandSeparator,
        decimalSeparator = decimalSeparator,
        decimalPlaces = decimalPlaces,
    )

fun String?.removePriceFormat(
    thousandSeparator: Char = ',',
    decimalSeparator: Char = '.',
): String =
    PriceFormatter.removeFormatting(
        formattedValue = this,
        thousandSeparator = thousandSeparator,
        decimalSeparator = decimalSeparator,
    )

fun String?.parsePrice(
    thousandSeparator: Char = ',',
    decimalSeparator: Char = '.',
): Double =
    PriceFormatter.parse(
        formattedValue = this,
        thousandSeparator = thousandSeparator,
        decimalSeparator = decimalSeparator,
    )

fun String?.cleanNumber(): String = PriceFormatter.cleanNumberString(this)
