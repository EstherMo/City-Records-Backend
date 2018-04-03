package com.example.recordsapi.models;

import lombok.*;


import javax.persistence.*;

@Data
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Table(name = "RECORDS")
public class Record {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "URL_LINK")
    private String urlLink;

    @Column(name = "CATEGORY")
    private String category;


    public Record(String urlLink, String category, Long id) {
        this.id = id;
        this.urlLink = urlLink;
        this.category= category;
    }
}