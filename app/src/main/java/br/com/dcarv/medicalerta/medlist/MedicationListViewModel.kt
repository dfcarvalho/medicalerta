package br.com.dcarv.medicalerta.medlist

import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import br.com.dcarv.medicalerta.common.authentication.Authentication
import br.com.dcarv.medicalerta.common.autoDispose
import br.com.dcarv.medicalerta.common.messages.Messages
import br.com.dcarv.medicalerta.common.model.Medication
import br.com.dcarv.medicalerta.common.navigation.NavigationEvent
import br.com.dcarv.medicalerta.common.ui.OptionalMessage
import br.com.dcarv.medicalerta.common.ui.SingleLiveEvent
import br.com.dcarv.medicalerta.common.ui.zipWith
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "MedicationListViewModel"

class MedicationListViewModel @Inject constructor(
    private val authManager: Authentication.Manager,
    private val messagesRepository: Messages.Repository,
    private val medicationListRepository: MedicationListRepository
) : ViewModel() {

    val showProgressBar: LiveData<Boolean>
    private val _showProgressbar = MutableLiveData<Boolean>()

    val showError: LiveData<OptionalMessage>
    private val _errorVisible = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String>()

    val medicationList: LiveData<List<Medication>>
    private val _medicationList = MutableLiveData<List<Medication>>()

    val navigateEvents = SingleLiveEvent<NavigationEvent>()

    private var authenticationChecked: Boolean = false

    private val disposables = CompositeDisposable()

    init {
        _showProgressbar.value = false
        showProgressBar = Transformations.map(_showProgressbar) { it }

        _errorVisible.value = false
        _errorMessage.value = ""
        showError = _errorVisible.zipWith(_errorMessage, OptionalMessage.Companion::from)

        _medicationList.value = emptyList()
        // TODO: handle order
        medicationList = Transformations.map(_medicationList) { it }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun onActivityCreated(fragment: Fragment) {
        if (!authenticationChecked) {
            _showProgressbar.postValue(true)

            authManager.authenticateIfNecessary(fragment)
                .doOnSuccess {
                    Log.d(TAG, "Auth successful: $it")
                    authenticationChecked = true
                }
                .doOnError {
                    Log.e(TAG, "Auth failed", it)
                    _errorVisible.postValue(true)
                    _errorMessage.postValue(messagesRepository.get(Messages.Key.AUTHENTICATION_ERROR_MESSAGE))
                }
                .flatMap { medicationListRepository.getMedsList(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // TODO: display medication
                    _medicationList.postValue(it)
                    _showProgressbar.postValue(false)
                }, {
                    _errorVisible.postValue(true)
                    _errorMessage.postValue(messagesRepository.get(Messages.Key.MEDICATION_LIST_ERROR_MESSAGE))
                    _showProgressbar.postValue(false)
                })
                .autoDispose(disposables)
        }
    }

    fun onAuthenticationResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return authManager.onAuthenticationActivityResult(requestCode, resultCode, data)
    }

    fun onMedicationClicked(med: Medication) {
        Log.d(TAG, "Medication clicked: $med")
        navigateEvents.postValue(NavigationEvent.MedicationDetails(med.id))
    }
}