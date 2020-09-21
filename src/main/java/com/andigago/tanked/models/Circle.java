package com.andigago.tanked.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Circle {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private float cx;

    @NotNull
    private float cy;

    @NotNull
    private float r;

    @NotNull
    private String fill;

    public Circle() {
        // for hibernate
    }
}
