package io.github.kabirnayeem99.dumarketingadmin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.dumarketingadmin.data.repositories.InformationRepository
import io.github.kabirnayeem99.dumarketingadmin.data.model.InformationData
import javax.inject.Inject


@HiltViewModel
class InformationViewModel @Inject constructor(val repo: InformationRepository) : ViewModel() {

    fun upsertInformation(informationData: InformationData): Task<Void> =
        repo.upsertInformationDataToDb(informationData)

    fun getInformationData() = repo.getInformationData()
}