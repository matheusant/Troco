package com.heracles.troco.domain.usecase.auth

import com.heracles.troco.domain.model.User
import com.heracles.troco.domain.repository.UserProfileRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke(user: User) = userProfileRepository.saveUserInfo(user)
}