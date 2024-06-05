package com.iot.breathingMeachin.repository;

import com.iot.breathingMeachin.entity.ChiSoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiSoRepository extends JpaRepository<ChiSoEntity, Integer> {
    @Query(value = "select * from chiso where benh_nhan_id = :idBenhNhan", nativeQuery = true)
    List<ChiSoEntity> getAllByIdBenhNhan(@Param("idBenhNhan") Integer idBenhNhan);
}
