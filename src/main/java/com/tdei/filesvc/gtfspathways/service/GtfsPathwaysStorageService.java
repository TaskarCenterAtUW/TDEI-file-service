package com.tdei.filesvc.gtfspathways.service;

import com.tdei.filesvc.common.service.StorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import com.tdei.filesvc.core.config.exception.handler.exceptions.FileExtensionNotAllowedException;
import com.tdei.filesvc.gtfspathways.model.GtfsPathwaysUpload;
import com.tdei.filesvc.gtfspathways.service.contract.IGtfsPathwaysStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.tdei.filesvc.common.utils.Utility.getExtensionByStringHandling;

@Service
@RequiredArgsConstructor
public class GtfsPathwaysStorageService implements IGtfsPathwaysStorageService {
    private final StorageService storageService;
    private final ApplicationProperties applicationProperties;

    @Override
    public String uploadBlob(GtfsPathwaysUpload meta, String agencyId, MultipartFile file) throws FileUploadException {
        String fileExtension = getExtensionByStringHandling(file.getOriginalFilename()).get();
        List<String> allowedExtensions = Arrays.stream(applicationProperties.getGtfsPathways().getUploadAllowedExtensions().split(",")).toList();

        if (!allowedExtensions.contains(fileExtension)) {
            throw new FileExtensionNotAllowedException("Uploaded file extension not supported. Allowed extensions are " + applicationProperties.getGtfsPathways().getUploadAllowedExtensions());
        }
        return storageService.uploadBlob(file, agencyId, applicationProperties.getGtfsPathways().getGtfsPathwaysContainerName());
    }
}
