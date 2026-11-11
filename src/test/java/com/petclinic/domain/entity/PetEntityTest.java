package com.petclinic.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PetEntityTest {

    @Test
    void equalsAndHashCode_contract() {
        Pet p1 = new Pet("Fluffy", "dog", null, "owner-1", null);
        Pet p2 = new Pet("Fluffy", "dog", null, "owner-1", null);
        assertThat(p1).isEqualTo(p1);
        assertThat(p1).isNotEqualTo(null);
        assertThat(p1.hashCode()).isEqualTo(p1.hashCode());
        // note: id is null in both; equals compares ids, so p1 != p2 is expected.
        assertThat(p1).isNotEqualTo(p2);
    }

    @Test
    void toString_containsFields() {
        Pet pet = new Pet("Fluffy", "dog", null, "owner-1", null);
        assertThat(pet.toString()).contains("Fluffy", "dog", "owner-1");
    }
}
