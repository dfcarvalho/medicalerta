package br.com.dcarv.medicalerta.common.messages

import br.com.dcarv.medicalerta.R

interface Messages {

    enum class Key(val resId: Int) {
        GENERIC_ERROR_MESSAGE(R.string.generic_error_message),

        AUTHENTICATION_ERROR_MESSAGE(R.string.authentication_error_message),

        MEDICATION_LIST_EMPTY_MESSAGE(R.string.meds_list_empty_message),
        MEDICATION_LIST_ERROR_MESSAGE(R.string.meds_list_error_message)
    }

    interface Repository {

        fun get(key: Key): String
    }
}