package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.bucket.BucketName;
import io.github.mateuszuran.ptdmanagerpersonalized.filestore.FileStore;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Vehicle;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Slf4j
@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final FileStore fileStore;
    private final UserRepository userRepository;

    public VehicleService(final VehicleRepository vehicleRepository, final FileStore fileStore, final UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.fileStore = fileStore;
        this.userRepository = userRepository;
    }

    public Vehicle getVehicle(Long id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
    }

    public Vehicle saveVehicle(Vehicle vehicle, String username) {
        var result = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
        vehicle.setUser(result);
        return vehicleRepository.save(vehicle);
    }

    public void uploadVehicleImage(final Long id, final MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("File not found");
        }
        if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image");
        }
        if (vehicleRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("Vehicle not found");
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        String path = String.format("%s/%s", BucketName.VEHICLE_IMAGE.getBucketName(), id);
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
            vehicle.setVehicleImageName(fileName);
            vehicle.setVehicleImagePath(path);
            vehicleRepository.save(vehicle);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] downloadVehicleImage(final Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        String path = String.format("%s/%s", BucketName.VEHICLE_IMAGE.getBucketName(), id);
        return fileStore.download(path, vehicle.getVehicleImageName());
    }
}
