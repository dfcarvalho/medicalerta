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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val TAG = "MedicationListViewModel"

class MedicationListViewModel @Inject constructor(
    private val authManager: Authentication.Manager
): ViewModel() {

    val showProgressBar: LiveData<Boolean>
    private val _showProgressbar=  MutableLiveData<Boolean>()

    private val disposables = CompositeDisposable()

    init {
        _showProgressbar.postValue(false)
        showProgressBar = Transformations.map(_showProgressbar) { it }
    }

    fun onActivityCreated(fragment: Fragment) {
        _showProgressbar.postValue(true)

        authManager.authenticateIfNecessary(fragment)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "Auth successful: $it")
                _showProgressbar.postValue(false)
            }, {
                Log.e(TAG, "Auth failed", it)
                // TODO: show error
                _showProgressbar.postValue(false)
            })
            .autoDispose(disposables)
    }

    fun onAuthenticationResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return authManager.onAuthenticationActivityResult(requestCode, resultCode, data)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}