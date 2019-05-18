package br.com.dcarv.medicalerta.common.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

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