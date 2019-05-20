package br.com.dcarv.medicalerta.common.authentication

import br.com.dcarv.medicalerta.common.asCompletable
import br.com.dcarv.medicalerta.common.asSingle
import br.com.dcarv.medicalerta.common.model.User
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseDb: FirebaseFirestore
) {

    fun saveUser(user: User): Completable =
        firebaseDb.collection("users")
            .document(user.id)
            .set(user)
            .asCompletable()

    fun getUser(id: String): Single<User> =
        firebaseDb.collection("users")
            .document(id)
            .get()
            .asSingle()
}