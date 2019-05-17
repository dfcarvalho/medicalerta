package br.com.dcarv.medicalerta.common.messages

import br.com.dcarv.medicalerta.R

interface Messages {

    enum class Key(val resId: Int) {
        AUTHENTICATION_ERROR_MESSAGE(R.string.authentication_error_message)
    }

    interface Repository {

        fun get(key: Messages.Key): String
    }
}