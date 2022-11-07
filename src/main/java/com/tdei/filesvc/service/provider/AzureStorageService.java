package com.tdei.filesvc.service.provider;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.tdei.filesvc.service.contract.IStorageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AzureStorageService implements IStorageProvider {

    private final CloudBlobContainer cloudBlobContainer;

    @Override
    public URI uploadBlob(InputStream sourceStream) {
        URI uri;
        try {
            CloudBlockBlob blob = cloudBlobContainer.getBlockBlobReference("fileName");
            blob.upload(sourceStream, -1);
            uri = blob.getUri();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (StorageException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(uri).orElseThrow(RuntimeException::new);
    }
}
