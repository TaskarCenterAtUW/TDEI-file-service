package com.tdei.filesvc.osw.service.contract;

import com.tdei.filesvc.osw.model.OswUpload;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface IOswStorageService {
    String uploadBlob(OswUpload meta, String agencyId, MultipartFile file) throws FileUploadException;
}
