package com.iot.breathingMeachin.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "chiso")
@Data
public class ChiSoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private double nhipTim;
    @Column
    private double spo2;

    @ManyToOne
    @JoinColumn(name = "benh_nhan_id")
    private BenhNhanEntity benhNhanEntity;
}
