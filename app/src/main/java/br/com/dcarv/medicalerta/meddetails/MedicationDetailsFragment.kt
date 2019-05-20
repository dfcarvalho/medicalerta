package br.com.dcarv.medicalerta.meddetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.dcarv.medicalerta.R
import br.com.dcarv.medicalerta.common.di.injector
import br.com.dcarv.medicalerta.common.ui.lazyViewModel
import br.com.dcarv.medicalerta.common.ui.withBundle

private const val TAG = "MedDetailsFragment"
private const val ARG_MED_ID = "MED_ID"

class MedicationDetailsFragment : Fragment() {

    private val viewModel: MedicationDetailsViewModel by lazyViewModel { injector.medicationDetailsViewModel() }

    private val args: MedicationDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medication_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        Log.d(TAG, "MedId: ${args.medId}")
    }

}
