package com.tdei.filesvc.common.service;

import com.tdei.filesvc.common.service.common.contract.IStorageService;
import com.tdei.filesvc.common.service.provider.AzureStorageServiceProvider;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.tdei.filesvc.common.utils.Utility.getExtensionByStringHandling;

@Service
@RequiredArgsConstructor
public class StorageService implements IStorageService {
    private final AzureStorageServiceProvider azureStorageService;

    @Override
    public String uploadBlob(MultipartFile file, String agencyId, String containerName) throws FileUploadException {
        String extension = getExtensionByStringHandling(file.getOriginalFilename()).get();
        String fileName = String.join(".", agencyId + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", ""), extension);

        return azureStorageService.uploadBlob(file, fileName, containerName);
    }
}
