package br.com.dcarv.medicalerta.common.network.model

import java.util.Date

data class FirebaseMedication(
    val id: String = "",
    val name: String = "",
    val nextDose: Date = Date(0)
//    val type: String = "pills"
)