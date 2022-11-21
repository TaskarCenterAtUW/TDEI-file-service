package com.tdei.filesvc.gtfspathways.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GtfsPathwaysUploadMessage extends GtfsPathwaysUpload {
    @JsonProperty("file_upload_path")
    private String fileUploadPath;
    @JsonProperty("user_id")
    private String userId;
}
