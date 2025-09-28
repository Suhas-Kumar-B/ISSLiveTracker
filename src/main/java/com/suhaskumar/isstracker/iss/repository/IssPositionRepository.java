package com.suhaskumar.isstracker.iss.repository;

import com.suhaskumar.isstracker.iss.model.IssPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IssPositionRepository extends JpaRepository<IssPosition, Long>, JpaSpecificationExecutor<IssPosition> {

    @Query("SELECT AVG(p.latitude) FROM IssPosition p")
    Double getAverageLatitude();

    @Query("SELECT AVG(p.longitude) FROM IssPosition p")
    Double getAverageLongitude();
}