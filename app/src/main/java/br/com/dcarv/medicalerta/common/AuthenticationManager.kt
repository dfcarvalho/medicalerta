package br.com.dcarv.medicalerta.common

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import br.com.dcarv.medicalerta.common.model.User
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.SingleEmitter

private const val TAG = "AuthenticationManager"
private const val RQ_FIREBASE_AUTH = 1

class AuthenticationManager : Authentication.Manager {

    private val firebaseAuth = FirebaseAuth.getInstance()

    private var pendingAuthentication: SingleEmitter<User>? = null

    override fun authenticateIfNecessary(activity: Activity): Single<User> {
        return firebaseAuth.currentUser?.let {
            Single.just(it.toModel())
        } ?: Single.create<User> { emitter ->
            pendingAuthentication = emitter

            val providers = listOf(AuthUI.IdpConfig.EmailBuilder().build())
            startActivityForResult(
                activity,
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RQ_FIREBASE_AUTH,
                null
            )
        }
    }

    fun authenticateIfNecessary(fragment: Fragment): Single<User> {
        return firebaseAuth.currentUser?.let {
            Single.just(it.toModel())
        } ?: Single.create<User> { emitter ->
            pendingAuthentication = emitter

            val providers = listOf(AuthUI.IdpConfig.EmailBuilder().build())
            fragment.startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RQ_FIREBASE_AUTH
            )
        }
    }

    override fun onAuthenticationActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ): Boolean {
        if (requestCode == RQ_FIREBASE_AUTH) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "User logged in: ${firebaseAuth.currentUser?.uid}")
                firebaseAuth.currentUser?.let {
                    pendingAuthentication?.onSuccess(it.toModel())
                    pendingAuthentication = null
                }
            } else {
                Log.e(
                    TAG,
                    "Failed to authenticate with error code: ${response?.error?.errorCode}",
                    response?.error
                )

                response?.error?.let {
                    pendingAuthentication?.onError(it)
                    pendingAuthentication = null
                }

            }

            return true
        }

        return false
    }
}