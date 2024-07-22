package com.example.librarymanagement.dto;

import com.example.librarymanagement.model.entity.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDto {
    private String bookTitle;
    private String author;
    private String nameOfLibrary;

    public ReservationDto(Reservation reservation) {
        this.bookTitle = reservation.getBook().getTitle();
        this.author = reservation.getBook().getAuthor()
                .getFirstName() + " " + reservation.getBook().getAuthor().getLastName();
        this.nameOfLibrary = reservation.getBook().getTitle();
    }
}