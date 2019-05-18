package br.com.dcarv.medicalerta.common.authentication

import br.com.dcarv.medicalerta.common.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseDb: FirebaseFirestore
) {

    fun saveUser(user: User): Completable {
        return Completable.create { emitter ->
            firebaseDb.collection("users")
                .document(user.id)
                .set(user)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
                // TODO: how should cancellation be handled here?
                .addOnCanceledListener { emitter.onError(Exception("Operation cancelled")) }
        }
    }

    fun getUser(id: String): Single<User> {
        return Single.create { emitter ->
            firebaseDb.collection("users")
                .document(id)
                .get()
                .addOnSuccessListener { snapshot ->
                    snapshot.toObject<User>()?.let {
                        emitter.onSuccess(it)
                    } ?: emitter.onError(Exception("Failed to convert user result to model"))

                }
                .addOnFailureListener { emitter.onError(it) }
                // TODO: how should cancellation be handled here?
                .addOnCanceledListener { emitter.onError(Exception("Operation cancelled")) }

        }

    }
}