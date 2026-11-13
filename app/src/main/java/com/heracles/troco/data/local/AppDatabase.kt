package com.heracles.troco.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.heracles.troco.data.local.dao.FiadoTransactionDao
import com.heracles.troco.data.local.dao.PendingSyncOperationDao
import com.heracles.troco.data.local.dao.ProductDao
import com.heracles.troco.data.local.dao.UserFiadoDao
import com.heracles.troco.data.local.entity.FiadoTransactionEntity
import com.heracles.troco.data.local.entity.PendingSyncOperationEntity
import com.heracles.troco.data.local.entity.ProductEntity
import com.heracles.troco.data.local.entity.UserFiadoEntity

@TypeConverters(Converters::class)
@Database(
    entities = [
        ProductEntity::class,
        PendingSyncOperationEntity::class,
        UserFiadoEntity::class,
        FiadoTransactionEntity::class
    ],
    version = 4
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun pendingSyncOperationDao(): PendingSyncOperationDao
    abstract fun userFiadoDao(): UserFiadoDao
    abstract fun fiadoTransactionDao(): FiadoTransactionDao
}