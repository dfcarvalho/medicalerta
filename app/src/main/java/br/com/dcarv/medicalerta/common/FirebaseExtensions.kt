package br.com.dcarv.medicalerta.common

import br.com.dcarv.medicalerta.common.model.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toModel() = User(
    id = uid,
    username = this.email ?: "",
    name = this.displayName ?: ""
)