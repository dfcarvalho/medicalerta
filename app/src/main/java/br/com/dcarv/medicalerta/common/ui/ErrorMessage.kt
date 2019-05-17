package br.com.dcarv.medicalerta.common.ui

sealed class ErrorMessage {
    data class Displayed(val message: String): ErrorMessage()
    object Hidden : ErrorMessage()
}