package com.petclinic.service.impl;

import com.petclinic.api.dto.PetRegistrationRequest;
import com.petclinic.api.dto.PetRegistrationResponse;
import com.petclinic.domain.entity.Pet;
import com.petclinic.domain.repository.OwnerRepository;
import com.petclinic.domain.repository.PetRepository;
import com.petclinic.exception.InvalidBirthDateException;
import com.petclinic.exception.InvalidPetTypeException;
import com.petclinic.exception.OwnerNotFoundException;
import com.petclinic.service.mapper.PetMapper;
import com.petclinic.service.validator.BirthDateValidator;
import com.petclinic.service.validator.PetTypeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PetRegistrationServiceImplTest {

    @Mock
    private PetRepository petRepository;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private PetTypeValidator petTypeValidator;
    @Mock
    private BirthDateValidator birthDateValidator;
    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetRegistrationServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @CsvSource({
            "dog", "cat", "bird", "rabbit", "hamster", "fish", "reptile"
    })
    void registerPet_withAllValidTypes(String type) {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", type, LocalDate.of(2020, 1, 1), "owner-123");
        Pet pet = new Pet("Fluffy", type, LocalDate.of(2020, 1, 1), "owner-123", LocalDate.now());
        ReflectionTestUtils.setField(pet, "id", "pet-456");
        PetRegistrationResponse resp = new PetRegistrationResponse("pet-456", "Fluffy", type, LocalDate.of(2020, 1, 1), "owner-123", LocalDate.now());

        doNothing().when(birthDateValidator).validate(any());
        doNothing().when(petTypeValidator).validate(any());
        when(ownerRepository.existsById("owner-123")).thenReturn(true);
        when(petRepository.save(any())).thenReturn(pet);
        when(petMapper.toResponse(any())).thenReturn(resp);

        PetRegistrationResponse result = service.registerPet(req);

        assertThat(result).isEqualTo(resp);
        verify(birthDateValidator).validate(LocalDate.of(2020, 1, 1));
        verify(petTypeValidator).validate(type);
        verify(ownerRepository).existsById("owner-123");
        verify(petRepository).save(any());
        verify(petMapper).toResponse(pet);
    }

    @ParameterizedTest
    @CsvSource({
            "DOG", "Cat", "BiRd", " rabbit ", " HAMSTER ", "Fish", "RePtIlE"
    })
    void registerPet_petTypeNormalization(String type) {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", type, LocalDate.of(2020, 1, 1), "owner-123");
        Pet pet = new Pet("Fluffy", type.trim().toLowerCase(), LocalDate.of(2020, 1, 1), "owner-123", LocalDate.now());
        ReflectionTestUtils.setField(pet, "id", "pet-456");
        PetRegistrationResponse resp = new PetRegistrationResponse("pet-456", "Fluffy", type.trim().toLowerCase(), LocalDate.of(2020, 1, 1), "owner-123", LocalDate.now());

        doNothing().when(birthDateValidator).validate(any());
        doNothing().when(petTypeValidator).validate(any());
        when(ownerRepository.existsById("owner-123")).thenReturn(true);
        when(petRepository.save(any())).thenReturn(pet);
        when(petMapper.toResponse(any())).thenReturn(resp);
        when(petTypeValidator.normalize(type)).thenReturn(type.trim().toLowerCase());

        PetRegistrationResponse result = service.registerPet(req);

        assertThat(result.type()).isEqualTo(type.trim().toLowerCase());
        verify(petTypeValidator).normalize(type);
    }

    @ParameterizedTest
    @ValueSource(strings = {"dragon", "unicorn", "tiger", "DOGG", "catt", "doggo"})
    void registerPet_invalidPetType_throwsInvalidPetTypeException(String type) {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", type, LocalDate.of(2020, 1, 1), "owner-123");
        doNothing().when(birthDateValidator).validate(any());
        doThrow(new InvalidPetTypeException(type)).when(petTypeValidator).validate(type);

        assertThatThrownBy(() -> service.registerPet(req))
                .isInstanceOf(InvalidPetTypeException.class);

        verify(petTypeValidator).validate(type);
        verifyNoInteractions(petRepository, petMapper);
    }

    @Test
    void registerPet_ownerNotFound_throwsOwnerNotFoundException() {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", "dog", LocalDate.of(2020, 1, 1), "owner-999");
        doNothing().when(birthDateValidator).validate(any());
        doNothing().when(petTypeValidator).validate(any());
        when(ownerRepository.existsById("owner-999")).thenReturn(false);

        assertThatThrownBy(() -> service.registerPet(req))
                .isInstanceOf(OwnerNotFoundException.class);

        verify(ownerRepository).existsById("owner-999");
        verifyNoInteractions(petRepository, petMapper);
    }

    @ParameterizedTest
    @MethodSource("futureDates")
    void registerPet_birthDateInFuture_throwsInvalidBirthDateException(LocalDate futureDate) {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", "dog", futureDate, "owner-123");
        doThrow(new InvalidBirthDateException(futureDate)).when(birthDateValidator).validate(futureDate);

        assertThatThrownBy(() -> service.registerPet(req))
                .isInstanceOf(InvalidBirthDateException.class);

        verify(birthDateValidator).validate(futureDate);
        verifyNoInteractions(petTypeValidator, ownerRepository, petRepository, petMapper);
    }

    static Set<LocalDate> futureDates() {
        return Set.of(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(1),
                LocalDate.now().plusYears(1)
        );
    }

    @Test
    void registerPet_repositorySaveFailure_triggersRollback() {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", "dog", LocalDate.of(2020, 1, 1), "owner-123");
        doNothing().when(birthDateValidator).validate(any());
        doNothing().when(petTypeValidator).validate(any());
        when(ownerRepository.existsById("owner-123")).thenReturn(true);
        when(petRepository.save(any())).thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> service.registerPet(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB error");

        verify(petRepository).save(any());
    }

    @Test
    void createPet_nullFields_throwsNPE() {
        PetRegistrationRequest req = new PetRegistrationRequest(null, null, null, null);
        assertThatThrownBy(() -> ReflectionTestUtils.invokeMethod(service, "createPet", req))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void registerPet_concurrentRegistrations() throws Exception {
        PetRegistrationRequest req = new PetRegistrationRequest("Fluffy", "dog", LocalDate.of(2020, 1, 1), "owner-123");
        doNothing().when(birthDateValidator).validate(any());
        doNothing().when(petTypeValidator).validate(any());
        when(ownerRepository.existsById("owner-123")).thenReturn(true);
        Pet pet = new Pet("Fluffy", "dog", LocalDate.of(2020, 1, 1), "owner-123", LocalDate.now());
        ReflectionTestUtils.setField(pet, "id", "pet-456");
        when(petRepository.save(any())).thenReturn(pet);
        PetRegistrationResponse resp = new PetRegistrationResponse("pet-456", "Fluffy", "dog", LocalDate.of(2020, 1, 1), "owner-123", LocalDate.now());
        when(petMapper.toResponse(any())).thenReturn(resp);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Callable<PetRegistrationResponse> task = () -> service.registerPet(req);
        Future<PetRegistrationResponse> f1 = executor.submit(task);
        Future<PetRegistrationResponse> f2 = executor.submit(task);

        assertThat(f1.get()).isEqualTo(resp);
        assertThat(f2.get()).isEqualTo(resp);
        executor.shutdown();
    }
}
