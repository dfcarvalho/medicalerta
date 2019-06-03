package br.com.dcarv.medicalerta.common.authentication

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.dcarv.medicalerta.common.model.User
import io.reactivex.Single

interface Authentication {

    interface Manager {

        val user: User

        fun authenticateIfNecessary(activity: Activity): Single<User>

        fun authenticateIfNecessary(fragment: Fragment): Single<User>

        fun onAuthenticationActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
    }
}