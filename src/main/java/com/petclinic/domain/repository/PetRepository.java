package com.petclinic.domain.repository;

import com.petclinic.domain.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Pet entity operations.
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, String> {

    /**
     * Find all pets by owner ID.
     *
     * @param ownerId Owner identifier
     * @return List of pets belonging to the owner
     */
    List<Pet> findByOwnerId(String ownerId);
}
