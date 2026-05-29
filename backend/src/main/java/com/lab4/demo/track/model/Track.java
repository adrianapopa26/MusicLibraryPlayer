package com.lab4.demo.track.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import jakarta.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512, nullable = false)
    private String title;

    @Column(length = 512, nullable = false)
    private String link;

    @Column(length = 512, nullable = false)
    private int duration;

    @Column(length = 512, nullable = false)
    private Boolean explicit_lyrics;

    @Column(length = 512, nullable = false)
    private String preview;

    @Column(length = 512, nullable = false)
    private String artist;

    @Column(length = 512, nullable = false)
    private String album;
//
//    public Track() {
//    }
}
