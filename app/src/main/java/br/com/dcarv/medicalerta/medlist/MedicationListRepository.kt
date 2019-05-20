package br.com.dcarv.medicalerta.medlist

import br.com.dcarv.medicalerta.common.asCompletable
import br.com.dcarv.medicalerta.common.asObservable
import br.com.dcarv.medicalerta.common.asSingleList
import br.com.dcarv.medicalerta.common.asSingleWithId
import br.com.dcarv.medicalerta.common.model.Medication
import br.com.dcarv.medicalerta.common.model.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MedicationListRepository @Inject constructor(
    private val firebaseDb: FirebaseFirestore
) {

    fun getMedsList(user: User): Single<List<Medication>> =
        medsCollection(user.id)
            .get()
            .asSingleList()

    fun observeMedsList(user: User): Observable<Medication> =
        // TODO: pagination?
        firebaseDb.collection("users")
            .document(user.id)
            .collection("meds")
            .get()
            .asObservable()

    fun addMed(user: User, med: Medication): Completable =
        medsCollection(user.id)
            .add(med)
            .asSingleWithId()
            .map { med.copy(id = it) }
            .flatMapCompletable { updateMed(user, med) }

    fun updateMed(user: User, med: Medication): Completable =
        medsCollection(user.id)
            .document(med.id)
            .set(med)
            .asCompletable()

    private fun medsCollection(userId: String): CollectionReference =
        firebaseDb.collection("users")
            .document(userId)
            .collection("meds")
}