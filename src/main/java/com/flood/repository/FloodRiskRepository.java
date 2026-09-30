package com.flood.repository;

import com.flood.entity.FloodRisk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * FloodRiskRepository.java
 * 
 * Spring Data JPA repository for FloodRisk assessment records.
 */
@Repository
public interface FloodRiskRepository extends JpaRepository<FloodRisk, Long> {

    List<FloodRisk> findByLocationIgnoreCase(String location);

    Optional<FloodRisk> findFirstByLocationIgnoreCaseOrderByCreatedAtDesc(String location);

    List<FloodRisk> findByRiskLevelIgnoreCase(String riskLevel);

    List<FloodRisk> findAllByOrderByCreatedAtDesc();
}
