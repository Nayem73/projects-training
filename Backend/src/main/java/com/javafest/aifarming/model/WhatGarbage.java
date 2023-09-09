package com.javafest.aifarming.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity(name="WhatGarbage")
@Table(name = "what_garbage")
public class WhatGarbage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String img;
    private String description;
    @ManyToOne
    @JoinColumn(
            name = "what_garbage_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "what_garbage_foreign_key"
            )
    )
    private Garbage garbage;

    @OneToMany(
            mappedBy = "whatGarbage",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    @JsonIgnore // Add this annotation to break the circular reference
    private List<WhatGarbagePicture> whatGarbagePictures;

    public WhatGarbage() {
    }

    public WhatGarbage(String title, String img, String description) {
        this.title = title;
        this.img = img;
        this.description = description;
    }

    public WhatGarbage(String title, String img, String description, Garbage garbage) {
        this.title = title;
        this.img = img;
        this.description = description;
        this.garbage = garbage;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Garbage getGarbage() {
        return garbage;
    }

    public void setGarbage(Garbage garbage) {
        this.garbage = garbage;
    }

    public List<WhatGarbagePicture> getWhatGarbagePictures() {
        return whatGarbagePictures;
    }

    public void setWhatGarbagePictures(List<WhatGarbagePicture> whatGarbagePictures) {
        this.whatGarbagePictures = whatGarbagePictures;
    }
}
