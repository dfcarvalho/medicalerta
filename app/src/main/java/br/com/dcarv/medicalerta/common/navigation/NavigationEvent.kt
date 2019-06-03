package br.com.dcarv.medicalerta.common.navigation

sealed class NavigationEvent {
    object MedicationList: NavigationEvent()
    class MedicationDetails(val medId: String): NavigationEvent()
    object NewMedication: NavigationEvent()
    class EditMedication(val medId: String? = null): NavigationEvent()
}