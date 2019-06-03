package br.com.dcarv.medicalerta.common.network

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

fun <T> Task<T>.asCompletable(): Completable {
    return Completable.create { emitter ->
        this
            .addOnSuccessListener { emitter.onComplete() }
            .addOnFailureListener { emitter.onError(it) }
            // TODO: how should cancellation be handled here?
            .addOnCanceledListener { emitter.onError(Exception("Operation cancelled")) }
    }
}

fun Task<DocumentReference>.asSingleWithId(): Single<String> {
    return Single.create { emitter ->
        this
            .addOnSuccessListener { snapshot ->
                emitter.onSuccess(snapshot.id)
            }
            .addOnFailureListener { emitter.onError(it) }
            // TODO: how should cancellation be handled here?
            .addOnCanceledListener { emitter.onError(Exception("Operation cancelled")) }

    }
}

inline fun <reified T> Task<DocumentSnapshot>.asSingle(): Single<T> {
    return Single.create { emitter ->
        this
            .addOnSuccessListener { snapshot ->
                snapshot.toObject<T>()?.let {
                    emitter.onSuccess(it)
                } ?: emitter.onError(Exception("Failed to convert user result to model"))

            }
            .addOnFailureListener { emitter.onError(it) }
            // TODO: how should cancellation be handled here?
            .addOnCanceledListener { emitter.onError(Exception("Operation cancelled")) }

    }
}

inline fun <reified T: Any> Task<QuerySnapshot>.asSingleList(): Single<List<T>> {
    return Single.create { emitter ->
        this
            .addOnSuccessListener { querySnapshot ->
                emitter.onSuccess(querySnapshot.toObjects())
            }
            .addOnFailureListener { emitter.onError(it) }
            .addOnCanceledListener { emitter.onError(Exception("Operation cancelled")) }
    }
}

inline fun <reified T: Any> Task<QuerySnapshot>.asObservable(): Observable<T> {
    return Observable.create { emitter ->
        this
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.forEach { snapshot ->
                    try {
                        val doc = snapshot.toObject<T>()
                        emitter.onNext(doc)
                    } catch (e: Exception) {
                        // TODO: which type exception to catch?
                        // TODO: ignore element?
                    }
                }
            }
            .addOnFailureListener { emitter.onError(it) }
            .addOnCanceledListener { emitter.onError(Exception("Operation cancelled")) }
    }
}