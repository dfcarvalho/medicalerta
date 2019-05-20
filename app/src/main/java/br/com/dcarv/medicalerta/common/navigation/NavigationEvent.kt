package br.com.dcarv.medicalerta.common.navigation

sealed class NavigationEvent {
    class MedicationDetails(val medId: String): NavigationEvent()
}