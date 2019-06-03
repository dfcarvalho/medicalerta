package br.com.dcarv.medicalerta.common.di

import android.content.Context
import br.com.dcarv.medicalerta.common.authentication.AuthenticationModule
import br.com.dcarv.medicalerta.common.messages.MessagesModule
import br.com.dcarv.medicalerta.common.network.FirebaseModule
import br.com.dcarv.medicalerta.editmed.EditMedicationViewModel
import br.com.dcarv.medicalerta.meddetails.MedicationDetailsViewModel
import br.com.dcarv.medicalerta.medlist.MedicationListViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AuthenticationModule::class, FirebaseModule::class, MessagesModule::class])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun applicationContext(applicationContext: Context): Builder

        fun build(): ApplicationComponent
    }

    fun medicationListViewModel(): MedicationListViewModel
    fun medicationDetailsViewModel(): MedicationDetailsViewModel
    fun editMedicationViewModel(): EditMedicationViewModel
}