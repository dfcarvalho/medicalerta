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
