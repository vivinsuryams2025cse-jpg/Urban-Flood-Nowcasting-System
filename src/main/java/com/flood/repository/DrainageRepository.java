package com.flood.repository;

import com.flood.entity.Drainage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DrainageRepository.java
 * 
 * Spring Data JPA repository for Drainage system persistence and querying.
 */
@Repository
public interface DrainageRepository extends JpaRepository<Drainage, Long> {

    Optional<Drainage> findByDrainageIdIgnoreCase(String drainageId);

    Optional<Drainage> findFirstByLocationIgnoreCase(String location);

    List<Drainage> findByStatusIgnoreCase(String status);

    List<Drainage> findByStatusIn(List<String> statuses);

    boolean existsByDrainageIdIgnoreCase(String drainageId);
}
