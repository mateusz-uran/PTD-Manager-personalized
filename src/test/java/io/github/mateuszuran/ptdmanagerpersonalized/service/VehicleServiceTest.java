package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.filestore.FileStore;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Vehicle;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
    @Mock
    private VehicleRepository repository;
    @Mock
    private FileStore fileStore;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private VehicleService service;
    private Vehicle vehicle;
    private MockMultipartFile file;

    @BeforeEach
    void setUp() {
        vehicle = Vehicle.builder()
                .truckMainModel("model")
                .truckLicensePlate("lop123")
                .truckType("lorry")
                .trailerLicensePlate("lub123")
                .trailerType("fridge")
                .leftTankFuelCapacity(250)
                .rightTankFuelCapacity(150)
                .adBlueCapacity(80)
                .trailerCapacity(220)
                .build();
    }

    @Test
    void givenVehicleId_whenGetVehicle_thenReturnObject() {
        //given
        given(repository.findById(vehicle.getId())).willReturn(Optional.of(vehicle));
        //when
        var result = service.getVehicle(vehicle.getId());
        //then
        assertThat(result).isEqualTo(vehicle);
    }

    @Test
    void givenUsername_whenSaveVehicle_thenReturnObject() {
        //given
        User user = new User(
                "john",
                "john123",
                false,
                "john123"
        );
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(repository.save(vehicle)).willReturn(vehicle);
        //when
        var result = service.saveVehicle(vehicle, user.getUsername());
        //then
        verify(repository).save(any(Vehicle.class));
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(vehicle);
    }

    @Test
    void givenFileAndVehicle_whenUpload_thenVerify() {
        //given
        file = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "test".getBytes()
        );
        given(repository.findById(vehicle.getId())).willReturn(Optional.of(vehicle));
        //when
        service.uploadVehicleImage(vehicle.getId(), file);
        //then
        verify(repository).save(any(Vehicle.class));
    }

    @Test
    void givenEmptyFileAndVehicle_whenUpload_thenThrowException() {
        //given
        file = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "".getBytes()
        );
        //when + then
        assertThatThrownBy(() -> service.uploadVehicleImage(vehicle.getId(), file))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("File not found");

    }

    @Test
    void givenWrongFileTypeAndVehicle_whenUpload_thenThrowException() {
        //given
        file = new MockMultipartFile(
                "image",
                "image.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test".getBytes());
        //when + then
        assertThatThrownBy(() -> service.uploadVehicleImage(vehicle.getId(), file))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("File must be an image");
    }

    @Test
    void givenFileAndVehicle_whenVehicleNotFound_thenThrowException() {
        //given
        given(repository.findById(vehicle.getId())).willReturn(Optional.empty());
        file = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "test".getBytes());
        //when + then
        assertThatThrownBy(() -> service.uploadVehicleImage(vehicle.getId(), file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Vehicle not found");
    }

    @Test
    void givenVehicle_whenDownloadFile_thenReturnFileByteArray() throws IOException {
        //given
        file = new MockMultipartFile(
                "image",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                "test".getBytes()
        );
        given(repository.findById(vehicle.getId())).willReturn(Optional.of(vehicle));
        //when
        when(service.downloadVehicleImage(vehicle.getId())).thenReturn(file.getBytes());
        var result = service.downloadVehicleImage(vehicle.getId());
        //then
        assertThat(result).isEqualTo(file.getBytes());
    }

    @Test
    void givenVehicle_whenDeleteObject_thenDoNothing() {
        //given
        given(repository.findById(vehicle.getId())).willReturn(Optional.of(vehicle));
        //when
        service.deleteVehicle(vehicle.getId());
        //then
        verify(repository, times(1)).delete(vehicle);
    }

    @Test
    void givenVehicle_whenDeleteImage_thenClearVehicleImageFields() {
        //given
        given(repository.findById(vehicle.getId())).willReturn(Optional.of(vehicle));
        //when
        service.deleteVehicleImage(vehicle.getId());
        //then
        assertThat(vehicle.getVehicleImageName()).isEmpty();
        assertThat(vehicle.getVehicleImagePath()).isEmpty();
    }
}