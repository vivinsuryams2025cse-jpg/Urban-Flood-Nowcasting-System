package com.flood.repository;

import com.flood.entity.Rainfall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * RainfallRepository.java
 * 
 * Spring Data JPA repository for Rainfall entity persistence and query operations.
 */
@Repository
public interface RainfallRepository extends JpaRepository<Rainfall, Long> {

    Optional<Rainfall> findFirstByLocationIgnoreCaseOrderByCreatedAtDesc(String location);

    List<Rainfall> findByLocationIgnoreCase(String location);

    List<Rainfall> findByIntensityGreaterThanEqual(double intensity);

    List<Rainfall> findByIntensityLevelIgnoreCase(String intensityLevel);
}
