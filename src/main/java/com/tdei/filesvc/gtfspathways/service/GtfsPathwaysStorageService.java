package com.tdei.filesvc.gtfspathways.service;

import com.tdei.filesvc.common.service.StorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import com.tdei.filesvc.gtfspathways.model.GtfsPathwaysUpload;
import com.tdei.filesvc.gtfspathways.service.contract.IGtfsPathwaysStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class GtfsPathwaysStorageService implements IGtfsPathwaysStorageService {
    private final StorageService storageService;
    private final ApplicationProperties applicationProperties;

    @Override
    public String uploadBlob(GtfsPathwaysUpload meta, String agencyId, MultipartFile file) throws FileUploadException {
        return storageService.uploadBlob(file, agencyId, applicationProperties.getApplication().getGtfsPathwaysContainerName());
    }
}
