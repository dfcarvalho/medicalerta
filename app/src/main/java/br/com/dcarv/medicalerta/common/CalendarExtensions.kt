package br.com.dcarv.medicalerta.common

import java.util.Calendar
import java.util.Date

fun Calendar.setDate(date: Date) {
    val dateCalendar = Calendar.getInstance()
    dateCalendar.time = date
    val year = dateCalendar.get(Calendar.YEAR)
    val month = dateCalendar.get(Calendar.MONTH)
    val day = dateCalendar.get(Calendar.DAY_OF_MONTH)

    set(Calendar.YEAR, year)
    set(Calendar.MONTH, month)
    set(Calendar.DAY_OF_MONTH, day)
}

fun Calendar.setTime(date: Date) {
    val dateCalendar = Calendar.getInstance()
    dateCalendar.time = date
    val hour = dateCalendar.get(Calendar.HOUR_OF_DAY)
    val minute = dateCalendar.get(Calendar.MINUTE)

    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minute)
}