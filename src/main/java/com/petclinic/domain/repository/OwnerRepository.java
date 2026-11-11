package com.petclinic.domain.repository;

import com.petclinic.domain.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Owner entity operations.
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, String> {

    /**
     * Find owner by email.
     *
     * @param email Owner email
     * @return Optional containing the owner if found
     */
    Optional<Owner> findByEmail(String email);
}
