package com.stefanomerlano.showcase.riskprocessor.persistence.repository;

import com.stefanomerlano.showcase.riskprocessor.persistence.entity.AnomalousTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for AnomalousTransactionEntity.
 * Provides CRUD (Create, Read, Update, Delete) operations out of the box.
 */
@Repository
public interface AnomalousTransactionRepository extends JpaRepository<AnomalousTransactionEntity, Long> {
}