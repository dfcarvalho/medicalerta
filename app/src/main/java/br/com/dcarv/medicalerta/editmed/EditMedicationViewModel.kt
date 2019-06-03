package br.com.dcarv.medicalerta.editmed

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import br.com.dcarv.medicalerta.common.authentication.Authentication
import br.com.dcarv.medicalerta.common.disposeWith
import br.com.dcarv.medicalerta.common.format
import br.com.dcarv.medicalerta.common.messages.Messages
import br.com.dcarv.medicalerta.common.messages.MessagesRepository
import br.com.dcarv.medicalerta.common.model.Medication
import br.com.dcarv.medicalerta.common.navigation.NavigationEvent
import br.com.dcarv.medicalerta.common.ui.OptionalMessage
import br.com.dcarv.medicalerta.common.ui.SingleLiveEvent
import br.com.dcarv.medicalerta.common.ui.zipWith
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Date
import javax.inject.Inject

private const val TAG = "EditMedicationViewModel"

class EditMedicationViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository,
    private val medicationRepository: MedicationRepository
) : ViewModel() {

    val showProgressBar: LiveData<Boolean>
    private val _showProgressbar = MutableLiveData<Boolean>()

    val showError: LiveData<OptionalMessage>
    private val _errorVisible = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()

//    val medTypes: LiveData<List<MedicationType>>
//    private val _medTypes = MutableLiveData<List<MedicationType>>()

    val medicationName = MutableLiveData<String>()
    val medicationNextDose = MutableLiveData<Date>()

//    val medData: LiveData<Medication>
//    private val _medData = MutableLiveData<Medication>()

    val navigateEvents = SingleLiveEvent<NavigationEvent>()

    private val disposeBag = CompositeDisposable()

    init {
        _showProgressbar.value = true
        showProgressBar = Transformations.map(_showProgressbar) { it }

        _errorVisible.value = false
        _errorMessage.value = ""
        showError = _errorVisible.zipWith(_errorMessage, OptionalMessage.Companion::from)

//        _medTypes.value = MedicationType.values().toList()
//        medTypes = Transformations.map(_medTypes) { it }

//        medData = Transformations.map(_medData) { it }
    }

    override fun onCleared() {
        super.onCleared()
        disposeBag.clear()
    }

    fun init(medId: String?) {
        loadMedData(medId)
    }

    fun setMedicationName(name: String) {
        medicationName.postValue(name)
//        val data = medData.valueOrDefault().copy(name = name)
//        _medData.postValue(data)
    }

    fun setMediationNextDose(nextDose: Date) {
        medicationNextDose.postValue(nextDose)
//        val data = medData.valueOrDefault().copy(nextDose = nextDose)
//        _medData.postValue(data)
    }

    fun saveMedication() {
        // TODO: validate data
        val med = Medication(
            id = "",
            name = medicationName.value ?: "",
            nextDose = medicationNextDose.value ?: Date()
        )

        medicationRepository.saveMedication(med)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _showProgressbar.postValue(true) }
            .subscribe({
                navigateEvents.postValue(NavigationEvent.MedicationList)
                _showProgressbar.postValue(false)
            }, {
                // TODO: error msg
                _errorMessage.postValue(messagesRepository.get(Messages.Key.GENERIC_ERROR_MESSAGE))
                _errorVisible.postValue(true)
                _showProgressbar.postValue(false)
            })
            .disposeWith(disposeBag)
    }

    private fun loadMedData(medId: String?) {
        if (medId != null) {
            medicationRepository.getMedication(medId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    postMedicationData(it)
                    _showProgressbar.postValue(false)
                }, {
                    Log.e(TAG, "Failed load edit medication data", it)
                    _errorVisible.postValue(true)
                    _errorMessage.postValue(messagesRepository.get(Messages.Key.GENERIC_ERROR_MESSAGE))
                    _showProgressbar.postValue(false)
                })
                .disposeWith(disposeBag)
        } else {
            _showProgressbar.postValue(false)
        }
    }

    private fun postMedicationData(med: Medication) {
        medicationName.postValue(med.name)
        medicationNextDose.postValue(med.nextDose)
    }

    private fun LiveData<Medication>.valueOrDefault(): Medication {
        return value ?: Medication("", "", Date())
    }
}
