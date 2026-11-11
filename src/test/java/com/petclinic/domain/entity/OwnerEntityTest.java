package com.petclinic.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OwnerEntityTest {

    @Test
    void equalsAndHashCode_contract() {
        Owner o1 = new Owner("John", "Doe", "john@example.com", "123");
        Owner o2 = new Owner("John", "Doe", "john@example.com", "123");
        assertThat(o1).isEqualTo(o1);
        assertThat(o1).isNotEqualTo(null);
        assertThat(o1.hashCode()).isEqualTo(o1.hashCode());
        // note: id is null in both; equals compares ids, so o1 != o2 is expected.
        assertThat(o1).isNotEqualTo(o2);
    }

    @Test
    void toString_containsFields() {
        Owner owner = new Owner("John", "Doe", "john@example.com", "123");
        assertThat(owner.toString()).contains("John", "Doe", "john@example.com");
    }
}
