package org.example.entity;

import lombok.Data;

@Data
public class Humidity {
    private Integer id;
    private String workshop;
    private long time;
    private float humidity;
}
