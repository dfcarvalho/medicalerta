package br.com.dcarv.medicalerta

import android.app.Application
import br.com.dcarv.medicalerta.common.di.ApplicationComponent
import br.com.dcarv.medicalerta.common.di.DaggerApplicationComponent
import br.com.dcarv.medicalerta.common.di.DaggerComponentProvider

class MedicalertaApplication: Application(), DaggerComponentProvider {

    override val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationContext(this)
            .build()
    }
}