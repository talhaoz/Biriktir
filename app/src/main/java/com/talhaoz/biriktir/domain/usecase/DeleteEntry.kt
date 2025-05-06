package com.talhaoz.biriktir.domain.usecase

import com.talhaoz.biriktir.domain.repository.SavingRepository
import javax.inject.Inject

class DeleteEntry @Inject constructor(
    private val repository: SavingRepository
) {
    suspend operator fun invoke(entryId: Int) = repository.deleteEntry(entryId)
}
