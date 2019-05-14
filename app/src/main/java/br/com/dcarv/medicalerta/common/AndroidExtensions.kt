package br.com.dcarv.medicalerta.common

import android.view.View
import androidx.fragment.app.Fragment
import br.com.dcarv.medicalerta.R

var View.gone: Boolean
    get() = visibility == View.GONE
    set(value) { visibility = if (value) View.GONE else View.VISIBLE }

val Fragment.progressBar: View?
    get() = view?.findViewById(R.id.progressBar)

var Fragment.showProgressBar: Boolean
    get() = progressBar?.gone != true
    set(value) { progressBar?.gone = !value }
