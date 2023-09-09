package com.javafest.aifarming.model;

import jakarta.persistence.*;

@Entity(name = "WhatGarbagePicture")
@Table(name= "what_garbage_picture")
public class WhatGarbagePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String img;

    @ManyToOne
    @JoinColumn(
            name = "what_garbage_picture_id",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "what_garbage_picture_foreign_key"
            )
    )
    private WhatGarbage whatGarbage;

    public WhatGarbagePicture() {
    }

    public WhatGarbagePicture(String img, WhatGarbage whatGarbage) {
        this.img = img;
        this.whatGarbage = whatGarbage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public WhatGarbage getWhatGarbage() {
        return whatGarbage;
    }

    public void setWhatGarbage(WhatGarbage whatGarbage) {
        this.whatGarbage = whatGarbage;
    }
}
