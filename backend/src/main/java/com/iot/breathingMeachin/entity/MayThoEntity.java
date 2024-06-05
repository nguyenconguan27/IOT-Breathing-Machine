package com.iot.breathingMeachin.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "maytho")
@Data
public class MayThoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "benh_nhan_id")
    private BenhNhanEntity benhNhanEntity;
}
