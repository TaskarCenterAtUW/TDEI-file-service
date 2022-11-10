package com.tdei.filesvc.common.service;

import com.tdei.filesvc.common.service.common.contract.IStorageService;
import com.tdei.filesvc.common.service.provider.AzureStorageServiceProvider;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StorageService implements IStorageService {
    private final AzureStorageServiceProvider azureStorageService;

    @Override
    public String uploadBlob(MultipartFile file, String fileName, String containerName) throws FileUploadException {

        return azureStorageService.uploadBlob(file, fileName, containerName);
    }
}
