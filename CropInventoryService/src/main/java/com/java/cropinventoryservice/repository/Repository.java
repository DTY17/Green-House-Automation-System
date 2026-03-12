package com.java.cropinventoryservice.repository;

import com.java.cropinventoryservice.entity.Crops;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Crops, Long> {
}
