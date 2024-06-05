package com.iot.breathingMeachin.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "benhnhan")
@Data
public class BenhNhanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String ten;
    @Column
    private String ngaySinh;
    @Column
    private String diaChi;
    @Column
    private String benhLy;

    @OneToMany(mappedBy = "benhNhanEntity", cascade=CascadeType.REMOVE)
    List<ChiSoEntity> chiSoEntityList;

    @OneToOne(mappedBy = "benhNhanEntity")
    private MayThoEntity mayThoEntity;

}
