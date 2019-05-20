package br.com.dcarv.medicalerta.common.ui

sealed class OptionalMessage {

    data class Displayed(val message: String) : OptionalMessage()
    object Hidden : OptionalMessage()

    companion object {
        fun from(visible: Boolean, msg: String): OptionalMessage {
            return if (visible) {
                Displayed(msg)
            } else {
                Hidden
            }
        }
    }
}