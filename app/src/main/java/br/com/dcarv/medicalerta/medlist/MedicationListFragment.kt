package br.com.dcarv.medicalerta.medlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.dcarv.medicalerta.R
import br.com.dcarv.medicalerta.common.AuthenticationManager
import br.com.dcarv.medicalerta.common.autoDispose
import br.com.dcarv.medicalerta.common.showProgressBar
import com.firebase.ui.auth.AuthUI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

private const val TAG = "MedicationListFragment"

class MedicationListFragment: Fragment() {

    private val authManager = AuthenticationManager()

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medication_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressBar = true

        authManager.authenticateIfNecessary(this)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "Auth successful: $it")
                showProgressBar = false
            }, {
                Log.e(TAG, "Auth failed", it)
                // TODO: show error
                showProgressBar = false
            })
            .autoDispose(disposables)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (authManager.onAuthenticationActivityResult(requestCode, resultCode, data)) {
            return
        }

        // TODO: handle other requests
    }
}