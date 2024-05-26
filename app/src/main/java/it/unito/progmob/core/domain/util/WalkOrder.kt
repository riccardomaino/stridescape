package it.unito.progmob.core.domain.util

sealed class WalkOrder(val orderType: WalkOrderType) {
    class Steps(orderType: WalkOrderType): WalkOrder(orderType)
    class Distance(orderType: WalkOrderType): WalkOrder(orderType)
    class Date(orderType: WalkOrderType): WalkOrder(orderType)
    class Time(orderType: WalkOrderType): WalkOrder(orderType)
}