package br.com.dcarv.medicalerta.common.di

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseAuthUI(): AuthUI = AuthUI.getInstance()
}