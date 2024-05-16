package it.unito.progmob.core.presentation

sealed class MainEvent {
    data object OnClickHome : MainEvent()
    data object OnClickHistory : MainEvent()
    data object OnClickMap : MainEvent()
    data object OnClickPlay : MainEvent()
}