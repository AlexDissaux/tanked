package com.andigago.tanked.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by stella on 10/12/18.
 */

@Entity
@Data
public class Rectangle {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    private float x;

    @NotNull
    private float y;

    @NotNull
    private float width;

    @NotNull
    private float height;

    @NotNull
    private String fill;

    public Rectangle() {
    }


}