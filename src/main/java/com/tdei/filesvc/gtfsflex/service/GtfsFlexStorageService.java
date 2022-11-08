package com.tdei.filesvc.gtfsflex.service;

import com.tdei.filesvc.common.service.StorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import com.tdei.filesvc.gtfsflex.model.GtfsFlexUpload;
import com.tdei.filesvc.gtfsflex.service.contract.IGtfsFlexStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GtfsFlexStorageService implements IGtfsFlexStorageService {
    private final StorageService storageService;
    private final ApplicationProperties applicationProperties;

    @Override
    public String uploadBlob(GtfsFlexUpload meta, String agencyId, MultipartFile file) throws FileUploadException {
        return storageService.uploadBlob(file, agencyId, applicationProperties.getApplication().getGtfsFlexContainerName());
    }
}
