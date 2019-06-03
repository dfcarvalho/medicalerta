package br.com.dcarv.medicalerta.common.authentication

import br.com.dcarv.medicalerta.common.network.asCompletable
import br.com.dcarv.medicalerta.common.network.asSingle
import br.com.dcarv.medicalerta.common.model.User
import br.com.dcarv.medicalerta.common.network.user
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseDb: FirebaseFirestore
) {

    fun saveUser(user: User): Completable =
        firebaseDb.user(user.id)
            .set(user)
            .asCompletable()

    fun getUser(id: String): Single<User> =
        firebaseDb.user(id)
            .get()
            .asSingle()
}