package br.com.dcarv.medicalerta.common.authentication

import dagger.Binds
import dagger.Module

@Module
abstract class AuthenticationModule {

    @Binds
    abstract fun bindAuthenticationManager(
        authenticationManager: AuthenticationManager
    ): Authentication.Manager
}