package com.iot.breathingMeachin.repository;

import com.iot.breathingMeachin.entity.MayThoEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MayThoRepository extends JpaRepository<MayThoEntity, Integer> {
    @Modifying
    @Transactional
    @Query(value = "update maytho set benh_nhan_id = null where id = :id", nativeQuery = true)
    void updateById(@Param("id") Integer id);
}
