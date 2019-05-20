package br.com.dcarv.medicalerta.medlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.dcarv.medicalerta.R
import br.com.dcarv.medicalerta.common.emoji
import br.com.dcarv.medicalerta.common.format
import br.com.dcarv.medicalerta.common.model.Medication
import br.com.dcarv.medicalerta.common.ui.gone
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_med_list.view.*

private const val TAG = "MedicationListAdapter"

class MedicationListAdapter(
    private val viewModel: MedicationListViewModel
) : RecyclerView.Adapter<MedicationListAdapter.ViewHolder>() {

    private var medsList: List<Medication> = emptyList()

    private var diffDisposable: Disposable? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_med_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = medsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val med = medsList[position]

        with(holder.itemView) {
            medListItemName.text = med.name
            if (med.nextDose.time != 0L) {
                val timeEmoji = med.nextDose.emoji
                medListItemNextDose.text = context.getString(
                    R.string.meds_list_item_dose_format,
                    timeEmoji,
                    med.nextDose.format()
                )
                medListItemNextDose.gone = false
            } else {
                medListItemNextDose.gone = true
            }
            setOnClickListener { viewModel.onMedicationClicked(med) }
        }
    }

    fun updateList(newMedsList: List<Medication>, changedOrder: Boolean = false) {
        diffDisposable?.run { dispose() }

        diffDisposable =
            Single.fromCallable {
                DiffUtil.calculateDiff(
                    Diff(medsList, newMedsList),
                    !changedOrder
                )
            }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    medsList = newMedsList
                    result.dispatchUpdatesTo(this)
                }, {
                    Log.e(TAG, "Failed to calculate diff", it)
                })

    }

    fun destroy() {
        diffDisposable?.dispose()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class Diff(
        private val oldMedsList: List<Medication>,
        private val newMedsList: List<Medication>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldMedsList[oldItemPosition].id == newMedsList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldMedsList[oldItemPosition] == newMedsList[newItemPosition]
        }

        override fun getOldListSize(): Int = oldMedsList.size

        override fun getNewListSize(): Int = newMedsList.size
    }
}