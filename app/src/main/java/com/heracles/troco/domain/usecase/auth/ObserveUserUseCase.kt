package com.heracles.troco.domain.usecase.auth

import com.heracles.troco.domain.model.User
import com.heracles.troco.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) {
    operator fun invoke(): Flow<User?> = userProfileRepository.observe()
}