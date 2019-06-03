package br.com.dcarv.medicalerta

import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.lang.Exception

fun <T> mockSuccessfulTask(result: T): Task<T> {
    return mockk {
        val successSlot = slot<OnSuccessListener<T>>()

        every { addOnSuccessListener(capture(successSlot)) } answers {
            successSlot.captured.onSuccess(result)
            this@mockk
        }
        every { addOnFailureListener(any()) } returns this
        every { addOnCanceledListener(any()) } returns this
    }
}

fun mockSuccessfulVoidTask(): Task<Void> {
    return mockk {
        val successSlot = slot<OnSuccessListener<Void>>()

        every { addOnSuccessListener(capture(successSlot)) } answers {
            successSlot.captured.onSuccess(null)
            this@mockk
        }
        every { addOnFailureListener(any()) } returns this
        every { addOnCanceledListener(any()) } returns this

    }
}

fun <T> mockFailedTask(error: Exception): Task<T> {
    return mockk {
        val failureSlot = slot<OnFailureListener>()

        every { addOnFailureListener(capture(failureSlot)) } answers {
            failureSlot.captured.onFailure(error)
            this@mockk
        }
        every { addOnSuccessListener(any()) } returns this
        every { addOnCanceledListener(any()) } returns this

    }
}

fun <T> mockCanceledTask(): Task<T> {
    return mockk {
        val canceledSlot= slot<OnCanceledListener>()

        every { addOnCanceledListener(capture(canceledSlot)) } answers {
            canceledSlot.captured.onCanceled()
            this@mockk
        }
        every { addOnSuccessListener(any()) } returns this
        every { addOnFailureListener(any()) } returns this
    }
}

inline fun <reified T> DocumentSnapshot.mockSerialization(result: T?): DocumentSnapshot {
    every { toObject(T::class.java) } returns result
    return this
}