package br.com.dcarv.medicalerta.common.network

import com.google.firebase.firestore.FirebaseFirestore

fun FirebaseFirestore.users() = collection(USERS_PATH)

fun FirebaseFirestore.user(userId: String) = users().document(userId)

fun FirebaseFirestore.medications(userId: String) =
    user(userId).collection(MEDS_PATH)

fun FirebaseFirestore.medication(userId: String, medId: String) =
    medications(userId).document(medId)