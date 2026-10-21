package com.heracles.troco.di

import com.heracles.troco.data.remote.ProductRemoteDataSource
import com.heracles.troco.data.repository.FirebaseAuthRepository
import com.heracles.troco.data.repository.FirestoreProductDataSourceImpl
import com.heracles.troco.data.repository.ProductRepositoryImpl
import com.heracles.troco.data.repository.UserProfileRepositoryImpl
import com.heracles.troco.domain.repository.AuthRepository
import com.heracles.troco.domain.repository.ProductRepository
import com.heracles.troco.domain.repository.UserProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: FirebaseAuthRepository): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserProfileRepository(impl: UserProfileRepositoryImpl): UserProfileRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    @Singleton
    abstract fun bindProductRemoteDataSource(impl: FirestoreProductDataSourceImpl): ProductRemoteDataSource

}