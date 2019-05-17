package br.com.dcarv.medicalerta.common.ui

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import br.com.dcarv.medicalerta.R

var View.gone: Boolean
    get() = visibility == View.GONE
    set(value) { visibility = if (value) View.GONE else View.VISIBLE }

val Fragment.progressBar: View?
    get() = view?.findViewById(R.id.progressBar)

var Fragment.showProgressBar: Boolean
    get() = progressBar?.gone != true
    set(value) { progressBar?.gone = !value }

val Fragment.errorView: View?
    get() = view?.findViewById(R.id.errorViewContainer)

var Fragment.showError: Boolean
    get() = errorView?.gone != true
    set(value) { errorView?.gone = !value }

var Fragment.errorMessage: String
    get() = errorView?.findViewById<TextView>(R.id.errorViewMessage)?.text?.toString() ?: ""
    set(value) { errorView?.findViewById<TextView>(R.id.errorViewMessage)?.text = value }

fun Fragment.showError(show: Boolean, message: String = "") {
    errorMessage = message
    showError = show
}

fun <T, U, V> LiveData<T>.zipWith(secondLiveData: LiveData<U>, zipper: (T, U) -> V): LiveData<V> {
    return MediatorLiveData<V>().apply {
        var firstDataUpdated = true
        var secondDataUpdated = true

        var lastData1: T? = null
        var lastData2: U? = null

        fun updateValueIfNeeded() {
            if (firstDataUpdated && secondDataUpdated && lastData1 != null && lastData2 != null) {
                value = zipper(lastData1!!, lastData2!!)
            }
        }

        addSource(this@zipWith) {
            lastData1 = it
            firstDataUpdated = true
            updateValueIfNeeded()
        }

        addSource(secondLiveData) {
            lastData2 = it
            secondDataUpdated = true
            updateValueIfNeeded()
        }
    }
}