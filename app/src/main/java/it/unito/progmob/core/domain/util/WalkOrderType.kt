package it.unito.progmob.core.domain.util

sealed class WalkOrderType {
    data object Ascending : WalkOrderType()
    data object Descending : WalkOrderType()
}