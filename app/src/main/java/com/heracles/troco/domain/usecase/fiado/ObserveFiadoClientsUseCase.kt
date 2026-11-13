package com.heracles.troco.domain.usecase.fiado

import com.heracles.troco.domain.model.UserFiado
import com.heracles.troco.domain.repository.UserFiadoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFiadoClientsUseCase @Inject constructor(
   private val repository: UserFiadoRepository
) {
    operator fun invoke(): Flow<List<UserFiado>> = repository.observeClients()
}