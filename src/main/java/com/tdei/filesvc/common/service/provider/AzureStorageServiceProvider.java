package com.tdei.filesvc.common.service.provider;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobErrorCode;
import com.azure.storage.blob.models.BlobStorageException;
import com.azure.storage.blob.models.ParallelTransferOptions;
import com.tdei.filesvc.common.service.common.contract.IStorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.util.Optional;


@Service
@Slf4j
public class AzureStorageServiceProvider implements IStorageService {
    private final ApplicationProperties applicationProperties;
    BlobServiceAsyncClient blobServiceAsyncClient;
    private BlobContainerClient blobClient;

    public AzureStorageServiceProvider(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        initStorage();
    }

    private void initStorage() {
        blobServiceAsyncClient = new BlobServiceClientBuilder()
                .endpoint(applicationProperties.getCloud().getAzure().getStorage().getBlob().getEndpoint())
                .connectionString(applicationProperties.getCloud().getAzure().getStorage().getBlob().getConnectionString())
                .buildAsyncClient();
    }

    private BlobContainerAsyncClient getContainerClient(String containerName) {
        try {
            return blobServiceAsyncClient.createBlobContainerIfNotExists(containerName).block();
        } catch (BlobStorageException ex) {
            // The container may already exist, so don't throw an error
            if (!ex.getErrorCode().equals(BlobErrorCode.CONTAINER_ALREADY_EXISTS)) {
                //Suppress error
            }
        }
        return blobServiceAsyncClient.getBlobContainerAsyncClient(containerName);
    }


    @Override
    public String uploadBlob(MultipartFile file, String fileName, String containerName) throws FileUploadException {

        String uri;
        long blockSize = 2L * 1024L * 1024L; //2MB

        try {
            BlobContainerAsyncClient containerClient = getContainerClient(containerName);
            BlobAsyncClient blobAsyncClient = containerClient.getBlobAsyncClient(fileName);

            blobAsyncClient.upload(covertByteArrayToFlux(file.getInputStream().readAllBytes()),
                            getTransferOptions(blockSize), true)
                    .doOnSuccess(blockBlobItem -> log.info("Successfully uploaded !!"))
                    .doOnError(throwable -> log.error(
                            "Error occurred while uploading !! Exception:{}",
                            throwable.getMessage()))
                    .subscribe();

            //blob.upload(file.getInputStream(), file.getSize(), true);
            uri = blobAsyncClient.getBlobUrl();
        } catch (Exception e) {
            throw new FileUploadException("File Upload failed", e.getCause());
        }

        return Optional.ofNullable(uri).orElseThrow(RuntimeException::new);
    }

    /**
     * Covert byte array to flux flux.
     *
     * @param byteArray the byte array
     * @return the flux
     */
    public Flux<ByteBuffer> covertByteArrayToFlux(byte[] byteArray) {
        return Flux.just(ByteBuffer.wrap(byteArray));
    }

    /**
     * Creating TransferOptions.
     *
     * @param blockSize represents block size
     * @return ParallelTransferOptions transfer options
     */
    public ParallelTransferOptions getTransferOptions(long blockSize) {
        return new ParallelTransferOptions()
                .setBlockSizeLong(blockSize)
                .setMaxConcurrency(5);
    }
}
