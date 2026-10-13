package com.heracles.troco.domain.model

import com.heracles.troco.components.Constants.CRITICAL_QUANTITY

object ProductStatusPolicy {

    fun determineStatus(
        isAvailable: Boolean,
        quantity: Int
    ): ProductStatus = when {
        !isAvailable -> ProductStatus.INACTIVE
        quantity < CRITICAL_QUANTITY -> ProductStatus.CRITIC
        else -> ProductStatus.ACTIVE
    }
}