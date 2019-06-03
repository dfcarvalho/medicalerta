package br.com.dcarv.medicalerta.editmed

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import br.com.dcarv.medicalerta.common.di.injector
import br.com.dcarv.medicalerta.common.ui.lazyParentFragmentViewModel
import io.reactivex.Maybe
import io.reactivex.subjects.BehaviorSubject
import java.util.Calendar
import java.util.Date

class TimePickerDialogFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val timeSubject = BehaviorSubject.create<Date>()

    private val viewModel: EditMedicationViewModel by lazyParentFragmentViewModel {
        injector.editMedicationViewModel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()

        viewModel.medicationNextDose.value?.let { calendar.time = it }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            activity!!,
            this,
            hour,
            minute,
            DateFormat.is24HourFormat(activity!!)
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        timeSubject.onComplete()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        timeSubject.onNext(calendar.time)
    }

    fun show(fragmentManager: FragmentManager): Maybe<Date> {
        return timeSubject.firstElement()
            .doOnSubscribe { show(fragmentManager, "DatePickerDialogFragment") }
    }

}