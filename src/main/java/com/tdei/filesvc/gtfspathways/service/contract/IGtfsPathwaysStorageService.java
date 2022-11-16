package com.tdei.filesvc.gtfspathways.service.contract;

import com.tdei.filesvc.gtfspathways.model.GtfsPathwaysUpload;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface IGtfsPathwaysStorageService {
    String uploadBlob(GtfsPathwaysUpload meta, String tdeiOrgId, String userId, MultipartFile file) throws FileUploadException;
}
