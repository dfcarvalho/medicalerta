package br.com.dcarv.medicalerta.editmed

import androidx.lifecycle.ViewModel
import br.com.dcarv.medicalerta.common.authentication.AuthenticationManager
import br.com.dcarv.medicalerta.common.model.Medication
import br.com.dcarv.medicalerta.common.network.asCompletable
import br.com.dcarv.medicalerta.common.network.asSingle
import br.com.dcarv.medicalerta.common.network.medication
import br.com.dcarv.medicalerta.common.network.medications
import br.com.dcarv.medicalerta.common.network.model.FirebaseMedication
import br.com.dcarv.medicalerta.common.network.model.toModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MedicationRepository @Inject constructor(
    private val authenticationManager: AuthenticationManager,
    private val firebaseDb: FirebaseFirestore
) : ViewModel() {

    fun getMedication(medId: String): Single<Medication> =
        authenticationManager.getUserId()
            .flatMap { userId ->
                firebaseDb.medication(userId, medId)
                    .get()
                    .asSingle<FirebaseMedication>()
            }
            .map(FirebaseMedication::toModel)

    fun saveMedication(medication: Medication): Completable =
        authenticationManager.getUserId()
            .flatMapCompletable { userId ->
                firebaseDb.medications(userId)
                    .add(medication)
                    .onSuccessTask {
                        val med = medication.copy(id = it?.id ?: medication.id)
                        firebaseDb.medication(userId, med.id)
                            .set(med)
                    }
                    .asCompletable()
            }

}