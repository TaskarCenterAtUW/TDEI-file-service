package com.tdei.filesvc.osw.controller;

import com.tdei.filesvc.common.model.MetaValidationError;
import com.tdei.filesvc.core.config.exception.handler.exceptions.MetadataValidationException;
import com.tdei.filesvc.osw.controller.contract.IOswFileController;
import com.tdei.filesvc.osw.model.OswUpload;
import com.tdei.filesvc.osw.service.OswStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/osw/v1")
@Tag(name = "Osw File Service", description = "File service operations")
public class OswFileController implements IOswFileController {
    private final OswStorageService storageService;

    @Override
    public ResponseEntity<String> uploadOswFile(OswUpload meta, String tdeiOrgId, String userId, MultipartFile file, HttpServletRequest httpServletRequest) throws FileUploadException {
        List<MetaValidationError> metaValidationErrors = meta.isMetadataValidated();
        if(metaValidationErrors.isEmpty()) {
            return ResponseEntity.ok(storageService.uploadBlob(meta, tdeiOrgId, userId, file));
        }
        else {
            // Send the validation errors
            throw new MetadataValidationException("Error validating Metadata",metaValidationErrors);
        }
    }
}
