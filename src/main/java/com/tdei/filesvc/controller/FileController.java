package com.tdei.filesvc.controller;

import com.tdei.filesvc.controller.contract.IFileController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "File Service", description = "File service operations")
public class FileController implements IFileController {

    @Override
    public ResponseEntity<String> uploadFile(MultipartFile file, HttpServletRequest httpServletRequest) {
        return null;
    }
}
