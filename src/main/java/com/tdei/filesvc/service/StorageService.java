package com.tdei.filesvc.service;

import com.tdei.filesvc.service.contract.IStorageService;
import com.tdei.filesvc.service.provider.AzureStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;

@Service
@RequiredArgsConstructor
public class StorageService implements IStorageService {
    private final AzureStorageService azureStorageService;

    @Override
    public URI uploadBlob(InputStream sourceStream) {
        return azureStorageService.uploadBlob(sourceStream);
    }
}
