package br.com.dcarv.medicalerta.editmed

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.dcarv.medicalerta.common.di.injector
import br.com.dcarv.medicalerta.common.ui.lazyParentFragmentViewModel
import io.reactivex.Maybe
import io.reactivex.subjects.BehaviorSubject
import java.util.Calendar
import java.util.Date

class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val dateSubject = BehaviorSubject.create<Date>()

    private val viewModel: EditMedicationViewModel by lazyParentFragmentViewModel {
        injector.editMedicationViewModel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()

        viewModel.medicationNextDose.value?.let { calendar.time = it }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity!!, this, year, month, day)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        dateSubject.onComplete()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        dateSubject.onNext(calendar.time)
    }

    fun show(fragmentManager: FragmentManager): Maybe<Date> {
        return dateSubject.firstElement()
            .doOnSubscribe { show(fragmentManager, "DatePickerDialogFragment") }
    }
}