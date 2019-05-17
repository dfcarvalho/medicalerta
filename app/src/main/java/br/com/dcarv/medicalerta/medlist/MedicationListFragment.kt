package br.com.dcarv.medicalerta.medlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import br.com.dcarv.medicalerta.R
import br.com.dcarv.medicalerta.common.di.injector
import br.com.dcarv.medicalerta.common.ui.ErrorMessage
import br.com.dcarv.medicalerta.common.ui.errorView
import br.com.dcarv.medicalerta.common.ui.showError
import br.com.dcarv.medicalerta.common.ui.showProgressBar

private const val TAG = "MedicationListFragment"

class MedicationListFragment: Fragment() {

    private val viewModel: MedicationListViewModel by lazy {
        ViewModelProviders.of(this, injector.medicationListViewModelFactory())
            .get(MedicationListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medication_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeViewModel()

        viewModel.onActivityCreated(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (viewModel.onAuthenticationResult(requestCode, resultCode, data)) {
            return
        }

        // TODO: handle other requests
    }

    private fun observeViewModel() {
        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            showProgressBar = it
        })

        viewModel.showError.observe(viewLifecycleOwner, Observer { errorMsg ->
            when (errorMsg) {
                is ErrorMessage.Displayed -> {
                    showError(true, errorMsg.message)
                }
                is ErrorMessage.Hidden -> {
                    showError(false)
                }
            }
        })
    }
}