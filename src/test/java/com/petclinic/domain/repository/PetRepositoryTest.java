package com.petclinic.domain.repository;

import com.petclinic.domain.entity.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PetRepositoryTest {

    @Autowired
    PetRepository petRepository;

    @Test
    void findByOwnerId_returnsPets() {
        Pet pet = new Pet("Fluffy", "dog", LocalDate.of(2020, 1, 1), "owner-123", LocalDate.now());
        petRepository.save(pet);

        List<Pet> pets = petRepository.findByOwnerId("owner-123");
        assertThat(pets).extracting(Pet::getName).contains("Fluffy");
    }
}
