package br.com.dcarv.medicalerta.common.network

import br.com.dcarv.medicalerta.common.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

fun FirebaseUser.toModel() = User(
    id = uid,
    username = this.email ?: "",
    name = this.displayName ?: ""
)