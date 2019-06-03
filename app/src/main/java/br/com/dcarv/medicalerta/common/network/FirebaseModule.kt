package br.com.dcarv.medicalerta.common.network

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseAuthUI(): AuthUI = AuthUI.getInstance()

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore
}