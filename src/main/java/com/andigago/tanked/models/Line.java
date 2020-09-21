package com.andigago.tanked.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Line {


    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private float x1;
    @NotNull
    private float x2;

    @NotNull
    private float y1;
    @NotNull
    private float y2;

    @NotNull
    private int strokeWidth;

    @NotNull
    private String stroke;

    public Line() {
        // for hibernate
    }


}
