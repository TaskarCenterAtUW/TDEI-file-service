package com.tdei.filesvc.gtfspathways.service.contract;

import com.tdei.filesvc.gtfspathways.model.GtfsPathwaysUpload;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface IGtfsPathwaysStorageService {
    String uploadBlob(GtfsPathwaysUpload meta, String agencyId, MultipartFile file) throws FileUploadException;
}
