package com.petclinic.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petclinic.api.dto.PetRegistrationRequest;
import com.petclinic.api.dto.PetRegistrationResponse;
import com.petclinic.exception.InvalidBirthDateException;
import com.petclinic.exception.InvalidPetTypeException;
import com.petclinic.exception.OwnerNotFoundException;
import com.petclinic.service.PetRegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetRegistrationController.class)
class PetRegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PetRegistrationService petRegistrationService;

    @ParameterizedTest
    @CsvSource({
            "dog", "cat", "bird", "rabbit", "hamster", "fish", "reptile"
    })
    void registerPet_successful_returns201(String type) throws Exception {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", type, LocalDate.of(2020, 5, 15), "owner-123");
        PetRegistrationResponse resp = new PetRegistrationResponse("pet-456", "Fluffy", type, LocalDate.of(2020, 5, 15), "owner-123", LocalDate.now());
        when(petRegistrationService.registerPet(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("pet-456"))
                .andExpect(jsonPath("$.type").value(type));

        verify(petRegistrationService).registerPet(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {51, 100})
    void registerPet_nameExceedsMaxLength_returns400(int len) throws Exception {
        String name = "A".repeat(len);
        PetRegistrationRequest req = new PetRegistrationRequest(name, "dog", LocalDate.of(2020, 1, 1), "owner-123");

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[*].field", hasItem("name")))
                .andExpect(jsonPath("$.validationErrors[*].message", hasItem("Pet name cannot exceed 50 characters")));

        verifyNoInteractions(petRegistrationService);
    }

    @ParameterizedTest
    @ValueSource(ints = {31, 100})
    void registerPet_typeExceedsMaxLength_returns400(int len) throws Exception {
        String type = "B".repeat(len);
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", type, LocalDate.of(2020, 1, 1), "owner-123");

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[*].field", hasItem("type")))
                .andExpect(jsonPath("$.validationErrors[*].message", hasItem("Pet type cannot exceed 30 characters")));

        verifyNoInteractions(petRegistrationService);
    }

    @ParameterizedTest
    @MethodSource("invalidPetTypes")
    void registerPet_invalidType_returns400(String invalidType) throws Exception {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", invalidType, LocalDate.of(2020, 5, 15), "owner-123");
        when(petRegistrationService.registerPet(any()))
                .thenThrow(new InvalidPetTypeException(invalidType));

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString(invalidType)));

        verify(petRegistrationService).registerPet(any());
    }

    static Stream<String> invalidPetTypes() {
        return Stream.of("dragon", "unicorn", "tiger", "DOGG", "catt", "doggo");
    }

    @ParameterizedTest
    @MethodSource("futureDates")
    void registerPet_birthDateInFuture_returns400(LocalDate futureDate) throws Exception {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", "dog", futureDate, "owner-123");
        when(petRegistrationService.registerPet(any()))
                .thenThrow(new InvalidBirthDateException(futureDate));

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("cannot be in the future")));

        verify(petRegistrationService).registerPet(any());
    }

    static Stream<LocalDate> futureDates() {
        return Stream.of(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(1),
                LocalDate.now().plusYears(1)
        );
    }

    @Test
    void registerPet_ownerNotFound_returns404() throws Exception {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", "dog", LocalDate.of(2020, 5, 15), "owner-999");
        when(petRegistrationService.registerPet(any()))
                .thenThrow(new OwnerNotFoundException("owner-999"));

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message", containsString("owner-999")));

        verify(petRegistrationService).registerPet(any());
    }

    @Test
    void registerPet_malformedJson_returns400() throws Exception {
        String malformedJson = "{ \"name\": \"Fluffy\", \"type\": \"dog\", "; // truncated

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(petRegistrationService);
    }

    @Test
    void registerPet_missingContentType_returns415() throws Exception {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", "dog", LocalDate.of(2020, 1, 1), "owner-123");

        mockMvc.perform(post("/api/v1/pets")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnsupportedMediaType());

        verifyNoInteractions(petRegistrationService);
    }

    @Test
    void registerPet_invalidJson_returns400() throws Exception {
        String invalidJson = "{ name: Fluffy, type: dog, birthDate: 2020-01-01, ownerId: owner-123 }";

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(petRegistrationService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void registerPet_emptyOrBlankName_returns400(String name) throws Exception {
        PetRegistrationRequest req = new PetRegistrationRequest(name, "dog", LocalDate.of(2020, 1, 1), "owner-123");

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[*].field", hasItem("name")))
                .andExpect(jsonPath("$.validationErrors[*].message", hasItem("Pet name cannot be empty")));

        verifyNoInteractions(petRegistrationService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void registerPet_emptyOrBlankType_returns400(String type) throws Exception {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", type, LocalDate.of(2020, 1, 1), "owner-123");

        mockMvc.perform(post("/api/v1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[*].field", hasItem("type")))
                .andExpect(jsonPath("$.validationErrors[*].message", hasItem("Pet type cannot be empty")));

        verifyNoInteractions(petRegistrationService);
    }

    @Test
    void unsupportedHttpMethods_return405() throws Exception {
        mockMvc.perform(get("/api/v1/pets")).andExpect(status().isMethodNotAllowed());
        mockMvc.perform(put("/api/v1/pets")).andExpect(status().isMethodNotAllowed());
        mockMvc.perform(delete("/api/v1/pets")).andExpect(status().isMethodNotAllowed());
        verifyNoInteractions(petRegistrationService);
    }
}
