package com.heracles.troco.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.heracles.troco.data.local.entity.ProductEntity
import com.heracles.troco.domain.model.ProductStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER by name")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products")
    suspend fun getAllProductsOnce(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: String): ProductEntity?

    @Insert
    suspend fun insertProduct(product: ProductEntity)

    @Upsert
    suspend fun upsertProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProduct(id: String)

    @Query(
        """
            UPDATE products 
            SET quantity = :quantity, price = :price, name = :name, status = :status
            WHERE id = :id
        """)
    suspend fun updateProduct(id: String, name: String, price: Long, quantity: Int, status: ProductStatus)
}