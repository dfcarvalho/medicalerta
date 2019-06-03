package br.com.dcarv.medicalerta.editmed

import br.com.dcarv.medicalerta.common.authentication.AuthenticationManager
import br.com.dcarv.medicalerta.common.model.Medication
import br.com.dcarv.medicalerta.common.network.asSingle
import br.com.dcarv.medicalerta.common.network.medication
import br.com.dcarv.medicalerta.common.network.model.FirebaseMedication
import br.com.dcarv.medicalerta.mockSerialization
import br.com.dcarv.medicalerta.mockSuccessfulTask
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import java.util.Date

private const val USER_ID = "USER_ID"
private const val MED_ID = "MED_ID"

class MedicationRepositoryTest {

    @MockK
    lateinit var authenticationManager: AuthenticationManager

    @MockK
    lateinit var firebaseDb: FirebaseFirestore

    @InjectMockKs
    lateinit var medicationRepository: MedicationRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test_getMedication_success() {
        // given
        val date = Date()
        val med = Medication(MED_ID, "NAME", date)
        val firebaseMed = FirebaseMedication(MED_ID, "NAME", date)
        val medSnapshot = mockk<DocumentSnapshot>().mockSerialization(firebaseMed)
        every { authenticationManager.getUserId() } returns Single.just(USER_ID)
        every { firebaseDb.medication(USER_ID, MED_ID).get() } returns mockSuccessfulTask(medSnapshot)

        // when
        val testObserver = medicationRepository.getMedication(MED_ID).test()

        // then
        verify { firebaseDb.medication(USER_ID, MED_ID).get().asSingle<FirebaseMedication>() }
        testObserver.assertNoErrors()
        testObserver.assertValue(med)
    }
}