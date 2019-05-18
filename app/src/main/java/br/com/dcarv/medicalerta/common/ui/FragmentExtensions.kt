package br.com.dcarv.medicalerta.common.ui

import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.dcarv.medicalerta.R

val Fragment.progressBar: View?
    get() = view?.findViewById(R.id.progressBar)

var Fragment.showProgressBar: Boolean
    get() = progressBar?.gone != true
    set(value) { progressBar?.gone = !value }

private val Fragment.errorView: View?
    get() = view?.findViewById(R.id.errorViewContainer)

var Fragment.showError: Boolean
    get() = errorView?.gone != true
    set(value) { errorView?.gone = !value }

var Fragment.errorMessage: String
    get() = errorView?.findViewById<TextView>(R.id.errorViewMessage)?.text?.toString() ?: ""
    set(value) { errorView?.findViewById<TextView>(R.id.errorViewMessage)?.text = value }

fun Fragment.showError(message: String) {
    errorMessage = message
    showError = true
}

fun Fragment.hideError() {
    showError = false
    errorMessage = ""
}

inline fun <reified T: ViewModel> Fragment.lazyViewModel(
    crossinline provider: () -> T
) = viewModels<T> {
    object: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T: ViewModel> create(modelClass: Class<T>) = provider() as T
    }
}