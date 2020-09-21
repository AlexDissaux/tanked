package com.andigago.tanked.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MapRepository extends JpaRepository<Map, Long> {
    @Query(value="SELECT * FROM MAP ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Map findOneRandom();

    Map findByIdAndOwnerId(int mapId, Long owner);

    void deleteByIdAndOwnerId(int mapId, Long ownerId);
}

