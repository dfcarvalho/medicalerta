package br.com.dcarv.medicalerta.common.model

import java.util.*

data class Medication(
    val id: String = "",
    val name: String = "",
    val nextDose: Date = Date(0)
)