package com.example.recordsapi.models;

import lombok.*;


import javax.persistence.*;

@Data
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Table(name = "RECORDS")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "URL_LINK")
    private String urlLink;

    @Column(name = "CATEGORY")
    private String category;


    public Record(String urlLink, String category) {
        this.urlLink = urlLink;
        this.category= category;
    }
}