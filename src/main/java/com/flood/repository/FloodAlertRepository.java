package com.flood.repository;

import com.flood.entity.FloodAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FloodAlertRepository.java
 * 
 * Spring Data JPA repository for civil flood alerts and evacuation advisories.
 */
@Repository
public interface FloodAlertRepository extends JpaRepository<FloodAlert, Long> {

    List<FloodAlert> findByLocationIgnoreCase(String location);

    List<FloodAlert> findByThreatLevelIgnoreCase(String threatLevel);

    List<FloodAlert> findAllByOrderByIssuedAtDesc();
}
