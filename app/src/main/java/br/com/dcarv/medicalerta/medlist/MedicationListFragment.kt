package br.com.dcarv.medicalerta.medlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.dcarv.medicalerta.R
import br.com.dcarv.medicalerta.common.di.injector
import br.com.dcarv.medicalerta.common.navigation.NavigationEvent
import br.com.dcarv.medicalerta.common.ui.OptionalMessage
import br.com.dcarv.medicalerta.common.ui.gone
import br.com.dcarv.medicalerta.common.ui.hideError
import br.com.dcarv.medicalerta.common.ui.lazyViewModel
import br.com.dcarv.medicalerta.common.ui.showError
import br.com.dcarv.medicalerta.common.ui.showProgressBar
import kotlinx.android.synthetic.main.fragment_medication_list.*

private const val TAG = "MedicationListFragment"

class MedicationListFragment : Fragment() {

    private val viewModel: MedicationListViewModel by lazyViewModel { injector.medicationListViewModel() }

    private val adapter: MedicationListAdapter by lazy { MedicationListAdapter(viewModel) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medication_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViews()

        observeViewModel()

        viewModel.onActivityCreated(this)
    }

    override fun onDetach() {
        super.onDetach()
        adapter.destroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (viewModel.onAuthenticationResult(requestCode, resultCode, data)) {
            return
        }

        // TODO: handle other requests
    }

    private fun setUpMedsList() {
        medListRecyclerView.adapter = adapter
        medListRecyclerView.layoutManager = LinearLayoutManager(
            context, RecyclerView.VERTICAL, false
        )
    }

    private fun observeViewModel() {
        viewModel.showProgressBar.observe(viewLifecycleOwner, Observer {
            showProgressBar = it
        })

        viewModel.showError.observe(viewLifecycleOwner, Observer { errorMsg ->
            when (errorMsg) {
                is OptionalMessage.Displayed -> {
                    showError(errorMsg.message)
                }
                is OptionalMessage.Hidden -> {
                    hideError()
                }
            }
        })

        viewModel.medicationList.observe(viewLifecycleOwner, Observer { medsList ->
            adapter.updateList(medsList)

            handleListVisibility(medsList.isEmpty())
        })

        viewModel.navigateEvents.observe(viewLifecycleOwner, Observer { event ->
            when (event) {
                is NavigationEvent.MedicationDetails -> {
                    val action = MedicationListFragmentDirections.actionMedicationListFragmentToMedicationDetailsFragment(event.medId)
                    findNavController().navigate(action)
                }
            }

        })
    }

    private fun setUpViews() {
        setUpMedsList()
    }

    private fun handleListVisibility(isEmpty: Boolean) {
        medListRecyclerView.gone = isEmpty
        medListEmptyMessage.gone = !isEmpty
    }
}