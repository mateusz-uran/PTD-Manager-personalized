package io.github.mateuszuran.ptdmanagerpersonalized.bucket;

public enum BucketName {

    VEHICLE_IMAGE("ptd-individual");

    private final String bucketName;

    BucketName(final String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
