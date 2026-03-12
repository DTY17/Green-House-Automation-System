package com.java.devices.repository;

import com.java.devices.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Repository extends JpaRepository<Device, Long> {
    Optional<Device> findAllByIdEquals(Long id);
}
