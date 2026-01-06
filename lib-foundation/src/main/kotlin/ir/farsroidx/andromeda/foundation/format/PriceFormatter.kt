@file:Suppress("unused")

package ir.farsroidx.andromeda.foundation.format

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object PriceFormatter {
    private val cache = mutableMapOf<String, DecimalFormat>()

    fun format(
        value: Any?,
        thousandSeparator: Char = ',',
        decimalSeparator: Char = '.',
        decimalPlaces: Int = 0,
        prefix: String = "",
        suffix: String = "",
    ): String {
        val number = parseToDouble(value)

        val pattern = buildDecimalPattern(decimalPlaces)
        val cacheKey = "${thousandSeparator}_${decimalSeparator}_$decimalPlaces"

        val formatter =
            cache.getOrPut(cacheKey) {
                DecimalFormat(
                    pattern,
                    DecimalFormatSymbols(Locale.getDefault()).apply {
                        groupingSeparator = thousandSeparator
                        this.decimalSeparator = decimalSeparator
                        monetaryDecimalSeparator = decimalSeparator
                    },
                ).apply {
                    isGroupingUsed = true
                    maximumFractionDigits = decimalPlaces
                    minimumFractionDigits = decimalPlaces
                }
            }

        return try {
            "$prefix${formatter.format(number)}$suffix"
        } catch (e: IllegalArgumentException) {
            "${prefix}0$suffix"
        } catch (e: ArithmeticException) {
            "${prefix}0$suffix"
        }
    }

    fun parse(
        formattedValue: String?,
        thousandSeparator: Char = ',',
        decimalSeparator: Char = '.',
    ): Double {
        if (formattedValue.isNullOrBlank()) return 0.0

        val cleanString =
            formattedValue
                .replace(thousandSeparator.toString(), "")
                .replace(decimalSeparator.toString(), ".")
                .replace("[^\\d.-]".toRegex(), "")

        return cleanString.toDoubleOrNull() ?: 0.0
    }

    fun removeFormatting(
        formattedValue: String?,
        thousandSeparator: Char = ',',
        decimalSeparator: Char = '.',
    ): String {
        if (formattedValue.isNullOrBlank()) return "0"

        return formattedValue
            .replace(thousandSeparator.toString(), "")
            .replace(decimalSeparator.toString(), ".")
            .replace("[^\\d.-]".toRegex(), "")
            .ifBlank { "0" }
    }

    fun cleanNumberString(input: String?): String {
        if (input.isNullOrBlank()) return "0"

        return input
            .replace("[^\\d.]".toRegex(), "")
            .replace("\\.+".toRegex(), ".")
            .removePrefix(".")
            .removeSuffix(".")
            .ifBlank { "0" }
    }

    private fun parseToDouble(value: Any?): Double =
        when (value) {
            null -> 0.0
            is Number -> value.toDouble()
            is String -> value.toDoubleOrNull() ?: 0.0
            else -> 0.0
        }

    private fun buildDecimalPattern(decimalPlaces: Int): String =
        buildString {
            append("#,##0")
            if (decimalPlaces > 0) {
                append(".")
                repeat(decimalPlaces) { append("0") }
            }
        }
}
