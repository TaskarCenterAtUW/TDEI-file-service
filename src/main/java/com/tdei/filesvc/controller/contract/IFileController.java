package com.tdei.filesvc.controller.contract;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Validated
public interface IFileController {
    @Operation(summary = "Uploading the file", description = "Uploading the file.  Returns the filePath for uploaded blob. ",
            tags = {"File Service"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response - Returns the filePath for uploaded blob.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))),
            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)
    })
    @RequestMapping(value = "uploadFile",
            produces = {"application/json"},
            consumes = {"text/plain"},
            method = RequestMethod.POST)
    ResponseEntity<String> uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest httpServletRequest);
}

