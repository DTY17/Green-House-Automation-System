package com.java.automationcontrolservice.repository;

import com.java.automationcontrolservice.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Log, Long> {
    Optional<Log> findAllByIdEquals(Long id);

    List<Log> getLogByDevice(String device);

    @Query("SELECT l FROM Log l WHERE l.device = :deviceId ORDER BY l.date DESC")
    Log findLatestLogByDevice(@Param("deviceId") String deviceId);
}
