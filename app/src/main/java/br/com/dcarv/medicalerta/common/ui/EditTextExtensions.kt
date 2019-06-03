package br.com.dcarv.medicalerta.common.ui

import android.widget.EditText

fun EditText.setTextIfNeeded(newText: String) {
    if (text.toString() != newText) {
        setText(newText)
    }
}