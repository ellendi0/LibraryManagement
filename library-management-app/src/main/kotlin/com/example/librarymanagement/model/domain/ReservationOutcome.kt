package com.example.librarymanagement.model.domain

sealed class ReservationOutcome {
    data class Journals(val journals: List<Journal>) : ReservationOutcome()
    data class Reservations(val reservations: List<Reservation>) : ReservationOutcome()
}
