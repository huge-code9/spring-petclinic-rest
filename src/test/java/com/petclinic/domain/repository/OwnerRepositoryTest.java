package com.petclinic.domain.repository;

import com.petclinic.domain.entity.Owner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OwnerRepositoryTest {

    @Autowired
    OwnerRepository ownerRepository;

    @Test
    void findByEmail_returnsOwner() {
        Owner owner = new Owner("John", "Doe", "john@example.com", "1234567890");
        ownerRepository.save(owner);

        Optional<Owner> found = ownerRepository.findByEmail("john@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }
}
