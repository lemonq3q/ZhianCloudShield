package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Humidity {
    private Integer id;
    private String workshop;
    private long time;
    private float humidity;
}
