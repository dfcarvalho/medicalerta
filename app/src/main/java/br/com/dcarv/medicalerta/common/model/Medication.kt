package br.com.dcarv.medicalerta.common.model

import java.util.Date

data class Medication(
    val id: String,
    val name: String,
    val nextDose: Date
)