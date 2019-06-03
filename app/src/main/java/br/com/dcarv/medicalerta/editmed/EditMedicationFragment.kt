package br.com.dcarv.medicalerta.editmed

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.dcarv.medicalerta.R
import br.com.dcarv.medicalerta.common.disposeWith
import br.com.dcarv.medicalerta.common.di.injector
import br.com.dcarv.medicalerta.common.format
import br.com.dcarv.medicalerta.common.navigation.NavigationEvent
import br.com.dcarv.medicalerta.common.setDateOnly
import br.com.dcarv.medicalerta.common.setTimeOnly
import br.com.dcarv.medicalerta.common.ui.OptionalMessage
import br.com.dcarv.medicalerta.common.ui.hideError
import br.com.dcarv.medicalerta.common.ui.lazyViewModel
import br.com.dcarv.medicalerta.common.ui.setTextIfNeeded
import br.com.dcarv.medicalerta.common.ui.showError
import br.com.dcarv.medicalerta.common.ui.showProgressBar
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_edit_medication.*
import java.util.Date

private const val TAG = "EditMedicationFragment"

class EditMedicationFragment : Fragment() {

    private val viewModel: EditMedicationViewModel by lazyViewModel { injector.editMedicationViewModel() }

    private val args: EditMedicationFragmentArgs by navArgs()

    private val disposeBag = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_medication, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViews()

        observeViewModel()

        viewModel.init(args.medId)
    }

    private fun setUpViews() {
        editMedNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel.setMedicationName(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editMedNextDoseEditText.setOnClickListener {
            Single.just(Date())
                .flatMap { date ->
                    DatePickerDialogFragment().show(childFragmentManager).toSingle()
                        .doOnSuccess { date.setDateOnly(it) }
                        .map { date }

                }
                .flatMap { date ->
                    TimePickerDialogFragment().show(childFragmentManager).toSingle()
                        .doOnSuccess { date.setTimeOnly(it) }
                        .map { date }
                }
                .doOnSuccess { viewModel.setMediationNextDose(it) }
                .map { it.format() }
                .subscribe({
                    editMedNextDoseEditText.setText(it)
                }, {
                    Log.e(TAG, "Failed to get time and date from picker", it)
                    editMedNextDoseEditText.setText("")
                })
                .disposeWith(disposeBag)
        }

        editMedSaveButton.setOnClickListener { viewModel.saveMedication() }
    }

    private fun observeViewModel() {
        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            showProgressBar = it
        })

        viewModel.showError.observe(viewLifecycleOwner, Observer { errorMsg ->
            when (errorMsg) {
                is OptionalMessage.Displayed -> showError(errorMsg.message)
                is OptionalMessage.Hidden -> hideError()
            }
        })

        viewModel.medicationName.observe(viewLifecycleOwner, Observer {
            editMedNameEditText.setTextIfNeeded(it)
        })

        viewModel.medicationNextDose.observe(viewLifecycleOwner, Observer {
            editMedNextDoseEditText.setTextIfNeeded(it.format())
        })

//        viewModel.medData.observe(viewLifecycleOwner, Observer { med ->
//            editMedNameEditText.setTextIfNeeded(med.name)
//            editMedNextDoseEditText.setTextIfNeeded(med.nextDose.format())
//        })

        viewModel.navigateEvents.observe(viewLifecycleOwner, Observer { event ->
            when (event) {
                is NavigationEvent.MedicationList -> navigateBackToMedicationList()
            }
        })
    }

    private fun navigateBackToMedicationList() {
        findNavController().popBackStack()
    }
}
