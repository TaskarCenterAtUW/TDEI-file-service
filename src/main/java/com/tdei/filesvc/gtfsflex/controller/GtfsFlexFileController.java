package com.tdei.filesvc.gtfsflex.controller;

import com.tdei.filesvc.gtfsflex.controller.contract.IGtfsFlexFileController;
import com.tdei.filesvc.gtfsflex.model.GtfsFlexUpload;
import com.tdei.filesvc.gtfsflex.service.GtfsFlexStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gtfsflex/v1")
@Tag(name = "Gtfs Flex File Service", description = "File service operations")
public class GtfsFlexFileController implements IGtfsFlexFileController {

    private final GtfsFlexStorageService storageService;

    @Override
    public ResponseEntity<String> uploadGtfsFlexFile(GtfsFlexUpload meta, String tdeiOrgId, String userId, MultipartFile file, HttpServletRequest httpServletRequest) throws FileUploadException {
        return ResponseEntity.ok(storageService.uploadBlob(meta, tdeiOrgId, userId, file));
    }
}
