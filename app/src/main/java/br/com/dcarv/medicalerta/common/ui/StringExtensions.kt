package br.com.dcarv.medicalerta.common.ui

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDate(locale: Locale = Locale.getDefault()): Date? {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", locale)
    return try {
        formatter.parse(this)
    } catch (e: ParseException) {
        null
    }
}