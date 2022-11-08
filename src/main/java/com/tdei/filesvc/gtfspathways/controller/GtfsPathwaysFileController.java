package com.tdei.filesvc.gtfspathways.controller;

import com.tdei.filesvc.gtfspathways.controller.contract.IGtfsPathwaysFileController;
import com.tdei.filesvc.gtfspathways.model.GtfsPathwaysUpload;
import com.tdei.filesvc.gtfspathways.service.GtfsPathwaysStorageService;
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
@RequestMapping("/api/gtfspathways/v1")
@Tag(name = "Gtfs Pathways File Service", description = "File service operations")
public class GtfsPathwaysFileController implements IGtfsPathwaysFileController {

    private final GtfsPathwaysStorageService storageService;

    @Override
    public ResponseEntity<String> uploadGtfsPathwaysFile(GtfsPathwaysUpload meta, String agencyId, MultipartFile file, HttpServletRequest httpServletRequest) throws FileUploadException {
        return ResponseEntity.ok(storageService.uploadBlob(meta, agencyId, file));
    }

}
