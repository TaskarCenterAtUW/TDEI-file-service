package com.tdei.filesvc.gtfsflex.service.contract;

import com.tdei.filesvc.gtfsflex.model.GtfsFlexUpload;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface IGtfsFlexStorageService {
    String uploadBlob(GtfsFlexUpload meta, String tdeiOrgId, String userId, MultipartFile file) throws FileUploadException;
}
