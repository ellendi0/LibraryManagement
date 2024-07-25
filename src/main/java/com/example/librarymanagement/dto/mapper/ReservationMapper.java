package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.ReservationDto;
import com.example.librarymanagement.model.entity.Author;
import com.example.librarymanagement.model.entity.Book;
import com.example.librarymanagement.model.entity.Reservation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationMapper {
    public ReservationDto toReservationDto(Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setBookTitle(getBook(reservation).getTitle());
        reservationDto.setAuthor(getAuthor(reservation).getFirstName() + " " + getAuthor(reservation).getLastName());
        reservationDto.setNameOfLibrary(getBook(reservation).getPublisher().getName());

        return reservationDto;
    }

    public List<ReservationDto> toReservationDto(List<Reservation> reservation) {
        if (reservation == null || reservation.isEmpty()) return new ArrayList<>();

        return reservation.stream()
                .map(this::toReservationDto)
                .toList();
    }

    private Book getBook(Reservation reservation) {
        return reservation.getBook();
    }

    private Author getAuthor(Reservation reservation) {
        return getBook(reservation).getAuthor();
    }
}