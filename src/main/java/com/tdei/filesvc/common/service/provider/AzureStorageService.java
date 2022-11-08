package com.tdei.filesvc.common.service.provider;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobErrorCode;
import com.azure.storage.blob.models.BlobStorageException;
import com.tdei.filesvc.common.service.common.contract.IStorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Service
public class AzureStorageService implements IStorageService {
    private final ApplicationProperties applicationProperties;
    BlobServiceClient blobServiceClient;
    private BlobContainerClient blobClient;

    public AzureStorageService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        initStorage();
    }

    private void initStorage() {
        blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(applicationProperties.getCloud().getAzure().getStorage().getBlob().getEndpoint())
                .connectionString(applicationProperties.getCloud().getAzure().getStorage().getBlob().getConnectionString())
                .buildClient();
    }

    private BlobContainerClient getContainerClient(String containerName) {
        try {
            return blobServiceClient.createBlobContainerIfNotExists(containerName);
        } catch (BlobStorageException ex) {
            // The container may already exist, so don't throw an error
            if (!ex.getErrorCode().equals(BlobErrorCode.CONTAINER_ALREADY_EXISTS)) {
                //Suppress error
            }
        }
        return blobServiceClient.getBlobContainerClient(containerName);
    }


    @Override
    public String uploadBlob(MultipartFile file, String fileName, String containerName) throws FileUploadException {

        String uri;
        try {
            BlobContainerClient containerClient = getContainerClient(containerName);
            BlobClient blob = containerClient.getBlobClient(fileName);
            blob.upload(file.getInputStream(), file.getSize(), true);
            uri = blob.getBlobUrl();
        } catch (Exception e) {
            throw new FileUploadException("File Upload failed", e.getCause());
        }

        return Optional.ofNullable(uri).orElseThrow(RuntimeException::new);
    }
}
