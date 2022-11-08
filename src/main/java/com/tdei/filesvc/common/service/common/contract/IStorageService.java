package com.tdei.filesvc.common.service.common.contract;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    String uploadBlob(MultipartFile file, String agencyId, String containerName) throws FileUploadException;
}
