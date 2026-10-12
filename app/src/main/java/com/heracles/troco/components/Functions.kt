package com.heracles.troco.components

fun Long.formatMoney(): String {
    if (this == 0L) return "R$ 0,00"

    val value = this.toString().padStart(3, '0')

    val cents = value.takeLast(2)
    val integer = value.dropLast(2)

    val formattedInteger = integer
        .reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()

    return "R$ $formattedInteger,$cents"
}