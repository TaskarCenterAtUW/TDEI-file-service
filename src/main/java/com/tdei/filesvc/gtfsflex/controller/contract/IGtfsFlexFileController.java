package com.tdei.filesvc.gtfsflex.controller.contract;

import com.tdei.filesvc.gtfsflex.model.GtfsFlexUpload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public interface IGtfsFlexFileController {
    @Operation(summary = "Uploading the GTFS flex file", description = "Uploading the file.  Returns the filePath for uploaded blob. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response - Returns the filePath for uploaded blob.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))),
            @ApiResponse(responseCode = "500", description = "An server error occurred.", content = @Content)
    })
    @RequestMapping(value = "uploadFile",
            produces = {"application/json"},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            method = RequestMethod.POST)
    ResponseEntity<String> uploadGtfsFlexFile(@RequestPart("meta") @Valid GtfsFlexUpload meta,
                                              @RequestPart("tdeiOrgId") @Valid String tdeiOrgId,
                                              @RequestPart("userId") @Valid String userId,
                                              @RequestPart("file") @NotNull MultipartFile file, HttpServletRequest httpServletRequest) throws FileUploadException;

}

