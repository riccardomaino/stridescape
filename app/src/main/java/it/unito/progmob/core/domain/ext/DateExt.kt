package it.unito.progmob.core.domain.ext

import java.util.Calendar
import java.util.Date

fun Date.toCalendar(): Calendar = Calendar.getInstance().also { it.time = this }