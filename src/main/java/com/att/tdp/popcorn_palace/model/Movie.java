package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movies", uniqueConstraints = @UniqueConstraint(columnNames = "title"))
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private int duration; // in minutes

    @Column(nullable = false)
    private double rating; // 1-10 scale

    @Column(nullable = false)
    private int releaseYear;
}

