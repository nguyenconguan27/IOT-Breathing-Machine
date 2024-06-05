package com.iot.breathingMeachin.dto;

import lombok.Data;

@Data
public class ChiSoDto {
    private Integer id;
    private double nhipTim;
    private double spo2;
    private Integer idBenhNhan;
}
