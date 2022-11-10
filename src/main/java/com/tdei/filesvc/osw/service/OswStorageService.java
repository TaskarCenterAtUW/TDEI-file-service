package com.tdei.filesvc.osw.service;

import com.tdei.filesvc.common.service.StorageService;
import com.tdei.filesvc.core.config.ApplicationProperties;
import com.tdei.filesvc.core.config.exception.handler.exceptions.FileExtensionNotAllowedException;
import com.tdei.filesvc.osw.model.OswUpload;
import com.tdei.filesvc.osw.service.contract.IOswStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.tdei.filesvc.common.utils.Utility.getExtensionByStringHandling;

@Service
@RequiredArgsConstructor
public class OswStorageService implements IOswStorageService {
    private final StorageService storageService;
    private final ApplicationProperties applicationProperties;

    @Override
    public String uploadBlob(OswUpload meta, String agencyId, MultipartFile file) throws FileUploadException {
        String fileExtension = getExtensionByStringHandling(file.getOriginalFilename()).get();
        List<String> allowedExtensions = Arrays.stream(applicationProperties.getOsw().getUploadAllowedExtensions().split(",")).toList();

        if (!allowedExtensions.contains(fileExtension)) {
            throw new FileExtensionNotAllowedException("Uploaded file extension not supported. Allowed extensions are " + applicationProperties.getOsw().getUploadAllowedExtensions());
        }

        String fileName = String.join(".", file.getName() + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", ""), fileExtension);
        String year = String.valueOf(LocalDateTime.now().getYear());
        String month = String.valueOf(LocalDateTime.now().getMonth());

        //Pattern: filetype/Year/Month/AgencyId/filename.extension
        //ex. gtfsflex/2022/November/101/testfile_1668063783868_295d783c624c4f86a7f09b116d55dfd0.zip
        String uploadPath = year + "/" + month + "/" + agencyId + "/" + fileName;
        return storageService.uploadBlob(file, uploadPath, applicationProperties.getOsw().getOswContainerName());
    }
}
