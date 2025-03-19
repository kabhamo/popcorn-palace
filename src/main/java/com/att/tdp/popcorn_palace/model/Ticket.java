package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID bookingId;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime; // Relationship with Showtime

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private UUID userId;

    @PrePersist
    public void generateUUID() {
        if (bookingId == null) {
            bookingId = UUID.randomUUID();
        }
    }
}
