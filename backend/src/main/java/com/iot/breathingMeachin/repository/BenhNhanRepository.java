package com.iot.breathingMeachin.repository;

import com.iot.breathingMeachin.entity.BenhNhanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhanEntity, Integer> {

}
