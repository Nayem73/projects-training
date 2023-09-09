package com.javafest.aifarming.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Garbage")
@Table(
        name = "garbage",
        uniqueConstraints = {
                @UniqueConstraint(name = "title_unique", columnNames = "title")
        }
)
public class Garbage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @OneToMany(
            mappedBy = "garbage",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private List<WhatGarbage> whatGarbages = new ArrayList<>();

    public Garbage() {
    }

    public Garbage(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
