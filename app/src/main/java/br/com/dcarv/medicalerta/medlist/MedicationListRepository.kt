package br.com.dcarv.medicalerta.medlist

import br.com.dcarv.medicalerta.common.network.asCompletable
import br.com.dcarv.medicalerta.common.network.asObservable
import br.com.dcarv.medicalerta.common.network.asSingleList
import br.com.dcarv.medicalerta.common.network.asSingleWithId
import br.com.dcarv.medicalerta.common.model.Medication
import br.com.dcarv.medicalerta.common.model.User
import br.com.dcarv.medicalerta.common.network.medication
import br.com.dcarv.medicalerta.common.network.medications
import br.com.dcarv.medicalerta.common.network.model.FirebaseMedication
import br.com.dcarv.medicalerta.common.network.model.toModel
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
        firebaseDb.medications(user.id)
            .get()
            .asSingleList<FirebaseMedication>()
            .map { it.map(FirebaseMedication::toModel) }


    fun observeMedsList(user: User): Observable<Medication> =
        // TODO: pagination?
        firebaseDb.medications(user.id)
            .get()
            .asObservable<FirebaseMedication>()
            .map(FirebaseMedication::toModel)

    fun addMed(user: User, med: Medication): Completable =
        firebaseDb.medications(user.id)
            .add(med)
            .asSingleWithId()
            .map { med.copy(id = it) }
            .flatMapCompletable { updateMed(user, med) }

    fun updateMed(user: User, med: Medication): Completable =
        firebaseDb.medication(user.id, med.id)
            .set(med)
            .asCompletable()
}