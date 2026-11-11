package com.petclinic.service.mapper;

import com.petclinic.api.dto.PetRegistrationResponse;
import com.petclinic.domain.entity.Pet;
import org.springframework.stereotype.Component;

/**
 * Mapper for Pet entity to DTO conversions.
 */
@Component
public class PetMapper {

    /**
     * Map Pet entity to PetRegistrationResponse DTO.
     *
     * @param pet Pet entity
     * @return PetRegistrationResponse DTO
     */
    public PetRegistrationResponse toResponse(Pet pet) {
        return new PetRegistrationResponse(
                pet.getId(),
                pet.getName(),
                pet.getType(),
                pet.getBirthDate(),
                pet.getOwnerId(),
                pet.getRegisteredAt()
        );
    }
}
