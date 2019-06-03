package br.com.dcarv.medicalerta.common.network.model

import br.com.dcarv.medicalerta.common.model.Medication

fun FirebaseMedication.toModel(): Medication =
    Medication(
        id,
        name,
        nextDose
    )
