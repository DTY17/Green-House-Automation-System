package com.java.zonemanagementservice.repository;

import com.java.zonemanagementservice.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface Repository extends JpaRepository<Zone, Long> {
    Optional<Zone> findAllByIdEquals(Long id);

    @Query("SELECT z FROM Zone z WHERE z.deviceId = :id")
    Zone findByDeviceId(@Param("id") String id);
}
