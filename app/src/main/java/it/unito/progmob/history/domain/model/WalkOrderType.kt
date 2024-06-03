package it.unito.progmob.history.domain.model

sealed class WalkOrderType {
    data object Ascending : WalkOrderType()
    data object Descending : WalkOrderType()
}