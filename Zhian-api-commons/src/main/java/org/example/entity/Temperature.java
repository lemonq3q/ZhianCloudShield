package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Temperature {
    private Integer id;
    private String workshop;
    private long time;
    private float temperature;
}
