package com.heracles.troco.domain.model

object ProductStatusPolicy {

    private const val CRITICAL_QUANTITY = 5

    fun determineStatus(
        isAvailable: Boolean,
        quantity: Int
    ): ProductStatus = when {
        !isAvailable -> ProductStatus.INACTIVE
        quantity < CRITICAL_QUANTITY -> ProductStatus.CRITIC
        else -> ProductStatus.ACTIVE
    }
}