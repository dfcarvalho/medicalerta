package br.com.dcarv.medicalerta.common.messages

import android.content.Context
import javax.inject.Inject

class MessagesRepository @Inject constructor(
    context: Context
): Messages.Repository {

    private val appContext: Context = context.applicationContext

    override fun get(key: Messages.Key): String {
        return appContext.getString(key.resId)
    }
}