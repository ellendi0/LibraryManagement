package com.example.librarymanagement.dto.mapper;

import com.example.librarymanagement.dto.ReservationDto;
import com.example.librarymanagement.model.entity.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationMapper {
    public static ReservationDto toReservationDto(Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setBookTitle(reservation.getBook().getTitle());
        reservationDto.setAuthor(reservation.getBook().getAuthor().getFirstName()
                + " " + reservation.getBook().getAuthor().getLastName());
        if(reservation.getLibrary() != null){
            reservationDto.setNameOfLibrary(reservation.getLibrary().getName());
        }else {
            reservationDto.setNameOfLibrary("");
        }
        return reservationDto;
    }

    public static List<ReservationDto> toReservationDto(List<Reservation> reservation) {
        List<ReservationDto> reservationDtos = new ArrayList<>();

        if(!reservation.isEmpty()){
            reservationDtos = reservation.stream().map(ReservationMapper::toReservationDto).toList();
        }
        return reservationDtos;
    }
}