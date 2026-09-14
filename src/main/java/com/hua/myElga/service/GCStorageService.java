package com.hua.myElga.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Blob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GCStorageService {

    private final Storage storage;
    private final String bucketName;

    public GCStorageService(@Value("${gcs.bucket-name}") String bucketName) {
        this.storage = StorageOptions.getDefaultInstance().getService();
        this.bucketName = bucketName;
    }

    //// Upload file to Google Cloud Storage ////
    public String upload(byte[] file, String fileName) {

        // Specify where the file will be stored
        BlobId blobId = BlobId.of(bucketName, fileName);

        // Describe the object
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/pdf").build();

        // Upload the PDF to Google Cloud Storage
        storage.create(blobInfo, file);

        // Return the reference that we'll store in MySQL
        return fileName;
    }

    //// Download specified file from Google Cloud Storage ////
    public byte[] download(String objectName) {

        BlobId blobId = BlobId.of(bucketName, objectName);

        Blob blob = storage.get(blobId);

        if (blob == null) {
            throw new RuntimeException("File not found in Google Cloud Storage: " + objectName);
        }

        return blob.getContent();
    }
}
