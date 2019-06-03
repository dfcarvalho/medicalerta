package br.com.dcarv.medicalerta.medlist

import br.com.dcarv.medicalerta.common.authentication.AuthenticationManager
import br.com.dcarv.medicalerta.common.model.Medication
import br.com.dcarv.medicalerta.common.network.asCompletable
import br.com.dcarv.medicalerta.common.network.asObservable
import br.com.dcarv.medicalerta.common.network.asSingleList
import br.com.dcarv.medicalerta.common.network.asSingleWithId
import br.com.dcarv.medicalerta.common.network.medication
import br.com.dcarv.medicalerta.common.network.medications
import br.com.dcarv.medicalerta.common.network.model.FirebaseMedication
import br.com.dcarv.medicalerta.common.network.model.toModel
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MedicationListRepository @Inject constructor(
    private val authenticationManager: AuthenticationManager,
    private val firebaseDb: FirebaseFirestore
) {

    fun getMedsList(): Single<List<Medication>> =
        authenticationManager.getUserId()
            .flatMap { userId ->
                firebaseDb.medications(userId)
                    .get()
                    .asSingleList<FirebaseMedication>()
            }
            .map { it.map(FirebaseMedication::toModel) }


    fun observeMedsList(): Observable<Medication> =
        // TODO: pagination?
        authenticationManager.getUserId()
            .flatMapObservable { userId ->
                firebaseDb.medications(userId)
                    .get()
                    .asObservable<FirebaseMedication>()
            }
            .map(FirebaseMedication::toModel)

    fun addMed(med: Medication): Completable =
        authenticationManager.getUserId()
            .flatMap { userId ->
                firebaseDb.medications(userId)
                    .add(med)
                    .asSingleWithId()
            }
            .map { med.copy(id = it) }
            .flatMapCompletable { updateMed(med) }

    fun updateMed(med: Medication): Completable =
        authenticationManager.getUserId()
            .flatMapCompletable { userId ->
                firebaseDb.medication(userId, med.id)
                    .set(med)
                    .asCompletable()
            }
}