package com.heracles.troco.data.mapper

import com.heracles.troco.data.local.entity.ProductEntity
import com.heracles.troco.domain.model.Product

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        name = this.name,
        price = this.price,
        quantity = this.quantity,
        status = this.status
    )
}