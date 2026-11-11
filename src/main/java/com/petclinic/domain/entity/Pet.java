package com.petclinic.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Pet entity representing a registered pet in the system.
 */
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 30)
    private String type;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "registered_at", nullable = false)
    private LocalDate registeredAt;

    protected Pet() {
        // JPA requires a no-arg constructor
    }

    public Pet(String name, String type, LocalDate birthDate, String ownerId, LocalDate registeredAt) {
        this.name = name;
        this.type = type;
        this.birthDate = birthDate;
        this.ownerId = ownerId;
        this.registeredAt = registeredAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public LocalDate getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDate registeredAt) {
        this.registeredAt = registeredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(id, pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", birthDate=" + birthDate +
                ", ownerId='" + ownerId + '\'' +
                ", registeredAt=" + registeredAt +
                '}';
    }
}
