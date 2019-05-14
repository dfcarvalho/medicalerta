package br.com.dcarv.medicalerta.common

import android.app.Activity
import android.content.Intent
import br.com.dcarv.medicalerta.common.model.User
import io.reactivex.Single

interface Authentication {

    interface Manager {

        fun authenticateIfNecessary(activity: Activity): Single<User>

        fun onAuthenticationActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
    }
}