package com.talhaoz.biriktir.domain.usecase

import com.talhaoz.biriktir.domain.model.SavingEntry
import com.talhaoz.biriktir.domain.repository.SavingRepository
import javax.inject.Inject

class InsertEntry @Inject constructor(
    private val repository: SavingRepository
) {
    suspend operator fun invoke(entry: SavingEntry) = repository.insertEntry(entry)
}

